package de.tum.in.eist.rentalcar;

import java.util.Objects;

import de.tum.in.eist.Location;

/**
 * POJO to store information of Rental Car (Type, seats, location, etc) returned by 
 * {@link RentalCarAPI} - GetRentalCar call
 */
public class RentalCar {

  private final String distance;
  private final Location coordinates;
  private final String duration;
  private final int seats;
  private final String type;

  public RentalCar(String distance, Location coordinates, String duration, int seats, String type) {
    this.distance = distance;
    this.coordinates = coordinates;
    this.duration = duration;
    this.seats = seats;
    this.type = type;
  }

  /** 
   * @return distance of rental car from source location with unit
   */
  public String getDistance() {
    return distance;
  }

  /** 
   * @return coordinates of rental car in {@link Location} POJO
   */
  public Location getCoordinates() {
    return coordinates;
  }

  /** 
   * @return walk duration from source location 
   * to rental car location with unit
   */
  public String getDuration() {
    return duration;
  }
  
  public int getSeats() {
    return seats;
  }
  
  public String getType() {
    return type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(distance, coordinates, duration, seats, type);
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof RentalCar) {
      RentalCar other = (RentalCar) obj;
      return this.distance.equals(other.distance)
          && this.coordinates.equals(other.coordinates)
          && this.duration.equals(other.duration)
          && this.seats==other.seats
          && this.type.equals(other.type);
    }
    return false;
  }
}
