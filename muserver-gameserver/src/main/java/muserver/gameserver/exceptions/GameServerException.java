package muserver.gameserver.exceptions;

import muserver.common.exceptions.ServerException;

public class GameServerException  extends ServerException {
 public GameServerException() {
 }

 public GameServerException(String message) {
  super(message);
 }

 public GameServerException(String message, Throwable throwable) {
  super(message, throwable);
 }

 public GameServerException(Throwable throwable) {
  super(throwable);
 }

 public GameServerException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
  super(message, throwable, enableSuppression, writableStackTrace);
 }
}