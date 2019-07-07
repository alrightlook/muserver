package muserver.joinserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class TcpJoinServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private final static Logger logger = LogManager.getLogger(TcpJoinServerHandler.class);
 private final static ConcurrentHashMap<ChannelId, ChannelHandlerContext> clients = new ConcurrentHashMap<>();

 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {
  clients.put(ctx.channel().id(), ctx);
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) throws Exception {
  InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();

  if (remoteAddress != null) {
   logger.info(String.format("Connection from: %s has been interrupted", remoteAddress.getAddress().getHostName()));
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

 }
}