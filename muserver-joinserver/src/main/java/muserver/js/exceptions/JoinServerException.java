package muserver.js.exceptions;

public class JoinServerException extends Exception {
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
