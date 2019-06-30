package intializers;

import configs.ConnectServerConfigs;
import configs.AbstractConfigs;
import handlers.TcpConnectServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TcpConnectServerInitializer extends ChannelInitializer<SocketChannel> {
 private final static Logger logger = LogManager.getLogger(TcpConnectServerInitializer.class);

 private final AbstractConfigs abstractConfigs;

 public TcpConnectServerInitializer(AbstractConfigs abstractConfigs) {
  this.abstractConfigs = abstractConfigs;
 }

 @Override
 protected void initChannel(SocketChannel socketChannel) throws Exception {
  ChannelPipeline channelPipeline = socketChannel.pipeline();
  channelPipeline.addLast(new TcpConnectServerHandler((ConnectServerConfigs) abstractConfigs));
 }
}
