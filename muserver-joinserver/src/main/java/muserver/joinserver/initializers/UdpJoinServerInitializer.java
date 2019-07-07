package muserver.joinserver.initializers;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.DatagramChannel;
import muserver.joinserver.handlers.UdpJoinServerHandler;

public class UdpJoinServerInitializer extends ChannelInitializer<DatagramChannel> {
 @Override
 protected void initChannel(DatagramChannel datagramChannel) throws Exception {
  datagramChannel.pipeline().addLast(new UdpJoinServerHandler());
 }
}
