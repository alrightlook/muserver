package muserver.joinserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.common.Globals;
import muserver.common.messages.PBMSG_HEAD;
import muserver.common.utils.HexUtils;
import muserver.common.utils.NettyUtils;
import muserver.joinserver.contexts.JoinServerContext;
import muserver.joinserver.exceptions.JoinServerException;
import muserver.joinserver.messages.SDHP_IDPASS;
import muserver.joinserver.messages.SDHP_IDPASSRESULT;
import muserver.joinserver.messages.SDHP_RESULT;
import muserver.joinserver.messages.SDHP_SERVERINFO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TcpJoinServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private final static Logger logger = LogManager.getLogger(TcpJoinServerHandler.class);

 public TcpJoinServerHandler(JoinServerContext joinServerContext) {
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Connection accepted: {}", ctx.channel().remoteAddress().toString());
  }
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) throws Exception {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Connection interrupted: {}", ctx.channel().remoteAddress().toString());
  }
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
   throw new JoinServerException(String.format("Invalid buffer length: %d", byteBuf.readableBytes()));
  }

  byte[] buffer = new byte[byteBuf.readableBytes()];

  byteBuf.getBytes(0, buffer);

  if (buffer[0] != Globals.PMHC_BYTE) {
   throw new JoinServerException(String.format("Invalid protocol type: %d", buffer[0]));
  }

  logger.trace(HexUtils.toString(buffer));

  switch (buffer[2]) {
   case 0: {
    getJoinInfo(ctx, buffer);
   }
   break;

   case 1: {
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
    throw new JoinServerException(String.format("Unsupported header code: %s", buffer[2]));
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

  String joominNumber = "";
  Integer userNumber = -1, dbNumber = 0;

  //byte result
  //2: Incorrect password

  SDHP_IDPASSRESULT idPassResult = SDHP_IDPASSRESULT.create(
      PBMSG_HEAD.create(Globals.PMHC_BYTE, (byte) SDHP_IDPASSRESULT.sizeOf(), (byte) 1),
      (byte) 2,
      idPass.number(),
      idPass.id(),
      userNumber,
      dbNumber,
      joominNumber
  );

  ctx.writeAndFlush(Unpooled.wrappedBuffer(idPassResult.serialize(new ByteArrayOutputStream())));
 }
}