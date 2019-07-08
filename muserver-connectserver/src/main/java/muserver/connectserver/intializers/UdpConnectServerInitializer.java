package muserver.connectserver.intializers;

import muserver.connectserver.contexts.ConnectServerContext;
import muserver.connectserver.handlers.UdpConnectServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.DatagramChannel;

public class UdpConnectServerInitializer extends ChannelInitializer<DatagramChannel> {
 private final ConnectServerContext connectServerContext;

 public UdpConnectServerInitializer(ConnectServerContext connectServerContext) {
  this.connectServerContext = connectServerContext;
 }

 @Override
 protected void initChannel(DatagramChannel datagramChannel) {
  datagramChannel.pipeline().addLast(new UdpConnectServerHandler(connectServerContext));
 }
}