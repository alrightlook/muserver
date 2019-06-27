import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import settings.ConnectServerSettings;

public class TcpConnectServerInitializer extends ChannelInitializer<SocketChannel> {
 private final ConnectServerSettings connectServerSettings;

 public TcpConnectServerInitializer(ConnectServerSettings connectServerSettings) {
  this.connectServerSettings = connectServerSettings;
 }

 @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
//        channelPipeline.addLast(new LoggingHandler(LogLevel.INFO));
        channelPipeline.addLast(new TcpConnectServerHandler(connectServerSettings));
    }
}
