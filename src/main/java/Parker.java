import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Parker {

  private JDBCUtils utils = new JDBCUtils();

  public boolean isParkingLotAvailable() throws SQLException {
    String sql = "SELECT SUM(capacity) AS sum FROM all_parking_lots;";
    ResultSet resultSet = utils.executeSelectStatement(sql);
    resultSet.next();
    int totalCapacity = resultSet.getInt("sum");
    return totalCapacity > 0;
  }

  public String findParkingLot() throws SQLException {
    String sql = "SELECT name FROM all_parking_lots WHERE capacity != 0 ORDER BY name LIMIT 1;";
    ResultSet resultSet = utils.executeSelectStatement(sql);
    resultSet.next();
    return resultSet.getString("name");
  }

  // Need change
  public int findParkingSpace(String parkingLot) throws SQLException {
    String sql = String.format("SELECT COUNT(plate_number) AS count FROM parking_lot_%s;", parkingLot);
    ResultSet resultSet = utils.executeSelectStatement(sql);
    resultSet.next();
    int spaceNumber = resultSet.getInt("count") + 1;
    return spaceNumber;
  }

  public void parkIntoSpace(ParkingTicket ticket) throws SQLException {
    decreaseOneSpace(ticket.getParkingLot());

    String sql = String.format("INSERT INTO parking_lot_%s (id, plate_number) VALUES (?, ?);", ticket.getParkingLot());
    PreparedStatement statement = utils.prepare(sql);
    statement.setInt(1, ticket.getParkingSpaceNumber());
    statement.setString(2, ticket.getPlateNumber());
    statement.executeUpdate();
    statement.close();
  }

  public void decreaseOneSpace(String parkingLot) throws SQLException {
    String selectSql = "SELECT capacity FROM all_parking_lots WHERE name = ?;";
    PreparedStatement selectStatement = utils.prepare(selectSql);
    selectStatement.setString(1, parkingLot);
    ResultSet resultSet = selectStatement.executeQuery();
    resultSet.next();
    int capacity = resultSet.getInt("capacity");

    String updateSql = "UPDATE all_parking_lots SET capacity = ? WHERE name = ?;";
    PreparedStatement updateStatement = utils.prepare(updateSql);
    updateStatement.setInt(1, capacity - 1);
    updateStatement.setString(2, parkingLot);
    updateStatement.executeUpdate();

    selectStatement.close();
    updateStatement.close();
  }

  public void close() {
    utils.close();
  }
}
