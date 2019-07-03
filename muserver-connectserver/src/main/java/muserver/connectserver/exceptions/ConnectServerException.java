package muserver.connectserver.exceptions;

public class ConnectServerException extends Exception {
 public ConnectServerException() {
 }

 public ConnectServerException(String message) {
  super(message);
 }

 public ConnectServerException(String message, Throwable throwable) {
  super(message, throwable);
 }

 public ConnectServerException(Throwable throwable) {
  super(throwable);
 }

 public ConnectServerException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
  super(message, throwable, enableSuppression, writableStackTrace);
 }
}
