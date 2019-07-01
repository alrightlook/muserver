package database.dialect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DatabaseDialect extends ConnectionProvider {
 PreparedStatement createPreparedStatement(Connection connection, String query) throws SQLException;
}
