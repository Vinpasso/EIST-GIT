package de.tum.in.eist.train;

import java.util.Objects;

import com.google.gson.JsonObject;

/**
 * POJO to store information of train trip returned by 
 * TrainAPI
 */
public class TrainTrip {

  private final String distance;
  private final String duration;
  private final String price;
  private final String cabin;
  private final int seats;

  public TrainTrip(String distance, String duration, String price, String cabin, int seats) {
    this.distance = distance;
    this.duration = duration;
    this.price = price;
    this.cabin = cabin;
    this.seats = seats;
  }

  /**
   * @return train trip distance in kms
   */
  public String getDistance() {
    return distance;
  }

  /**
   * @return train trip duration with unit
   */
  public String getDuration() {
    return duration;
  }

  /**
   * @return train travel price in EUR
   */
  public String getPrice() {
    return price;
  }
  
  public String getCabin(){
    return cabin;
  }
  
  public int getSeats(){
    return seats;
  }
  
  /**
   * @return train trip duration value in hrs.
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
   * @return train trip distance value in kms.
   */
  public double getDistanceValue() {
    String[] parsedDistance = distance.split(" ");
      return Double.parseDouble(parsedDistance[0]);
  }
  
  /**
   * @return train trip price value in EUR.
   * 
   */
  public double getPriceValue() {
    String priceValue = price.split(" ")[0];
    return Double.parseDouble(priceValue);
  }
  
  /**
   * @return JSON of {@link TrainTrip} POJO
   */
  public JsonObject toJSON() {
    JsonObject json = new JsonObject();
    json.addProperty("distance", distance);
    json.addProperty("duration", duration);
    json.addProperty("price", price);
    json.addProperty("cabin", cabin);
    json.addProperty("seats", seats);
    return json;
  }

  @Override
  public int hashCode() {
    return Objects.hash(distance, duration, price, cabin, seats);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof TrainTrip) {
      TrainTrip other = (TrainTrip) obj;
      return this.distance.equals(other.distance)
          && this.duration.equals(other.duration)
          && this.price.equals(other.price)
          && this.cabin.equals(other.cabin)
          && this.seats==other.seats;
    }
    return false;
  }
}
