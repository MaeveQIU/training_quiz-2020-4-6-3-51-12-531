public class ParkingTicket {

  private String parkingLot;
  private int parkingSpaceNumber;
  private String plateNumber;

  public ParkingTicket(String parkingLot, int parkingSpaceNumber, String plateNumber) {
    this.parkingLot = parkingLot;
    this.parkingSpaceNumber = parkingSpaceNumber;
    this.plateNumber = plateNumber;
  }

  public String getParkingLot() {
    return parkingLot;
  }

  public int getParkingSpaceNumber() {
    return parkingSpaceNumber;
  }

  public String getPlateNumber() {
    return plateNumber;
  }

  @Override
  public String toString() {
    return parkingLot + "," + parkingSpaceNumber + "," + plateNumber;
  }
}
