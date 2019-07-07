package muserver.joinserver.initializers;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import muserver.joinserver.handlers.TcpJoinServerHandler;

public class TcpJoinServerInitializer extends ChannelInitializer<SocketChannel> {
 @Override
 protected void initChannel(SocketChannel socketChannel) throws Exception {
  socketChannel.pipeline().addLast(new TcpJoinServerHandler());
 }
}
