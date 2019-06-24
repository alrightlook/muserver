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
import tasks.HealthTask;

import java.io.ByteArrayInputStream;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UdpConnectServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
 private final static Logger logger = LogManager.getLogger(UdpConnectServerHandler.class);
 private final Map<Short, GameServerSettings> gameServersSettingsMap = new HashMap<>();
 private final ConcurrentHashMap<Byte, AbstractPacket> packets = new ConcurrentHashMap<>();

 public UdpConnectServerHandler(ConnectServerSettings connectServerSettings) {
  Map<Short, List<GameServerSettings>> gameServersGroupingBy = connectServerSettings.gameServers()
      .stream()
      .collect(Collectors.groupingBy(x -> x.serverCode()));

  for (Map.Entry<Short, List<GameServerSettings>> entry : gameServersGroupingBy.entrySet()) {
   gameServersSettingsMap.put(entry.getKey(), entry.getValue().get(0));
  }

  Timer timer = new Timer();

  timer.schedule(new HealthTask(packets), 0, 1000);
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

      packets.put(headCode, gameServerInfo);
     }
     break;
     case 2: {
      PMSG_JOINSERVERINFO joinServerInfo = PMSG_JOINSERVERINFO.deserialize(new ByteArrayInputStream(buffer));

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

 @Override
 public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
  logger.error(cause.getMessage(), cause);
  ctx.close();
 }
}