package de.tum.in.eist;

import java.util.Objects;

/**
 * POJO to store coordinates of any Location
 */
public class Location {

  private final double latitude;
  private final double longitude;

  public Location(double latitude, double longitude) {
    validateInputs(latitude, longitude);
    this.latitude = latitude;
    this.longitude = longitude;
  }

  /**
   * Function to validate the coordinates<br>
   * Validate that - <br>
   *      -90 <= latitude <= 90 <br>
   *     -180 <= longitude <= 180 
   */
  private void validateInputs(double latitude, double longitude) {
    if(latitude<-90.0 || latitude>90.0)
      throw new IllegalArgumentException("Latitude can be only between -90.0 and 90.0");
    if(longitude<-180.0 || longitude>180.0)
      throw new IllegalArgumentException("Longitude can be only between -180.0 and 180.0");
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  @Override
  public int hashCode() {
    return Objects.hash(latitude, longitude);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Location) {
      Location other = (Location) obj;
      return this.latitude == other.latitude
          && this.longitude == other.longitude;
    }
    return false;
  }
}
