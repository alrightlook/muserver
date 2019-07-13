package muserver.startup;

import muserver.common.IServer;
import muserver.common.exceptions.ServerException;
import muserver.connectserver.ConnectServer;
import muserver.joinserver.JoinServer;
import muserver.joinserver.exceptions.JoinServerException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class Startup {
 private final static Logger logger = LogManager.getLogger(Startup.class);

 public static void main(String[] args) throws Exception {
  CommandLineParser parser = new DefaultParser();

  Options cliOptions = new Options();

  cliOptions.addOption("p", "path", true, "path to configuration file");

  CommandLine cl = parser.parse(cliOptions, args);

  if (!cl.hasOption("p")) {
   throw new JoinServerException("--path argument to the startup.json file is required for starting services");
  }

  String path = cl.getOptionValue("p");
  
  IServer joinServer = new JoinServer(), connectServer = new ConnectServer();

  Runtime.getRuntime().addShutdownHook(new Thread(() -> {
   try {
    if (joinServer != null) {
     joinServer.shutdown();
    }

    if (connectServer != null) {
     connectServer.shutdown();
    }
   } catch (Exception e) {
    logger.error(e.getMessage(), e);
   }
  }));

  File startup = new File(path);

  CompletableFuture.runAsync(() -> {
   try {
    connectServer.startup(startup);
   } catch (ServerException e) {
    logger.error(e.getMessage(), e);
   }
  }).thenRun(() -> {
   try {
    joinServer.startup(startup);
   } catch (ServerException e) {
    logger.error(e.getMessage(), e);
   }
  }).exceptionally(throwable -> {
   logger.error(throwable.getMessage(), throwable);
   return null;
  }).get();
 }
}
