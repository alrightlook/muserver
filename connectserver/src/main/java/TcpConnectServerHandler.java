import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import settings.ConnectServerSettings;

import java.util.concurrent.ConcurrentHashMap;

public class TcpConnectServerHandler extends SimpleChannelInboundHandler<byte[]> {
 private final static Logger logger = LogManager.getLogger(TcpConnectServerHandler.class);
 private final static ConcurrentHashMap<ChannelId, Client> clients = new ConcurrentHashMap<>();

 public TcpConnectServerHandler(ConnectServerSettings connectServerSettings) {
 }

 @Override
 public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
  ctx.flush();
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {
  clients.put(ctx.channel().id(), Client.create(clients.size()));
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) throws Exception {
  clients.remove(ctx.channel().id());
 }

 @Override
 public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
  logger.error(cause.getMessage(), cause);
  ctx.close();
 }

 @Override
 protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) throws Exception {
  logger.info(bytes);
 }
}
