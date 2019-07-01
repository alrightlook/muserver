package database.dialect;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider extends AutoCloseable {
 void close();

 boolean testConnection(Connection connection, int timeout) throws SQLException;

 Connection getConnection() throws SQLException;
}