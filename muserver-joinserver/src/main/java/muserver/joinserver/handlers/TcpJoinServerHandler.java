package muserver.joinserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.common.Globals;
import muserver.common.messages.PBMSG_HEAD;
import muserver.common.utils.NettyUtils;
import muserver.joinserver.messages.SDHP_IDPASS;
import muserver.joinserver.messages.SDHP_RESULT;
import muserver.joinserver.messages.SDHP_SERVERINFO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class TcpJoinServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private final static Logger logger = LogManager.getLogger(TcpJoinServerHandler.class);
 private final static ConcurrentHashMap<ChannelId, ChannelHandlerContext> clients = new ConcurrentHashMap<>();

 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {
  InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();

  if (remoteAddress != null) {
   logger.info(String.format("Connection with: {} is accepted", remoteAddress.getAddress().getHostName()));
  }

  clients.put(ctx.channel().id(), ctx);
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) throws Exception {
  InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();

  if (remoteAddress != null) {
   logger.info(String.format("Connection with: {} is interrupted", remoteAddress.getAddress().getHostName()));
  }

  clients.remove(ctx.channel().id());
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
 protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
  if (byteBuf.readableBytes() < 4) {
   logger.warn(String.format("Invalid buffer length: %d", byteBuf.readableBytes()));
   NettyUtils.closeConnection(ctx);
  }

  byte[] buffer = new byte[byteBuf.readableBytes()];

  byteBuf.getBytes(0, buffer);

  if (buffer[0] != Globals.PMHC_BYTE) {
   logger.warn(String.format("Invalid protocol type: %d", buffer[0]));
   NettyUtils.closeConnection(ctx);
  }

  switch (buffer[2]) {
   case 0: {
    handleJoinInfo(ctx, buffer);
   }
   break;

   case 1: {
    handleJoinIdPassRequest(ctx, buffer);
   }
   break;

   case 2: {

   }
   break;

   case 3: {

   }
   break;

   case 4: {

   }
   break;

   case 0x10: {

   }
   break;

   case 0x11: {

   }
   break;

   case 0x20: {

   }
   break;

   case 0x30: {

   }
   break;

   default:
    throw new UnsupportedOperationException(String.format("Unsupported header code: %s", buffer[2]));
  }
 }


 private void handleJoinInfo(ChannelHandlerContext ctx, byte[] buffer) throws IOException {
  SDHP_SERVERINFO serverInfo = SDHP_SERVERINFO.deserialize(new ByteArrayInputStream(buffer));
  logger.info("Server info -> name: {} port: {} type: {}", serverInfo.serverName(), serverInfo.port(), serverInfo.type());
  SDHP_RESULT result = SDHP_RESULT.create(PBMSG_HEAD.create(Globals.PMHC_BYTE, (byte) SDHP_RESULT.sizeOf(), (byte) 0), (byte) 1, 0);
  ctx.writeAndFlush(Unpooled.wrappedBuffer(result.serialize(new ByteArrayOutputStream())));
 }

 private void handleJoinIdPassRequest(ChannelHandlerContext ctx, byte[] buffer) throws IOException {
  SDHP_IDPASS idPass = SDHP_IDPASS.deserialize(new ByteArrayInputStream(buffer));
  ctx.close();
 }
}