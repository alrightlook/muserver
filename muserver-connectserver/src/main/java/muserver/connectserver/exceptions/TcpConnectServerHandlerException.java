package muserver.connectserver.exceptions;

public class TcpConnectServerHandlerException extends Exception {
 public TcpConnectServerHandlerException() {
 }

 public TcpConnectServerHandlerException(String message) {
  super(message);
 }

 public TcpConnectServerHandlerException(String message, Throwable throwable) {
  super(message, throwable);
 }

 public TcpConnectServerHandlerException(Throwable throwable) {
  super(throwable);
 }

 public TcpConnectServerHandlerException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
  super(message, throwable, enableSuppression, writableStackTrace);
 }
}