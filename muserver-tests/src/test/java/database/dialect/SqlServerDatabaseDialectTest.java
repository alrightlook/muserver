package database.dialect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SqlServerDatabaseDialectTest {
 @Test
 public void testConnection() throws Exception {
  String hostName = "127.0.0.1";
  Integer portNumber = 1433;
  String instanceName = "MSSQLSERVER";
  String databaseName = "MuOnline";
  String username = "testUsername";
  String password = "testPassword";
  Assertions.assertTrue(new SqlServerDatabaseDialect(hostName, portNumber, instanceName, databaseName, username, password).getConnection().isValid(10));
 }
}
