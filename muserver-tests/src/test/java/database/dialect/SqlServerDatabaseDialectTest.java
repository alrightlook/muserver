package database.dialect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

public class SqlServerDatabaseDialectTest {
 @Test
 public void testConnection() throws Exception {
  String connectionString = "jdbc:sqlserver://127.0.0.1:1444;instanceName=MSSQLSERVER;databaseName=MuOnline;username=test;password=test";
  Assertions.assertTrue(new SqlServerDatabaseDialect(connectionString).getConnection().isValid(10));
 }

 @Test
 public void testCreatePreparedStatement() throws Exception {
  String connectionString = "jdbc:sqlserver://127.0.0.1:1444;instanceName=MSSQLSERVER;databaseName=MuOnline;username=test;password=test";
  SqlServerDatabaseDialect sqlServerDatabaseDialect = new SqlServerDatabaseDialect(connectionString);
  Connection connection = sqlServerDatabaseDialect.getConnection();
  sqlServerDatabaseDialect.createPreparedStatement(connection, "SELECT 1");
 }
}
