package muserver.connectserver.intializers;

import muserver.connectserver.contexts.ConnectServerContext;
import muserver.connectserver.handlers.TcpConnectServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class TcpConnectServerInitializer extends ChannelInitializer<SocketChannel> {
 private final ConnectServerContext ctx;

 public TcpConnectServerInitializer(ConnectServerContext ctx) {
  this.ctx = ctx ;
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) {
  socketChannel.pipeline().addLast(new TcpConnectServerHandler(ctx));
 }
}
