package database.dialect;

import sun.security.util.Password;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SqlServerDatabaseDialect implements DatabaseDialect {
 private final Queue<Connection> connections = new ConcurrentLinkedQueue<>();
 private final SqlServerConnection sqlServerConnection;

 public SqlServerDatabaseDialect(SqlServerConnection sqlServerConnection) {
  this.sqlServerConnection = sqlServerConnection;
 }

 @Override
 public void close() {

 }

 @Override
 public boolean testConnection(Connection connection, int timeout) throws SQLException {
  return false;
 }

 @Override
 public Connection getConnection() throws SQLException {
  Properties properties = new Properties();

  if (sqlServerConnection.username() != null) {
   properties.setProperty("user", sqlServerConnection.username());
  }

  if (sqlServerConnection.password() != null) {
   properties.setProperty("password", sqlServerConnection.password());
  }

  String sqlServerConnectionString = "jdbc:jtds:sqlserver://127.0.0.1;instance=MSSQLSERVER;DatabaseName=MuOnline";

  Connection connection = DriverManager.getConnection(sqlServerConnectionString, properties);

  connections.add(connection);

  return connection;
 }

 @Override
 public PreparedStatement createPreparedStatement(Connection connection, String query) throws SQLException {
  return null;
 }
}
