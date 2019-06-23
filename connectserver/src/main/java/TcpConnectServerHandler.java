import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import settings.ConnectServerSettings;

import java.util.concurrent.ConcurrentHashMap;

public class TcpConnectServerHandler extends ChannelInboundHandlerAdapter {
 private final static Logger logger = LogManager.getLogger(TcpConnectServerHandler.class);
 private final ConnectServerSettings connectServerSettings;
 private final ConcurrentHashMap<ChannelId, Client> clients = new ConcurrentHashMap<>();

 public TcpConnectServerHandler(ConnectServerSettings connectServerSettings) {
  this.connectServerSettings = connectServerSettings;
 }

 @Override
 public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
  logger.info(msg);
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
}
