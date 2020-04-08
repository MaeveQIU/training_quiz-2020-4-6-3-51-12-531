import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Fetcher {

  private JDBCUtils utils = new JDBCUtils();

  public boolean isTicketValidate(ParkingTicket ticket) throws SQLException {
    boolean isParkingLotExist = isParkingLotExist(ticket);
    if (!isParkingLotExist) {
      return false;
    }

    String sql = String.format("SELECT plate_number FROM parking_lot_%s WHERE id = ?;", ticket.getParkingLot());
    PreparedStatement statement = utils.prepare(sql);
    statement.setInt(1, ticket.getParkingSpaceNumber());
    ResultSet resultSet = statement.executeQuery();
    if (!resultSet.next()){
      return false;
    }
    String plate_number = resultSet.getString("plate_number");
    if (plate_number == null) {
      return false;
    }
    return plate_number.equals(ticket.getPlateNumber());
  }

  public boolean isParkingLotExist(ParkingTicket ticket) throws SQLException {
    String sql = "SELECT name, capacity FROM all_parking_lots WHERE name = ?;";
    PreparedStatement statement = utils.prepare(sql);
    statement.setString(1, ticket.getParkingLot());
    ResultSet resultSet = statement.executeQuery();
    return resultSet.next();
  }

  public void updateParkingDatabase(ParkingTicket ticket) throws SQLException {
    increaseOneSpace(ticket.getParkingLot());

    String sql = String.format("UPDATE parking_lot_%s SET plate_number = NULL WHERE id = ?;", ticket.getParkingLot());
    PreparedStatement statement = utils.prepare(sql);
    statement.setInt(1, ticket.getParkingSpaceNumber());
    statement.executeUpdate();
    statement.close();
  }

  public void increaseOneSpace(String parkingLot) throws SQLException {
    String selectSql = "SELECT capacity FROM all_parking_lots WHERE name = ?;";
    PreparedStatement selectStatement = utils.prepare(selectSql);
    selectStatement.setString(1, parkingLot);
    ResultSet resultSet = selectStatement.executeQuery();
    resultSet.next();
    int capacity = resultSet.getInt("capacity");

    String updateSql = "UPDATE all_parking_lots SET capacity = ? WHERE name = ?;";
    PreparedStatement updateStatement = utils.prepare(updateSql);
    updateStatement.setInt(1, capacity + 1);
    updateStatement.setString(2, parkingLot);
    updateStatement.executeUpdate();

    selectStatement.close();
    updateStatement.close();
  }

  public void close() {
    utils.close();
  }
}
