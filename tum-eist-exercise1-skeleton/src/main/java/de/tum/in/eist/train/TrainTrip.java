package de.tum.in.eist.train;

import java.util.Objects;

/**
 * POJO to store information of train trip returned by 
 * {@link TrainAPI} - MakeTrip call
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
