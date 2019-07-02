package database.dialect;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnectionProvider extends AutoCloseable {
 /**
  * Close this connection provider.
  */
 void close();

 /**
  * Determine if the specified connection is valid.
  *
  * @param connection the database connection; may not be null
  * @param timeout    The time in seconds to wait for the database operation used to validate the connection to complete.  If the timeout period expires before the operation completes, this method returns false.  A value of 0 indicates a timeout is not applied to the database operation.
  * @return true if it is valid, or false otherwise
  * @throws SQLException if there is an error with the database connection
  */
 boolean testConnection(Connection connection, int timeout) throws SQLException;

 /**
  * Create a connection.
  *
  * @return the connection; never null
  * @throws SQLException if there is a problem getting the connection
  */
 Connection getConnection() throws SQLException, ClassNotFoundException;

 /**
  * Get the publicly viewable identifier for this connection provider and / or the database.
  * The resulting value should not contain any secrets or passwords.
  *
  * @return the identifier; never null
  */
 default String identifier() {
  return toString();
 }
}