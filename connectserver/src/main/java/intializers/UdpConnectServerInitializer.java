package intializers;

import configs.AbstractConfigs;
import configs.ConnectServerConfigs;
import configs.ServerListConfigs;
import handlers.UdpConnectServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.DatagramChannel;

import java.util.Map;

public class UdpConnectServerInitializer extends ChannelInitializer<DatagramChannel> {
 private final Map<Short, ServerListConfigs> serverListConfigsMap;

 public UdpConnectServerInitializer(Map<Short, ServerListConfigs> serverListConfigsMap) {
  this.serverListConfigsMap = serverListConfigsMap;
 }

 @Override
 protected void initChannel(DatagramChannel datagramChannel) throws Exception {
  datagramChannel.pipeline().addLast(new UdpConnectServerHandler(serverListConfigsMap));
 }
}
