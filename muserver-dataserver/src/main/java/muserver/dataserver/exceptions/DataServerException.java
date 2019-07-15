package muserver.dataserver.exceptions;

import muserver.common.exceptions.ServerException;

public class DataServerException extends ServerException {
 public DataServerException() {
 }

 public DataServerException(String message) {
  super(message);
 }

 public DataServerException(String message, Throwable throwable) {
  super(message, throwable);
 }

 public DataServerException(Throwable throwable) {
  super(throwable);
 }

 public DataServerException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
  super(message, throwable, enableSuppression, writableStackTrace);
 }
}
