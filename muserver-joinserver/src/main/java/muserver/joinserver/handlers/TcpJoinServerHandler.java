package muserver.joinserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import jdk.nashorn.internal.objects.Global;
import muserver.common.Globals;
import muserver.common.messages.PBMSG_HEAD;
import muserver.common.utils.HexUtils;
import muserver.common.utils.NettyUtils;
import muserver.joinserver.messages.SDHP_IDPASS;
import muserver.joinserver.messages.SDHP_IDPASSRESULT;
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
   logger.info("Connection with: {} is accepted", remoteAddress.getAddress().getHostName());
  }

  clients.put(ctx.channel().id(), ctx);
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) throws Exception {
  InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();

  if (remoteAddress != null) {
   logger.info("Connection with: {} is interrupted", remoteAddress.getAddress().getHostName());
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
   logger.warn("Invalid buffer length: {}", byteBuf.readableBytes());
   NettyUtils.closeConnection(ctx);
  }

  byte[] buffer = new byte[byteBuf.readableBytes()];

  byteBuf.getBytes(0, buffer);

  if (buffer[0] != Globals.PMHC_BYTE) {
   logger.warn("Invalid protocol type: {}", buffer[0]);
   NettyUtils.closeConnection(ctx);
  }

  logger.trace(HexUtils.toString(buffer));

  switch (buffer[2]) {
   case 0: {
    getJoinInfo(ctx, buffer);
   }
   break;

   case 1: {
    // 0xC1 0x2C 0x1
    // 0x0
    // 0x28 0x23
    // 0x74 0x65 0x73 0x74 0x0 0x0 0x0 0x0 0x0 0x0 0x0
    // 0x74 0x65 0x73 0x74 0x0 0x0 0x0 0x0 0x0 0x0 0x0
    // 0x31 0x39 0x32 0x2E 0x31 0x36 0x38 0x2E 0x32 0x2E 0x36 0x0 0x0 0x0 0x0 0x0
    joinIdPassRequest(ctx, buffer);
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

   case 5: {

   }
   break;

   case 6: {

   }
   break;

   case 0x30: {

   }
   break;

   case 0x31: {

   }
   break;

   case (byte) 0xA0: {

   }
   break;

   case (byte) 0xA1: {

   }
   break;

   case (byte) 0xA2: {

   }
   break;

   case (byte) 0xA3: {

   }
   break;

   default:
    logger.warn("Unsupported header id: {}", buffer[2]);
    //throw new UnsupportedOperationException(String.format("Unsupported header id: %s", buffer[2]));
  }
 }


 private void getJoinInfo(ChannelHandlerContext ctx, byte[] buffer) throws IOException {
  SDHP_SERVERINFO serverInfo = SDHP_SERVERINFO.deserialize(new ByteArrayInputStream(buffer));
  logger.info("Server info -> name: {} port: {} type: {}", serverInfo.serverName(), serverInfo.port(), serverInfo.type());
  SDHP_RESULT result = SDHP_RESULT.create(PBMSG_HEAD.create(Globals.PMHC_BYTE, (byte) SDHP_RESULT.sizeOf(), (byte) 0), (byte) 1, 0);
  ctx.writeAndFlush(Unpooled.wrappedBuffer(result.serialize(new ByteArrayOutputStream())));
 }

 private void joinIdPassRequest(ChannelHandlerContext ctx, byte[] buffer) throws IOException {
  SDHP_IDPASS idPass = SDHP_IDPASS.deserialize(new ByteArrayInputStream(buffer));

  if (idPass.id() == null || idPass.id().isEmpty()) {
//   spResult.h.size     = sizeof( spResult );
//   spResult.h.c		= PMHC_BYTE;
//   spResult.h.headcode = 0x01;
//   spResult.result     = result;
//   spResult.Number     = lpMsgIdPass->Number;
//   spResult.UserNumber = UserNumber;
//   spResult.DBNumber   = DBNumber;

//   SDHP_IDPASSRESULT.create(
//       PBMSG_HEAD.create(Globals.PMHC_BYTE, (byte) 0, (byte) 1),
//
//   );
  }
 }
}