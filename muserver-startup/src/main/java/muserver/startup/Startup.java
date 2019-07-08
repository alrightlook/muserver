package muserver.startup;

import muserver.common.IServer;
import muserver.common.exceptions.ServerException;
import muserver.connectserver.ConnectServer;
import muserver.connectserver.exceptions.ConnectServerException;
import muserver.joinserver.JoinServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;

public class Startup {
    private final static Logger logger = LogManager.getLogger(Startup.class);

    public static void main(String[] args) throws Exception {
        String path = "./startup.json";

        IServer joinServer = new JoinServer(path), connectServer = new ConnectServer(path);

        CompletableFuture<Void> startupFeature = CompletableFuture.runAsync(() -> {
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
        });

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

        startupFeature.get();
    }
}
