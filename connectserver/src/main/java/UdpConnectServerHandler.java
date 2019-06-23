import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import exceptions.ConnectServerException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import messages.AbstractPacket;
import messages.PMSG_GAMESERVERINFO;
import messages.PMSG_JOINSERVERINFO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import settings.ConnectServerSettings;
import settings.GameServerSettings;

import java.io.ByteArrayInputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class UdpConnectServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
 private final static int EXPIRE_AFTER_WRITE_DURATION = 1000 * 5;
 private final static Logger logger = LogManager.getLogger(UdpConnectServerHandler.class);
 private final static Cache<Byte, AbstractPacket> packetsCache = CacheBuilder.newBuilder()
     .expireAfterAccess(EXPIRE_AFTER_WRITE_DURATION, TimeUnit.MILLISECONDS)
     .removalListener(removalNotification -> {
      byte headCode = (byte) removalNotification.getKey();
      switch (headCode) {
       case 1:
        logger.info("Connection to the game server has been interrupted");
        break;
       case 2:
        logger.warn("Connection to the join server has been interrupted");
        break;
      }
     })
     .maximumSize(Integer.MAX_VALUE)
     .build();
 private final ConnectServerSettings connectServerSettings;
 private final Map<Integer, GameServerSettings> gameServersSettingsMap = new HashMap<>();


 public UdpConnectServerHandler(ConnectServerSettings connectServerSettings) {
  this.connectServerSettings = connectServerSettings;
  Map<Integer, List<GameServerSettings>> gameServersGroupingBy = connectServerSettings.gameServers().stream().collect(Collectors.groupingBy(x -> x.serverCode()));
  for (Map.Entry<Integer, List<GameServerSettings>> entry : gameServersGroupingBy.entrySet()) {
   gameServersSettingsMap.put(entry.getKey(), entry.getValue().get(0));
  }
 }

 @Override
 public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
  ctx.flush();
 }

 @Override
 protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
  ByteBuf content = packet.content();
  if (content.readableBytes() > 0) {
   byte[] buffer = new byte[content.readableBytes()];
   content.getBytes(0, buffer);
   byte type = buffer[0], headCode = buffer[2];
   if (type == (byte) 0xC1) {
    switch (headCode) {
     case 1: {
      AbstractPacket abstractPacket = packetsCache.get(headCode, new Callable<AbstractPacket>() {
       @Override
       public AbstractPacket call() throws Exception {
        return PMSG_GAMESERVERINFO.deserialize(new ByteArrayInputStream(buffer));
       }
      });

      PMSG_GAMESERVERINFO gameServerInfo = (PMSG_GAMESERVERINFO) abstractPacket;

      GameServerSettings gameServerSettings = gameServersSettingsMap.getOrDefault(gameServerInfo.serverCode(), null);

      if (gameServerSettings == null) {
       throw new ConnectServerException(String.format("Server code %d mismatch configuration", gameServerInfo.serverCode()));
      }

      logger.info(String.format("Game server packet time: %s", abstractPacket.packetTime()));
     }
     break;
     case 2: {
      AbstractPacket abstractPacket = packetsCache.get(headCode, new Callable<AbstractPacket>() {
       @Override
       public AbstractPacket call() throws Exception {
        return PMSG_JOINSERVERINFO.deserialize(new ByteArrayInputStream(buffer));
       }
      });

      logger.info(String.format("Join server packet time: %s", abstractPacket.packetTime()));
     }
     break;
     default: {
      InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
      if (remoteAddress != null) {
       logger.warn(String.format("Hacking attempt from: %s", remoteAddress.getAddress().getHostName()));
      }
     }
    }
   } else {
    InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
    if (remoteAddress != null) {
     logger.warn(String.format("Hacking attempt from: %s", remoteAddress.getAddress().getHostName()));
    }
   }
  }
 }

 @Override
 public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
  logger.error(cause.getMessage(), cause);
  ctx.close();
 }
}