package muserver.js;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import muserver.js.exceptions.JoinServerException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JoinServer {
    private final static Logger logger = LogManager.getLogger(JoinServer.class);
    private final String path;
    private final ObjectMapper mapper = new ObjectMapper();
    private final EventLoopGroup udpEventLoopGroup, tcpParentLoopGroup, tcpChildLoopGroup;

    public JoinServer(String path) {
        this.path = path;
        udpEventLoopGroup = new NioEventLoopGroup(1);
        tcpChildLoopGroup = new NioEventLoopGroup(1);
        tcpParentLoopGroup = new NioEventLoopGroup(1);
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    }

    public static void main(String[] args) throws Exception {
        CommandLineParser parser = new DefaultParser();

        Options cliOptions = new Options();

        cliOptions.addOption("p", "path", true, "path to configuration file");

        CommandLine cl = parser.parse(cliOptions, args);

        if (!cl.hasOption("p")) {
            throw new JoinServerException("Arg --path is required");
        }

        String path = cl.getOptionValue("p");

        JoinServer joinServer = new JoinServer(path);

        joinServer.startup();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                joinServer.shutdown();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }));
    }

    public void startup() throws JoinServerException {
        //            CommonConfigs commonConfigs = mapper.readValue(IOUtils.toString(new FileInputStream(path), StandardCharsets.UTF_8), CommonConfigs.class);
//
//            updateLoggerConfiguration(commonConfigs);
//
//            Map<Short, ServerListConfigs> serverListConfigsMap = new HashMap<>();
//            Map<Short, List<ServerListConfigs>> serverListConfigsGroupingBy = commonConfigs.connectServer().serverListConfigs().stream().collect(Collectors.groupingBy(x -> x.serverCode()));
//
//            for (Map.Entry<Short, List<ServerListConfigs>> entry : serverListConfigsGroupingBy.entrySet()) {
//                serverListConfigsMap.put(entry.getKey(), entry.getValue().get(0));
//            }
//
//            logger.info(String.format("Start UDP channel on port %d", commonConfigs.connectServer().udpPort()));
//            UdpConnectServerInitializer udpConnectServerInitializer = new UdpConnectServerInitializer(serverListConfigsMap);
//            new Bootstrap().group(udpEventLoopGroup).channel(NioDatagramChannel.class).handler(udpConnectServerInitializer).bind(commonConfigs.connectServer().udpPort());
//
//            logger.info(String.format("Start TCP channel on port %d", commonConfigs.connectServer().tcpPort()));
//            TcpConnectServerInitializer tcpConnectServerInitializer = new TcpConnectServerInitializer(serverListConfigsMap);
//            new ServerBootstrap().group(tcpParentLoopGroup, tcpChildLoopGroup).channel(NioServerSocketChannel.class).childHandler(tcpConnectServerInitializer).bind(commonConfigs.connectServer().tcpPort());

    }

    public void shutdown() {
        logger.info("Shutdown connect server");
        udpEventLoopGroup.shutdownGracefully();
        tcpChildLoopGroup.shutdownGracefully();
        tcpParentLoopGroup.shutdownGracefully();
    }
}
