package de.tum.in.eist.rentalcar;

import java.util.Objects;

import com.google.gson.JsonObject;

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
  
  /**
   * @return walk duration value from source location 
   * to rental car location in hrs.
   */
  public double getDurationValue() {
    String[] parsedDuration = duration.split(" ");
    if (parsedDuration[1].equals("Minutes")) {
      return (Double.parseDouble(parsedDuration[0]) / 60);
    } else {
      return Double.parseDouble(parsedDuration[0]);
    }
  }
  
  /**
   * @return distance value of rental car from source location in Kms.
   */
  public double getDistanceValue() {
    String[] parsedDistance = distance.split(" ");
    if (parsedDistance[1].equals("Meters")) {
      return (Double.parseDouble(parsedDistance[0]) / 1000);
    } else {
      return Double.parseDouble(parsedDistance[0]);
    }
  }
  
  /**
   * @return JSON of {@link RentalCar} POJO
   */
  public JsonObject toJSON(){
    JsonObject json = new JsonObject();
    json.addProperty("distance", distance);
    json.add("coordinates", coordinates.toJSON());
    json.addProperty("duration", duration);
    json.addProperty("seats", seats);
    json.addProperty("type", type);
    return json;
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
