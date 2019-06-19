import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.BitSet;
import java.util.concurrent.CountDownLatch;

public class ConnectServer {
    private final static int DEFAULT_TCP_PORT = 44405;
    private final static int DEFAULT_UDP_PORT = 55557;
    private final static Logger logger = LogManager.getLogger(ConnectServer.class);

    private final BitSet keyboard = new BitSet();

    public static ChannelFuture startTcpListener() {
        EventLoopGroup parentLoopGroup = new NioEventLoopGroup(1), childLoopGroup = new NioEventLoopGroup();

        ServerBootstrap tcpBootstrap = new ServerBootstrap();

        return tcpBootstrap.group(parentLoopGroup, childLoopGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO)).childHandler(new TcpConnectServerInitializer()).bind(DEFAULT_TCP_PORT);
    }

    public static ChannelFuture startUdpListener() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        Bootstrap udpBootstrap = new Bootstrap();
        return udpBootstrap.group(eventLoopGroup).channel(NioDatagramChannel.class).handler(new UdpConnectServerInitializer()).bind(DEFAULT_UDP_PORT);
    }

    public static void main(String[] args) throws Exception {
        logger.info(String.format("Start up UDP listener on port %d", DEFAULT_UDP_PORT));
        startUdpListener().sync().channel().closeFuture().await();
        logger.info(String.format("Going to shutdown connect server"));
    }
}