import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.DatagramChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class UdpConnectServerInitializer extends ChannelInitializer<DatagramChannel> {
 @Override
 protected void initChannel(DatagramChannel datagramChannel) throws Exception {
  ChannelPipeline channelPipeline = datagramChannel.pipeline();
  channelPipeline.addLast(new ObjectEncoder());
  channelPipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
  channelPipeline.addLast(new LoggingHandler(LogLevel.INFO));
  channelPipeline.addLast(new UdpConnectServerHandler());
 }
}
