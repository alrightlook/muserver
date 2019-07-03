package muserver.connectserver.exceptions;

public class UdpConnectServerHandlerException extends Exception {
 public UdpConnectServerHandlerException() {
 }

 public UdpConnectServerHandlerException(String message) {
  super(message);
 }

 public UdpConnectServerHandlerException(String message, Throwable throwable) {
  super(message, throwable);
 }

 public UdpConnectServerHandlerException(Throwable throwable) {
  super(throwable);
 }

 public UdpConnectServerHandlerException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
  super(message, throwable, enableSuppression, writableStackTrace);
 }
}
