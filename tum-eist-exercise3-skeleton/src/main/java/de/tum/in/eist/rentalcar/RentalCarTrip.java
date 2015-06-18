package de.tum.in.eist.rentalcar;

import java.util.Objects;

import com.google.gson.JsonObject;

/**
 * POJO to store information of Rental Car trip (price, duration, etc.) returned by 
 * Rental Car API
 */
public class RentalCarTrip {

  private final String distance;
  private final String duration;
  private final String price;
  private final String carClass;

  public RentalCarTrip(String distance, String duration, String price, String carClass) {
    this.distance = distance;
    this.duration = duration;
    this.price = price;
    this.carClass = carClass;
  }

  /**
   * @return distance of rental car trip with unit
   */
  public String getDistance() {
    return distance;
  }

  /**
   * @return driving duration of rental car trip with unit
   */
  public String getDuration() {
    return duration;
  }

  /**
   * @return price of rental car trip in EUR
   */
  public String getPrice() {
    return price;
  }
  
  public String getCarClass(){
    return carClass;
  }

  /**
   * @return drive duration value of rental car trip in hrs
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
   * @return distance value of rental car trip in Kms.
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
   * @return price value of rental car trip in EUR
   */
  public double getPriceValue() {
    String priceValue = price.split(" ")[0];
    return Double.parseDouble(priceValue);
  }
  
  /**
   * @return JSON of {@link RentalCarTrip} POJO
   */
  public JsonObject toJSON() {
    JsonObject json = new JsonObject();
    json.addProperty("distance", distance);
    json.addProperty("duration", duration);
    json.addProperty("price", price);
    json.addProperty("carClass", carClass);
    return json;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(distance, duration, price, carClass);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof RentalCarTrip) {
      RentalCarTrip other = (RentalCarTrip) obj;
      return this.distance.equals(other.distance) && this.duration.equals(other.duration)
          && this.price.equals(other.price) && this.carClass.equals(other.carClass);
    }
    return false;
  }

}
