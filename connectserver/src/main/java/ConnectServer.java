import com.fasterxml.jackson.databind.ObjectMapper;
import configs.CommonConfigs;
import exceptions.ConnectServerException;
import intializers.TcpConnectServerInitializer;
import intializers.UdpConnectServerInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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

public class ConnectServer {
 private final static Logger logger = LogManager.getLogger(ConnectServer.class);
 private final String path;
 private final ObjectMapper mapper = new ObjectMapper();
 private final EventLoopGroup udpEventLoopGroup, tcpParentLoopGroup, tcpChildLoopGroup;

 public ConnectServer(String path) {
  this.path = path;
  udpEventLoopGroup = new NioEventLoopGroup(1);
  tcpChildLoopGroup = new NioEventLoopGroup(1);
  tcpParentLoopGroup = new NioEventLoopGroup(1);
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
  try {
   CommonConfigs commonConfigs = fromJson(mapper, IOUtils.toString(new FileInputStream(path), StandardCharsets.UTF_8), CommonConfigs.class);
   startUdpChannel(commonConfigs);
   startTcpChannel(commonConfigs);
  } catch (Exception e) {
   throw new ConnectServerException(e.getMessage(), e);
  }
 }

 public void shutdown() {
  logger.info("Shutdown connect server");
  udpEventLoopGroup.shutdownGracefully();
  tcpChildLoopGroup.shutdownGracefully();
  tcpParentLoopGroup.shutdownGracefully();
 }

 private void startUdpChannel(CommonConfigs commonConfigs) {
  logger.info(String.format("Start UDP channel on port %d", commonConfigs.connectServerConfigs().udpPort()));
  new Bootstrap().group(udpEventLoopGroup).channel(NioDatagramChannel.class).handler(new UdpConnectServerInitializer(commonConfigs.connectServerConfigs())).bind(commonConfigs.connectServerConfigs().udpPort());
  logger.info("OK..");
 }

 private void startTcpChannel(CommonConfigs commonConfigs) {
  logger.info(String.format("Start TCP channel on port %d", commonConfigs.connectServerConfigs().tcpPort()));
  new ServerBootstrap().group(tcpParentLoopGroup, tcpChildLoopGroup).channel(NioServerSocketChannel.class).childHandler(new TcpConnectServerInitializer(commonConfigs.connectServerConfigs())).bind(commonConfigs.connectServerConfigs().tcpPort());
  logger.info("OK..");
 }

 private <T> T fromJson(ObjectMapper mapper, String content, Class<T> clazzType) throws IOException {
  logger.info(String.format("Reading content from %s", path));
  T t = mapper.readValue(content, clazzType);
  logger.info("OK..");
  return t;
 }
}