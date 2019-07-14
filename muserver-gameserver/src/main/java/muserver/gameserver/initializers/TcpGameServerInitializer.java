package muserver.gameserver.initializers;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import muserver.gameserver.contexts.GameServerContext;
import muserver.gameserver.handlers.TcpGameServerHandler;

public class TcpGameServerInitializer extends ChannelInitializer<SocketChannel> {
 private final GameServerContext gameServerContext;

 public TcpGameServerInitializer(GameServerContext gameServerContext) {
  this.gameServerContext = gameServerContext;
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) throws Exception {
  socketChannel.pipeline().addLast(new TcpGameServerHandler(gameServerContext));
 }
}
