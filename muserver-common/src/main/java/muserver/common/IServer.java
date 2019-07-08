package muserver.common;

import muserver.common.exceptions.ServerException;

public interface IServer {
    void startup() throws ServerException;
    void shutdown();
}
