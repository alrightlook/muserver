import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectServer {
    private final static Logger logger = LogManager.getLogger(ConnectServer.class);

    public static ChannelFuture startTcpListener() throws Exception {
        EventLoopGroup parentLoopGroup = new NioEventLoopGroup(1), childLoopGroup = new NioEventLoopGroup();

        ServerBootstrap tcpBootstrap = new ServerBootstrap();

        tcpBootstrap
                .group(parentLoopGroup, childLoopGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ConnectServerInitializer());

        return tcpBootstrap.bind(44405);
    }
}