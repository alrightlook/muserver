package intializers;

import configs.ConnectServerConfigs;
import configs.AbstractConfigs;
import handlers.UdpConnectServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.DatagramChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UdpConnectServerInitializer extends ChannelInitializer<DatagramChannel> {
 private final static Logger logger = LogManager.getLogger(UdpConnectServerInitializer.class);

 private final AbstractConfigs abstractConfigs;

 public UdpConnectServerInitializer(AbstractConfigs abstractConfigs) {
  this.abstractConfigs = abstractConfigs;
 }

 @Override
 protected void initChannel(DatagramChannel datagramChannel) throws Exception {
  ChannelPipeline channelPipeline = datagramChannel.pipeline();
  channelPipeline.addLast(new UdpConnectServerHandler((ConnectServerConfigs) abstractConfigs));
 }
}
