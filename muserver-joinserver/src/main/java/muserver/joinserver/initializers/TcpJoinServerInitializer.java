package muserver.joinserver.initializers;

import database.dialect.DatabaseDialect;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import muserver.joinserver.contexts.JoinServerContext;
import muserver.joinserver.handlers.TcpJoinServerHandler;

public class TcpJoinServerInitializer extends ChannelInitializer<SocketChannel> {
 private final JoinServerContext joinServerContext;

 public TcpJoinServerInitializer(JoinServerContext joinServerContext) {
  this.joinServerContext = joinServerContext;
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) throws Exception {
  socketChannel.pipeline().addLast(new TcpJoinServerHandler(joinServerContext));
 }
}
