package muserver.joinserver;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import muserver.common.configs.AppenderConfigs;
import muserver.common.configs.CommonConfigs;
import muserver.common.configs.ServerListConfigs;
import muserver.joinserver.exceptions.JoinServerException;
import muserver.joinserver.initializers.TcpJoinServerInitializer;
import muserver.joinserver.initializers.UdpJoinServerInitializer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
  try {
   CommonConfigs commonConfigs = mapper.readValue(IOUtils.toString(new FileInputStream(path), StandardCharsets.UTF_8), CommonConfigs.class);

   updateLoggerConfiguration(commonConfigs);

   Map<Short, ServerListConfigs> serverListConfigsMap = new HashMap<>();

   Map<Short, List<ServerListConfigs>> serverListConfigsGroupingBy = commonConfigs.connectServer().serverListConfigs().stream().collect(Collectors.groupingBy(x -> x.serverCode()));

   for (Map.Entry<Short, List<ServerListConfigs>> entry : serverListConfigsGroupingBy.entrySet()) {
    serverListConfigsMap.put(entry.getKey(), entry.getValue().get(0));
   }

   logger.info(String.format("Start UDP channel on port %d", commonConfigs.connectServer().udpPort()));
   UdpJoinServerInitializer udpJoinServerInitializer = new UdpJoinServerInitializer();
   new Bootstrap().group(udpEventLoopGroup).channel(NioDatagramChannel.class).handler(udpJoinServerInitializer).bind(commonConfigs.connectServer().udpPort());

   logger.info(String.format("Start TCP channel on port %d", commonConfigs.connectServer().tcpPort()));
   TcpJoinServerInitializer tcpJoinServerInitializer = new TcpJoinServerInitializer();
   new ServerBootstrap().group(tcpParentLoopGroup, tcpChildLoopGroup).channel(NioServerSocketChannel.class).childHandler(tcpJoinServerInitializer).bind(commonConfigs.connectServer().tcpPort());
  } catch (Exception e) {
   throw new JoinServerException(e.getMessage(), e);
  }
 }

 public void shutdown() {
  logger.info("Shutdown connect server");
  udpEventLoopGroup.shutdownGracefully();
  tcpChildLoopGroup.shutdownGracefully();
  tcpParentLoopGroup.shutdownGracefully();
 }

 private void updateLoggerConfiguration(CommonConfigs commonConfigs) throws JoinServerException {
  ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

  Level level = Level.getLevel(commonConfigs.logging().level().loggingLevel());

  builder.setStatusLevel(level);
  builder.setConfigurationName("muserver.cs.ConnectServer");

  List<AppenderRefComponentBuilder> appenderRefComponentBuilderList = new ArrayList<>();

  for (AppenderConfigs appender : commonConfigs.logging().appenders()) {
   switch (appender.type()) {
    case FILE: {
     AppenderComponentBuilder appenderComponentBuilder = builder.newAppender(appender.name(), appender.type().getName());

     appenderComponentBuilder.addAttribute("fileName", appender.fileName());

     appenderComponentBuilder.add(builder.newLayout("PatternLayout").addAttribute("pattern", appender.format()));

     builder.add(appenderComponentBuilder);

     appenderRefComponentBuilderList.add(builder.newAppenderRef(appender.name()));
    }
    break;

    case CONSOLE: {
     AppenderComponentBuilder appenderComponentBuilder = builder.newAppender(appender.name(), appender.type().getName());

     appenderComponentBuilder.add(builder.newLayout("PatternLayout").addAttribute("pattern", appender.format()));

     builder.add(appenderComponentBuilder);

     appenderRefComponentBuilderList.add(builder.newAppenderRef(appender.name()));
    }
    break;

    case ROLLING_FILE: {
     AppenderComponentBuilder appenderComponentBuilder = builder.newAppender(appender.name(), appender.type().getName());

     appenderComponentBuilder.addAttribute("fileName", appender.fileName());
     appenderComponentBuilder.addAttribute("filePattern", appender.filePattern());

     appenderComponentBuilder.add(builder.newLayout("PatternLayout").addAttribute("pattern", appender.format()));

     ComponentBuilder triggeringPolicy = null;

     if (appender.sizeBasedTriggeringPolicy() != null) {
      triggeringPolicy = builder.newComponent("Policies").addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", appender.sizeBasedTriggeringPolicy().size()));
      appenderComponentBuilder.addComponent(triggeringPolicy);
     }

     if (triggeringPolicy == null) {
      throw new JoinServerException("Triggering policy is null or not valid");
     }

     builder.add(appenderComponentBuilder);

     appenderRefComponentBuilderList.add(builder.newAppenderRef(appender.name()));
    }
    break;

    default:
     throw new UnsupportedOperationException(String.format("Unsupported appender type: %s", appender.type().getName()));
   }
  }

  RootLoggerComponentBuilder rootLogger = builder.newRootLogger(level);

  for (AppenderRefComponentBuilder appenderRefComponentBuilder : appenderRefComponentBuilderList) {
   rootLogger.add(appenderRefComponentBuilder);
  }

  builder.add(rootLogger);

  BuiltConfiguration configuration = builder.build();

  LoggerContext ctx = Configurator.initialize(configuration);

  ctx.updateLoggers(configuration);

  ctx.start(configuration);
 }
}
