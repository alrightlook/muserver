package intializers;

import configs.ServerListConfigs;
import handlers.TcpConnectServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

public class TcpConnectServerInitializer extends ChannelInitializer<SocketChannel> {
 private final TcpConnectServerHandler tcpConnectServerHandler;

 public TcpConnectServerInitializer(Map<Short, ServerListConfigs> serverListConfigsMap) {
  tcpConnectServerHandler = new TcpConnectServerHandler(serverListConfigsMap);
 }

 public TcpConnectServerHandler getTcpConnectServerHandler() {
  return tcpConnectServerHandler;
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) throws Exception {
  socketChannel.pipeline().addLast(tcpConnectServerHandler);
 }
}
