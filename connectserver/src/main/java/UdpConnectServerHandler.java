import exceptions.UdpConnectServerHandlerException;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UdpConnectServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
 private final static int DELAY_IN_MILLIS = 0;
 private final static int PERIOD_IN_MILLIS = 1000;
 private final static int PACKET_TIMEOUT_IN_MILLIS = 1000 * 5;
 private final static Timer scheduler = new Timer();
 private final static Logger logger = LogManager.getLogger(UdpConnectServerHandler.class);
 private final Map<Short, GameServerSettings> gameServersSettingsMap = new HashMap<>();
 private final ConcurrentHashMap<Byte, AbstractPacket> packets = new ConcurrentHashMap<>();

 public UdpConnectServerHandler(ConnectServerSettings connectServerSettings) {
  Map<Short, List<GameServerSettings>> gameServersGroupingBy = connectServerSettings.gameServers().stream().collect(Collectors.groupingBy(x -> x.serverCode()));
  for (Map.Entry<Short, List<GameServerSettings>> entry : gameServersGroupingBy.entrySet()) {
   gameServersSettingsMap.put(entry.getKey(), entry.getValue().get(0));
  }
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {
  scheduler.schedule(new TimerTask() {
   @Override
   public void run() {
    for (Byte headCode : packets.keySet()) {
     switch (headCode) {
      case 1: {
       AbstractPacket abstractPacket = packets.getOrDefault(headCode, null);
       if (abstractPacket != null) {
        if (new Date().getTime() - abstractPacket.packetTime().getTime() > PACKET_TIMEOUT_IN_MILLIS) {
         logger.warn("Connection to the Game server has been interrupted");
        }
       }
      }
      break;
      case 2: {
       AbstractPacket abstractPacket = packets.getOrDefault(headCode, null);
       if (abstractPacket != null) {
        if (new Date().getTime() - abstractPacket.packetTime().getTime() > PACKET_TIMEOUT_IN_MILLIS) {
         logger.warn("Connection to the Join server has been interrupted");
        }
       }
      }
      break;
     }
    }
   }
  }, DELAY_IN_MILLIS, PERIOD_IN_MILLIS);
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) throws Exception {
  packets.clear();
  scheduler.cancel();
  scheduler.purge();
 }

 @Override
 public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
  logger.error(cause.getMessage(), cause);
  ctx.close();
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
      PMSG_GAMESERVERINFO gameServerInfo = PMSG_GAMESERVERINFO.deserialize(new ByteArrayInputStream(buffer));

      if (gameServerInfo.serverCode() < 0) {
       throw new UdpConnectServerHandlerException(String.format("Invalid server code: %d", gameServerInfo.serverCode()));
      }

      GameServerSettings gameServerSettings = gameServersSettingsMap.getOrDefault(gameServerInfo.serverCode(), null);

      if (gameServerSettings == null) {
       throw new UdpConnectServerHandlerException(String.format("Server code %d mismatching configuration", gameServerInfo.serverCode()));
      }

      if (!packets.containsKey(headCode)) {
       logger.info(String.format("Connection to the game server %d has been established", gameServerInfo.serverCode()));
      }

      packets.put(headCode, gameServerInfo);
     }
     break;
     case 2: {
      PMSG_JOINSERVERINFO joinServerInfo = PMSG_JOINSERVERINFO.deserialize(new ByteArrayInputStream(buffer));
      if (!packets.containsKey(headCode)) {
       logger.info("Connection to the join server has been established");
      }
      packets.put(headCode, joinServerInfo);
     }
     break;
     default: {
      InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
      if (remoteAddress != null) {
       logger.warn(String.format("Hacking attempt from: %s", remoteAddress.getAddress().getHostName()));
       ctx.close();
      }
     }
    }
   } else {
    InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
    if (remoteAddress != null) {
     logger.warn(String.format("Hacking attempt from: %s", remoteAddress.getAddress().getHostName()));
     ctx.close();
    }
   }
  }
 }
}