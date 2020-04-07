import java.sql.*;

public class JDBCUtils {

  private static final String URL = "jdbc:mysql://localhost:3306/parking_sys";
  private static final String USER = "root";
  private static final String PASSWORD = "--------";
  public Connection connection;

  public JDBCUtils() {
    try {
      this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public PreparedStatement prepare(String sql) {
    try {
      return connection.prepareStatement(sql);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void executeStatement(String sql) {
    Statement statement = null;
    try {
      statement = connection.createStatement();
      statement.executeUpdate(sql);
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ResultSet executeSelectStatement(String sql) {
    ResultSet resultSet = null;
    try {
      Statement statement = connection.createStatement();
      resultSet = statement.executeQuery(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return resultSet;
  }

  public void close() {
    try {
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
