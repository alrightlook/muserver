package muserver.connectserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import muserver.common.Globals;
import muserver.common.configs.ServerConfigs;
import muserver.common.messages.AbstractPacket;
import muserver.common.messages.PBMSG_HEAD;
import muserver.common.utils.NettyUtils;
import muserver.connectserver.contexts.ConnectServerContext;
import muserver.connectserver.exceptions.UdpConnectServerHandlerException;
import muserver.connectserver.messages.PMSG_GAMESERVER_INFO;
import muserver.connectserver.messages.PMSG_JOINSERVER_INFO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

public class UdpConnectServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
 private final static int DELAY_IN_MILLIS = 0;
 private final static int PERIOD_IN_MILLIS = 1000;
 private final static int PACKET_TIMEOUT_IN_MILLIS = 1000 * 5;
 private final static Timer scheduler = new Timer();
 private final static Logger logger = LogManager.getLogger(UdpConnectServerHandler.class);
 private final static AtomicReference<PMSG_JOINSERVER_INFO> joinServerInfoReference = new AtomicReference<>();

 private final ConnectServerContext connectServerContext;

 public UdpConnectServerHandler(ConnectServerContext connectServerContext) {
  this.connectServerContext = connectServerContext;
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) {
  scheduler.schedule(new TimerTask() {
   @Override
   public void run() {
    for (Short serverCode : connectServerContext.packets().keySet()) {
     AbstractPacket abstractPacket = connectServerContext.packets().getOrDefault(serverCode, null);
     if (abstractPacket != null) {
      if (new Date().getTime() - abstractPacket.packetTime().getTime() > PACKET_TIMEOUT_IN_MILLIS) {
       logger.warn(String.format("Game server connection has been interrupted. Server code: %d", serverCode));
       connectServerContext.packets().remove(serverCode);
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
 public void channelInactive(ChannelHandlerContext ctx) {
  scheduler.cancel();
  scheduler.purge();
  connectServerContext.packets().clear();
  joinServerInfoReference.set(null);
 }

 @Override
 public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
  logger.error(cause.getMessage(), cause);
  ctx.close();
 }

 @Override
 public void channelReadComplete(ChannelHandlerContext ctx) {
  ctx.flush();
 }

 @Override
 protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
  ByteBuf content = packet.content();

  if (content.readableBytes() > 0) {
   byte[] buffer = new byte[content.readableBytes()];

   content.getBytes(0, buffer);

   if (buffer.length < 3) {
    NettyUtils.closeConnection(ctx);
    logger.warn(String.format("Invalid buffer length that equals to: %d", buffer.length));
   }

   PBMSG_HEAD header = PBMSG_HEAD.deserialize(new ByteArrayInputStream(buffer));

   switch (header.type()) {
    case Globals.C1_PACKET: {
     switch (header.headCode()) {
      case 1: {
       PMSG_GAMESERVER_INFO gameServerInfo = PMSG_GAMESERVER_INFO.deserialize(new ByteArrayInputStream(buffer));

       ServerConfigs serverConfigs = this.connectServerContext.serversConfigsMap().getOrDefault(gameServerInfo.serverCode(), null);

       if (serverConfigs == null) {
        throw new UdpConnectServerHandlerException(String.format("Server code %d mismatching configuration", gameServerInfo.serverCode()));
       }

       if (!connectServerContext.packets().containsKey(gameServerInfo.serverCode())) {
        logger.info(String.format("Game server connection established. Server code: %d", gameServerInfo.serverCode()));
       }

       connectServerContext.packets().put(gameServerInfo.serverCode(), gameServerInfo);
      }
      break;
      case 2: {
       PMSG_JOINSERVER_INFO joinServerInfo = PMSG_JOINSERVER_INFO.deserialize(new ByteArrayInputStream(buffer));
       if (joinServerInfoReference.get() == null) {
        logger.info("Join server connection established");
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
   NettyUtils.closeConnection(ctx);
  }
 }
}