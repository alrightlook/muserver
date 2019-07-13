package muserver.common;

import muserver.common.exceptions.ServerException;

import java.io.File;

public interface IServer {
    void startup(File file) throws ServerException;
    void shutdown() throws ServerException;
}
