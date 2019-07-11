package muserver.connectserver;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import muserver.common.IServer;
import muserver.common.configs.CommonConfigs;
import muserver.common.configs.ServerConfigs;
import muserver.common.logging.LoggingLevel;
import muserver.common.types.AppenderType;
import muserver.connectserver.contexts.ConnectServerContext;
import muserver.connectserver.exceptions.ConnectServerException;
import muserver.connectserver.intializers.TcpConnectServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import muserver.common.utils.LoggerUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConnectServer implements IServer {
 private final static Logger logger = LogManager.getLogger(ConnectServer.class);
 private final String path;
 private final ObjectMapper mapper = new ObjectMapper();
 private final EventLoopGroup udpEventLoopGroup, tcpParentLoopGroup, tcpChildLoopGroup;

 public ConnectServer(String path) {
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
   throw new ConnectServerException("Arg --path is required");
  }

  String path = cl.getOptionValue("p");

  ConnectServer connectServer = new ConnectServer(path);

  connectServer.startup();

  Runtime.getRuntime().addShutdownHook(new Thread(() -> {
   try {
    connectServer.shutdown();
   } catch (Exception e) {
    logger.error(e.getMessage(), e);
   }
  }));
 }

 public void startup() throws ConnectServerException {
  CommonConfigs commonConfigs;
  try {
   commonConfigs = mapper.readValue(IOUtils.toString(new FileInputStream(path), StandardCharsets.UTF_8), CommonConfigs.class);
  } catch (IOException e) {
   throw new ConnectServerException(e.getMessage(), e);
  }

  LoggerUtils.updateLoggerConfiguration(ConnectServer.class.getCanonicalName(), AppenderType.CONSOLE, "%d{DEFAULT} [%t] %-5level %logger{36} - %msg%n", LoggingLevel.INFO);

  Map<Short, ServerConfigs> serversConfigsMap = new HashMap<>();

  for (Map.Entry<Short, List<ServerConfigs>> entry : commonConfigs.connectServer().serversConfigs().stream().collect(Collectors.groupingBy(x -> x.id())).entrySet()) {
   serversConfigsMap.put(entry.getKey(), entry.getValue().get(0));
  }

  ConnectServerContext connectServerContext = new ConnectServerContext(serversConfigsMap);

  TcpConnectServerInitializer tcpConnectServerInitializer = new TcpConnectServerInitializer(connectServerContext);

  logger.info(String.format("Start connect server tcp channel on port %d", commonConfigs.connectServer().tcpPort()));
  new ServerBootstrap().group(tcpParentLoopGroup, tcpChildLoopGroup).channel(NioServerSocketChannel.class).childHandler(tcpConnectServerInitializer).bind(commonConfigs.connectServer().tcpPort());
 }

 public void shutdown() {
  logger.info("Shutdown connect server gracefully");
  udpEventLoopGroup.shutdownGracefully();
  tcpChildLoopGroup.shutdownGracefully();
  tcpParentLoopGroup.shutdownGracefully();
 }
}