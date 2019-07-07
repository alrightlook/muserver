package muserver.joinserver.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UdpJoinServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
 private final static Logger logger = LogManager.getLogger(UdpJoinServerHandler.class);

 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {

 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) throws Exception {

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
 }
}