package muserver.connectserver.intializers;

import muserver.common.configs.ServerListConfigs;
import muserver.connectserver.handlers.TcpConnectServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

public class TcpConnectServerInitializer extends ChannelInitializer<SocketChannel> {
 private final Map<Short, ServerListConfigs> serverListConfigsMap;

 public TcpConnectServerInitializer(Map<Short, ServerListConfigs> serverListConfigsMap) {
  this.serverListConfigsMap = serverListConfigsMap ;
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) throws Exception {
  socketChannel.pipeline().addLast(new TcpConnectServerHandler(serverListConfigsMap));
 }
}
