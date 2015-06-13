package de.tum.in.eist.rentalcar;

import java.util.Objects;

import com.google.gson.JsonObject;

import de.tum.in.eist.JsonHelper;

/**
 * POJO to store information of Rental Car trip (price, duration, etc.) returned by 
 * {@link RentalCarAPI} - MakeTrip call
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
    /*
     * You have to parse POJO of
     * RentalCarTrip to JSON here
     * See RentalCar.toJSON() for example
     * In skeleton for now, we are returning some fixed
     * JSON data
     */
    return JsonHelper.parse("{\"distance\": \"100.5 Meters\","
        + " \"duration\": \"5.6 Minutes\","
        + " \"price\": \"20.50 EUR\","
        + " \"carClass\": \"Taxi\"}");
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
