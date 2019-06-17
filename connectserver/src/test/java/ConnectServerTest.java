import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ConnectServerTest {
    private static ChannelFuture connectServer;

    private final static Logger logger = LogManager.getLogger(ConnectServerTest.class);

    @BeforeAll
    public static void startup() throws Exception {
        connectServer = ConnectServer.start();
        Thread.sleep(5000);
    }

    @AfterAll
    public static void shutdown() throws Exception {
        connectServer.sync().channel().closeFuture().sync();
    }

    @Test
    public void testConnectServer() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap tcpBootstrap = new Bootstrap();
            tcpBootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline channelPipeline = ch.pipeline();
                            channelPipeline.addLast(
                                    new ObjectEncoder(),
                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                    new ConnectServerClientHandler());
                        }
                    });

            tcpBootstrap.connect("192.168.88.30", 44405).sync().channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    static class ConnectServerClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            logger.info(msg);
            ctx.write(msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            String logString = "Going to flush client message";
            logger.info(logString);
            ctx.flush();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            String logString = "Hello, from client";
            logger.info(logString);
            ctx.writeAndFlush(logString);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.error(cause.getMessage(), cause);
            ctx.close();
        }
    }
}
