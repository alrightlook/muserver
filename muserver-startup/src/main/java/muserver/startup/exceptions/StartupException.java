package muserver.startup.exceptions;

import muserver.common.exceptions.ServerException;

public class StartupException extends ServerException {
 public StartupException() {
 }

 public StartupException(String message) {
  super(message);
 }

 public StartupException(String message, Throwable throwable) {
  super(message, throwable);
 }

 public StartupException(Throwable throwable) {
  super(throwable);
 }

 public StartupException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
  super(message, throwable, enableSuppression, writableStackTrace);
 }
}
