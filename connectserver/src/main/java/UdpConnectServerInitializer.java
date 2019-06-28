import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.DatagramChannel;
import settings.ConnectServerSettings;

public class UdpConnectServerInitializer extends ChannelInitializer<DatagramChannel> {
 private final ConnectServerSettings connectServerSettings;

 public UdpConnectServerInitializer(ConnectServerSettings connectServerSettings) {
  this.connectServerSettings = connectServerSettings;
 }

 @Override
 protected void initChannel(DatagramChannel datagramChannel) throws Exception {
  ChannelPipeline channelPipeline = datagramChannel.pipeline();
//  channelPipeline.addLast(new LoggingHandler(LogLevel.INFO));
  channelPipeline.addLast(new UdpConnectServerHandler(connectServerSettings));
 }
}
