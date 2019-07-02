package database.dialect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SqlServerDatabaseDialect implements DatabaseDialect {
 private final static Logger logger = LogManager.getLogger(SqlServerDatabaseDialect.class);
 private final String hostname, instanceName, databaseName, username, password;
 private final Integer portNumber;
 private final Queue<Connection> connections = new ConcurrentLinkedQueue<>();

 public SqlServerDatabaseDialect(String hostname, Integer portNumber, String instanceName, String databaseName, String username, String password) {
  this.hostname = hostname;
  this.portNumber = portNumber;
  this.instanceName = instanceName;
  this.databaseName = databaseName;
  this.username = username;
  this.password = password;
 }

 @Override
 public PreparedStatement createPreparedStatement(Connection connection, String query) throws SQLException {
  logger.trace("Creating a PreparedStatement '{}'", query);
  PreparedStatement statement = connection.prepareStatement(query);
  return statement;
 }

 @Override
 public void close() {
  Connection connection;
  while ((connection = connections.poll()) != null) {
   try {
    connection.close();
   } catch (Throwable e) {
    logger.warn("Error while closing connection", e);
   }
  }
 }

 @Override
 public boolean testConnection(Connection connection, int timeout) throws SQLException {
  return connection.isValid(timeout);
 }

 @Override
 public Connection getConnection() throws SQLException {
  Properties properties = new Properties();

  if (username != null) {
   properties.setProperty("username", username);
  }

  if (password != null) {
   properties.setProperty("password", password);
  }

  String connectionString = String.format("jdbc:sqlserver://%s:%d;instanceName=%s;databaseName=%s;username=%s;password=%s", hostname, portNumber, instanceName, databaseName, username, password);

  Connection connection = DriverManager.getConnection(connectionString, properties);

  connections.add(connection);

  return connection;
 }
}
