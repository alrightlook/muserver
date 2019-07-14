package muserver.gameserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.common.Globals;
import muserver.common.utils.HexUtils;
import muserver.gameserver.contexts.GameServerContext;
import muserver.gameserver.exceptions.GameServerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TcpGameServerHandler  extends SimpleChannelInboundHandler<ByteBuf> {
 private final static Logger logger = LogManager.getLogger(TcpGameServerHandler.class);

 public TcpGameServerHandler(GameServerContext gameServerContext) {
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
   throw new GameServerException(String.format("Invalid buffer length: %d", byteBuf.readableBytes()));
  }

  byte[] buffer = new byte[byteBuf.readableBytes()];

  byteBuf.getBytes(0, buffer);

  logger.info(HexUtils.toString(buffer));
 }
}
