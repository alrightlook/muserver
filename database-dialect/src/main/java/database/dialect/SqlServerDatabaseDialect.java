package database.dialect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SqlServerDatabaseDialect implements DatabaseDialect {
 private final static Logger logger = LogManager.getLogger(SqlServerDatabaseDialect.class);
 private final String connectionString;
 private final Queue<Connection> connections = new ConcurrentLinkedQueue<>();

 public SqlServerDatabaseDialect(String connectionString) {
  this.connectionString = connectionString;
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
  Connection connection = DriverManager.getConnection(connectionString, properties);
  connections.add(connection);
  return connection;
 }
}
