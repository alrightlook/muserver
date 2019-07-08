package muserver.joinserver;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import muserver.common.IServer;
import muserver.common.configs.CommonConfigs;
import muserver.common.configs.ServerListConfigs;
import muserver.common.logging.LoggingLevel;
import muserver.common.types.AppenderType;
import muserver.joinserver.exceptions.JoinServerException;
import muserver.joinserver.initializers.TcpJoinServerInitializer;
import muserver.common.utils.LoggerUtils;
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

public class JoinServer implements IServer {
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
  try {
   CommonConfigs commonConfigs = mapper.readValue(IOUtils.toString(new FileInputStream(path), StandardCharsets.UTF_8), CommonConfigs.class);

   LoggerUtils.updateLoggerConfiguration(JoinServer.class.getCanonicalName(), AppenderType.CONSOLE, "%d{DEFAULT} [%t] %-5level %logger{36} - %msg%n", LoggingLevel.INFO);

   Map<Short, ServerListConfigs> serverListConfigsMap = new HashMap<>();

   Map<Short, List<ServerListConfigs>> serverListConfigsGroupingBy = commonConfigs.connectServer().serverListConfigs().stream().collect(Collectors.groupingBy(x -> x.serverCode()));

   for (Map.Entry<Short, List<ServerListConfigs>> entry : serverListConfigsGroupingBy.entrySet()) {
    serverListConfigsMap.put(entry.getKey(), entry.getValue().get(0));
   }

   logger.info(String.format("Start join server TCP channel on port %d", commonConfigs.connectServer().tcpPort()));
   TcpJoinServerInitializer tcpJoinServerInitializer = new TcpJoinServerInitializer();
   new ServerBootstrap().group(tcpParentLoopGroup, tcpChildLoopGroup).channel(NioServerSocketChannel.class).childHandler(tcpJoinServerInitializer).bind(commonConfigs.joinServer().tcpPort());
  } catch (Exception e) {
   throw new JoinServerException(e.getMessage(), e);
  }
 }

 public void shutdown() {
  logger.info("Shutdown join server");
  udpEventLoopGroup.shutdownGracefully();
  tcpChildLoopGroup.shutdownGracefully();
  tcpParentLoopGroup.shutdownGracefully();
 }
}
