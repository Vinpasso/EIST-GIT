package de.tum.in.eist.rentalcar;

import java.util.Objects;

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
