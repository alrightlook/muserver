import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.DatagramChannel;
import configs.ConnectServerConfigs;

public class UdpConnectServerInitializer extends ChannelInitializer<DatagramChannel> {
 private final ConnectServerConfigs ConnectServerConfigs;

 public UdpConnectServerInitializer(ConnectServerConfigs connectServerConfigs) {
  this.ConnectServerConfigs = connectServerConfigs;
 }

 @Override
 protected void initChannel(DatagramChannel datagramChannel) throws Exception {
  ChannelPipeline channelPipeline = datagramChannel.pipeline();
//  channelPipeline.addLast(new LoggingHandler(LogLevel.INFO));
  channelPipeline.addLast(new UdpConnectServerHandler(ConnectServerConfigs));
 }
}
