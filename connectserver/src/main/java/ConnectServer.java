import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.ConnectServerException;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import settings.ConnectServerSettings;

import java.io.FileInputStream;

public class ConnectServer {
    private final String path;
    private final ObjectMapper json = new ObjectMapper();
    private final static Logger logger = LogManager.getLogger(ConnectServer.class);
    private final EventLoopGroup udpEventLoopGroup, tcpParentLoopGroup, tcpChildLoopGroup;

    public ConnectServer(String path) {
        this.path = path;
        udpEventLoopGroup = new NioEventLoopGroup(1);
        tcpChildLoopGroup = new NioEventLoopGroup(1);
        tcpParentLoopGroup = new NioEventLoopGroup(1);
    }

    public void startup(int tcpPort, int udpPort) throws ConnectServerException {
        try {
            FileInputStream fileInputStream = new FileInputStream(path);

            String jsonString = IOUtils.toString(fileInputStream, "UTF-8");

            ConnectServerSettings connectServerSettings = json.readValue(jsonString, ConnectServerSettings.class);

            new Bootstrap().group(udpEventLoopGroup)
                .channel(NioDatagramChannel.class)
                .handler(new UdpConnectServerInitializer(connectServerSettings))
                .bind(udpPort);

            new ServerBootstrap().group(tcpParentLoopGroup, tcpChildLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new TcpConnectServerInitializer(connectServerSettings))
                .bind(tcpPort);
        } catch (Exception e) {
            throw new ConnectServerException(e.getMessage(), e);
        }
    }

    public void shutdown() {
        udpEventLoopGroup.shutdownGracefully();
        tcpChildLoopGroup.shutdownGracefully();
        tcpParentLoopGroup.shutdownGracefully();
    }
}