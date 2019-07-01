package handlers;

import configs.ServerListConfigs;
import exceptions.UdpConnectServerHandlerException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import messages.AbstractPacket;
import messages.PMSG_GAMESERVER_INFO;
import messages.PMSG_HEAD;
import messages.PMSG_JOINSERVER_INFO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class UdpConnectServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
 private final static int DELAY_IN_MILLIS = 0;
 private final static int PERIOD_IN_MILLIS = 1000;
 private final static int PACKET_TIMEOUT_IN_MILLIS = 1000 * 5;
 private final static Timer scheduler = new Timer();
 private final static Logger logger = LogManager.getLogger(UdpConnectServerHandler.class);
 private final static AtomicReference<PMSG_JOINSERVER_INFO> joinServerInfoReference = new AtomicReference<>();
 private final static ConcurrentHashMap<Short, AbstractPacket> abstractPackets = new ConcurrentHashMap<>();

 private final Map<Short, ServerListConfigs> serverListConfigsMap;

 public UdpConnectServerHandler(Map<Short, ServerListConfigs> serverListConfigsMap) {
  this.serverListConfigsMap = serverListConfigsMap;
 }

 public static AtomicReference<PMSG_JOINSERVER_INFO> getJoinServerInfoReference() {
  return joinServerInfoReference;
 }

 public static ConcurrentHashMap<Short, AbstractPacket> getAbstractPackets() {
  return abstractPackets;
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {
  scheduler.schedule(new TimerTask() {
   @Override
   public void run() {
    for (Short serverCode : abstractPackets.keySet()) {
     AbstractPacket abstractPacket = abstractPackets.getOrDefault(serverCode, null);
     if (abstractPacket != null) {
      if (new Date().getTime() - abstractPacket.packetTime().getTime() > PACKET_TIMEOUT_IN_MILLIS) {
       logger.warn(String.format("Game server connection has been interrupted. Server code: %d", serverCode));
       abstractPackets.remove(serverCode);
      }
     }
    }

    if (joinServerInfoReference.get() != null) {
     if (new Date().getTime() - joinServerInfoReference.get().packetTime().getTime() > PACKET_TIMEOUT_IN_MILLIS) {
      logger.warn(String.format("Join server connection has benn interrupted"));
      joinServerInfoReference.set(null);
     }
    }
   }
  }, DELAY_IN_MILLIS, PERIOD_IN_MILLIS);
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) throws Exception {
  scheduler.cancel();
  scheduler.purge();
  abstractPackets.clear();
  joinServerInfoReference.set(null);
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

   if (buffer.length < 3) {
    closeConnection(ctx);
    logger.warn(String.format("Invalid buffer length that equals to: %d", buffer.length));
   }

   PMSG_HEAD header = PMSG_HEAD.deserialize(new ByteArrayInputStream(buffer));

   switch (header.type()) {
    case (byte) 0xC1: {
     switch (header.headCode() ) {
      case 1: {
       PMSG_GAMESERVER_INFO gameServerInfo = PMSG_GAMESERVER_INFO.deserialize(new ByteArrayInputStream(buffer));

       ServerListConfigs ServerListConfigs = serverListConfigsMap.getOrDefault(gameServerInfo.serverCode(), null);

       if (ServerListConfigs == null) {
        throw new UdpConnectServerHandlerException(String.format("Server code %d mismatching configuration", gameServerInfo.serverCode()));
       }

       if (!abstractPackets.containsKey(gameServerInfo.serverCode())) {
        logger.info(String.format("Game server connection is up and run. Server code: %d", gameServerInfo.serverCode()));
       }

       abstractPackets.put(gameServerInfo.serverCode(), gameServerInfo);
      }
      break;
      case 2: {
       PMSG_JOINSERVER_INFO joinServerInfo = PMSG_JOINSERVER_INFO.deserialize(new ByteArrayInputStream(buffer));
       if (joinServerInfoReference.get() == null) {
        logger.info("Join server connection is up and run");
       }
       joinServerInfoReference.set(joinServerInfo);
      }
      break;
      default: {
       throw new UnsupportedOperationException(String.format("Unsupported head code type: %d", header.headCode()));
      }
     }
    }
    break;
    default:
     throw new UnsupportedOperationException(String.format("Unsupported protocol type: %d", header.type()));
   }
  } else {
   closeConnection(ctx);
  }
 }


 private void closeConnection(ChannelHandlerContext ctx) {
  InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
  if (remoteAddress != null) {
   logger.warn(String.format("Hacking attempt from: %s", remoteAddress.getAddress().getHostName()));
   ctx.close();
  }
 }
}