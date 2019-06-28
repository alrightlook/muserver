import configs.ConnectServerConfigs;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class TcpConnectServerInitializer extends ChannelInitializer<SocketChannel> {
 private final ConnectServerConfigs ConnectServerConfigs;

 public TcpConnectServerInitializer(ConnectServerConfigs connectServerConfigs) {
  this.ConnectServerConfigs = connectServerConfigs;
 }

 @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
//        channelPipeline.addLast(new LoggingHandler(LogLevel.INFO));
        channelPipeline.addLast(new TcpConnectServerHandler(ConnectServerConfigs));
    }
}
