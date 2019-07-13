package muserver.joinserver.contexts;

import database.dialect.DatabaseDialect;

import java.sql.Connection;

public class JoinServerContext {
 private final DatabaseDialect dialect;

 public JoinServerContext(DatabaseDialect dialect) {
  this.dialect = dialect;
 }

 public DatabaseDialect dialect() {
  return dialect;
 }
}
