package muserver.dataserver.initializers;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import muserver.dataserver.contexts.DataServerContext;
import muserver.dataserver.handlers.TcpDataServerHandler;

public class TcpDataServerInitializer extends ChannelInitializer<SocketChannel> {
 private final DataServerContext dataServerContext;

 public TcpDataServerInitializer(DataServerContext dataServerContext) {
  this.dataServerContext = dataServerContext;
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) throws Exception {
  socketChannel.pipeline().addLast(new TcpDataServerHandler(dataServerContext));
 }
}
