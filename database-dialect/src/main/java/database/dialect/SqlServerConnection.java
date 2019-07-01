package database.dialect;

public class SqlServerConnection {
 private final String username;
 private final String password;

 public SqlServerConnection(String username, String password) {
  this.username = username;
  this.password = password;
 }

 public String username() {
  return username;
 }

 public String password() {
  return password;
 }
}
