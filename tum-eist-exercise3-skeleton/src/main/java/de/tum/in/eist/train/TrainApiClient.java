package de.tum.in.eist.train;

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
 * Client to query Train API and parse the 
 * JSON response to {@link TrainTrip} POJO
 */
public class TrainApiClient {

  private final URLFetchService service;

  @Inject
  public TrainApiClient(URLFetchService service) {
    this.service = service;
  }

  /**
   * Sends a http GET request to Train API
   * to make train trip. 
   */
  public TrainTrip trainTrip(String userClass, int seats, Location origin, Location destination) throws IOException {
    String cabinClass = getCabinClass(userClass);
    URL url = new URL("http://tum-train-api.appspot.com/makeTrip/" + cabinClass + "/" + seats
        + "/" + origin.getLatitude() + "/" + origin.getLongitude()
        + "/" + destination.getLatitude() + "/" + destination.getLongitude());
    HTTPResponse response = service.fetch(url);
    
    // Get the data from response as String
    String jsonString = URLFetchServiceHelper.toString(response);
    try {
      // Parse JSON from string
      JsonObject json = JsonHelper.parse(jsonString);
      return toTrainTrip(json);
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    }
  }

  /**
   * Maps user class with cabin class<br>
   * Student - ThirdClass<br>
   * Business - FirstClass<br>
   * Family - Economy
   */
  static String getCabinClass(String userClass) {
    switch(userClass){
      case "Student":
        return "ThirdClass";
      case "Business":
        return "FirstClass";
      case "Family":
        return "Economy";
      default:
        throw new IllegalArgumentException(userClass + " is an invalid User Class");
    }
  }

  /**
   * Checks the API data from {@link #trainTrip(String, int, Location, Location)} for errors.
   * Maps this JSON data to {@link TrainTrip} POJO 
   */
  public static TrainTrip toTrainTrip(JsonObject response) {
    if (!response.has("status")) {
      throw new IllegalArgumentException("No Status set from Train API");
    } else {
      if (!response.get("status").getAsString().equals("ok"))
        throw new IllegalArgumentException(response.get("status").getAsString());
      else {
        String duration = response.get("duration").getAsString();
        String distance = response.get("distance").getAsString();
        String price = response.get("totalPrice").getAsString();
        String cabin = response.get("cabin").getAsString();
        int seats = response.get("seats").getAsInt();
        return new TrainTrip(distance, duration, price, cabin, seats);
      }
    }
  }
}
