package de.tum.in.eist.rentalcar;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.gson.JsonObject;

import de.tum.in.eist.JsonHelper;
import de.tum.in.eist.Location;
import de.tum.in.eist.URLFetchServiceHelper;

/**
 * Client to query Rental Car API and parse the 
 * JSON response to respective POJOs
 */
public class RentalCarApiClient {

  private final URLFetchService service;

  @Inject
  public RentalCarApiClient(URLFetchService service) {
    this.service = service;
  }

  /**
   * Sends a http GET request to Rental Car API
   * to get a rental Car. 
   */
  public RentalCar getRentalCar(String carClass, Location origin) throws IOException {
    URL url =
        new URL("http://tum-rental-car-api.appspot.com/getRentalCar/" + carClass
            + "/" + origin.getLatitude() + "/" + origin.getLongitude());

    HTTPResponse response = service.fetch(url);
    
    // Get the data from response as String
    String jsonString = URLFetchServiceHelper.toString(response);
    try {
      // Parse JSON from string
      JsonObject json = JsonHelper.parse(jsonString);
      return toRentalCar(json);
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    }
  }
  
  /**
   * Sends a http GET request to Rental Car API
   * to make rental car trip
   */
  public RentalCarTrip rentalCarTrip(String carClass, Location origin, Location destination) throws IOException {
    URL url = new URL("http://tum-rental-car-api.appspot.com/makeTrip/" + carClass
        + "/" + origin.getLatitude() + "/" + origin.getLongitude()
        + "/" + destination.getLatitude() + "/" + destination.getLongitude());

    HTTPResponse response = service.fetch(url);
    
    // Get the data from response as String
    String jsonString = URLFetchServiceHelper.toString(response);
    try {
      // Parse JSON from string
      JsonObject json = JsonHelper.parse(jsonString);
      return toRentalCarTrip(json);
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    }
  }

  /**
   * Checks the API data from {@link #getRentalCar(String, Location)} for errors.
   * Maps this JSON data to {@link RentalCar} POJO
   */
  public static RentalCar toRentalCar(JsonObject response) {
    if (!response.has("status")) {
      throw new IllegalArgumentException("No Status set from Car API");
    } else {
      if (!response.get("status").getAsString().equals("ok"))
        throw new IllegalArgumentException(response.get("status").getAsString());
      else {
        Location rentalCarLoc = toLocation(response);
        String duration = response.get("duration").getAsString();
        String distance = response.get("distance").getAsString();
        int seats = response.get("typicalSeating").getAsInt();
        String type = response.get("carTypeName").getAsString();
        return new RentalCar(distance, rentalCarLoc, duration, seats, type);
      }
    }
  }
  
  /**
   * Checks the API data from {@link #rentalCarTrip(String, Location, Location)} for errors.
   * Maps this JSON data to {@link RentalCarTrip} POJO 
   */
  private static RentalCarTrip toRentalCarTrip(JsonObject response) {
    if (!response.has("status")) {
      throw new IllegalArgumentException("No Status set from Car API");
    } else {
      if (!response.get("status").getAsString().equals("ok"))
        throw new IllegalArgumentException(response.get("status").getAsString());
      else {
        String duration = response.get("duration").getAsString();
        String distance = response.get("distance").getAsString();
        String price = response.get("totalPrice").getAsString();
        String carClass = response.get("carTypeName").getAsString();
        return new RentalCarTrip(distance, duration, price, carClass);
      }
    }
  }

  /**
   * Converts JSON latitude and longitude of any location
   * to {@link Location} POJO
   */
  private static Location toLocation(JsonObject car) {
    return new Location(car.get("latitude").getAsDouble(), car.get("longitude").getAsDouble());
  }
}
