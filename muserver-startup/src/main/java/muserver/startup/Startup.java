package muserver.startup;

import muserver.common.IServer;
import muserver.common.exceptions.ServerException;
import muserver.connectserver.ConnectServer;
import muserver.connectserver.exceptions.ConnectServerException;
import muserver.joinserver.JoinServer;
import muserver.joinserver.exceptions.JoinServerException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;

public class Startup {
 private final static Logger logger = LogManager.getLogger(Startup.class);

 public static void main(String[] args) throws Exception {
  CommandLineParser parser = new DefaultParser();

  Options cliOptions = new Options();

  cliOptions.addOption("p", "path", true, "path to configuration file");

  CommandLine cl = parser.parse(cliOptions, args);

  if (!cl.hasOption("p")) {
   throw new JoinServerException("Arg --path is required");
  }

  String path = cl.getOptionValue("p");
  
  IServer joinServer = new JoinServer(path), connectServer = new ConnectServer(path);

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

  CompletableFuture.runAsync(() -> {
   try {
    connectServer.startup();
   } catch (ServerException e) {
    logger.error(e.getMessage(), e);
   }
  }).thenRun(() -> {
   try {
    joinServer.startup();
   } catch (ServerException e) {
    logger.error(e.getMessage(), e);
   }
  }).exceptionally(throwable -> {
   logger.error(throwable.getMessage(), throwable);
   return null;
  }).get();
 }
}
