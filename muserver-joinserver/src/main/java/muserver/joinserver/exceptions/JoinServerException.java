package muserver.joinserver.exceptions;

import muserver.common.exceptions.ServerException;

public class JoinServerException extends ServerException {
    public JoinServerException() {
    }

    public JoinServerException(String message) {
        super(message);
    }

    public JoinServerException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public JoinServerException(Throwable throwable) {
        super(throwable);
    }

    public JoinServerException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }
}
