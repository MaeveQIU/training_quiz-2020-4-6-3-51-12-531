import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParkingSystemInitializer {

  private JDBCUtils utils = new JDBCUtils();

  public void initParkingSystem(String initInfo) throws SQLException {
    initParkingDatabase();

    String[] initInfoArray = initInfo.split(",");
    for (String infoDetail : initInfoArray) {
      String[] info = infoDetail.split(":");
      insertIntoParkingDatabase(info[0], Integer.parseInt(info[1]));
      createParkingTable(info[0], Integer.parseInt(info[1]));
    }
  }

  public void initParkingDatabase() {
    utils.executeStatement("DROP DATABASE parking_sys;");
    utils.executeStatement("CREATE DATABASE parking_sys;");
    utils.executeStatement("USE parking_sys;");
    String sql = "CREATE TABLE IF NOT EXISTS all_parking_lots (\n" +
            "name VARCHAR(8),\n" +
            "capacity SMALLINT);";
    utils.executeStatement(sql);
  }

  public void insertIntoParkingDatabase(String index, int capacity) throws SQLException {
    String sql = "INSERT INTO all_parking_lots (name, capacity) VALUES (?, ?);";
    PreparedStatement statement = utils.prepare(sql);
    statement.setString(1, index);
    statement.setInt(2, capacity);
    statement.executeUpdate();
    statement.close();
  }

  public void createParkingTable(String index, int capacity) throws SQLException {
    String createSql = String.format("CREATE TABLE IF NOT EXISTS parking_lot_%s (" +
            "id SMALLINT, plate_number VARCHAR(20));", index);
    utils.executeStatement(createSql);

    for (int i = 1; i < capacity + 1; i++) {
      String insertSql = String.format("INSERT INTO parking_lot_%s (id) VALUES (?);", index);
      PreparedStatement statement = utils.prepare(insertSql);
      statement.setInt(1, i);
      statement.executeUpdate();
      statement.close();
    }
  }

  public void close() {
    utils.close();
  }
}
