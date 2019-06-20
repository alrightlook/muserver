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

import java.util.concurrent.CountDownLatch;

public class ConnectServer {
    private final static int DEFAULT_TCP_PORT = 44405;
    private final static int DEFAULT_UDP_PORT = 55557;
    private final static Logger logger = LogManager.getLogger(ConnectServer.class);

    public static ChannelFuture startTcpServer(int nThreads, int port) throws InterruptedException {
        EventLoopGroup parentLoopGroup = new NioEventLoopGroup(nThreads), childLoopGroup = new NioEventLoopGroup();
        ServerBootstrap tcpBootstrap = new ServerBootstrap();
        return tcpBootstrap.group(parentLoopGroup, childLoopGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO)).childHandler(new TcpConnectServerInitializer()).bind(port);
    }

    public static ChannelFuture startUdpServer(int nThreads, int port) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(nThreads);
        Bootstrap udpBootstrap = new Bootstrap();
        return udpBootstrap.group(eventLoopGroup).channel(NioDatagramChannel.class).handler(new UdpConnectServerInitializer()).bind(port);
    }

    public static void main(String[] args) throws Exception {
        CountDownLatch shutdownLatch = new CountDownLatch(1);

        logger.info(String.format("Starting UDP server on port %d", DEFAULT_UDP_PORT));
        startUdpServer(1, DEFAULT_UDP_PORT);

//        logger.info(String.format("Starting TCP server on port %d", DEFAULT_TCP_PORT));
//        startTcpServer(1, DEFAULT_TCP_PORT);

        shutdownLatch.await();

        logger.info(String.format("Going to shutdown connect server"));
    }
}