package de.tum.in.eist;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;

import de.tum.in.eist.rentalcar.RentalCarApiClient;
import de.tum.in.eist.train.TrainApiClient;

/**
 * This is the TUM Mobility Services application. Functions - <br>
 * 1. Take User Inputs from Request <br>
 * 2. Query API Classes <br>
 * 3. Call {@link RankingSystem} to calculate totals<br> 
 * 4. Call {@link RankingSystem} to find optimal travel option<br> 
 * 5. Send the results in {@link Response}
 */
@Path("/findMyOptions")
public class MobilityServices {
  private static final int EARTH_RADIUS_KM = 6371;

  private final RentalCarApiClient rentalCarApi;
  private final TrainApiClient trainApi;

  @Inject
  public MobilityServices(RentalCarApiClient rentalCarApi,
      TrainApiClient trainApi){
    this.rentalCarApi = rentalCarApi;
    this.trainApi = trainApi;
  }

  @GET
  @Path("/{userClass}/{source}/{destination}/{seats}/{date}/{nonStop}/{refundable}")
  @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
  public Response findTravelOptions(
      @PathParam("userClass") String userClass,
      @PathParam("source") String source, 
      @PathParam("destination") String destination,
      @PathParam("seats") int seats,
      @PathParam("date") String date,
      @PathParam("nonStop") boolean nonStop,
      @PathParam("refundable") boolean refundable)
      {
    
    // Validate user inputs
    switch (userClass) {
      case "Student":
        if(seats<1 || seats>1)
          return Response.ok(getErrorJSON("Student class must opt for 1 seat only").toString()).build();
        break;
      case "Family":
        if(seats<2 || seats>4)
          return Response.ok(getErrorJSON("Family class must contain atleast 2 / atmost 4 seats").toString()).build();
        break;
      case "Business":
        if(seats<1 || seats>1)
          return Response.ok(getErrorJSON("Business class must opt for 1 seat only").toString()).build();
        break;
      default:
        return Response.ok(getErrorJSON("Invalid User Class "+userClass).toString()).build();
    }
    
    Trip trip = new Trip();
    try {
      /* 
       * Query distance24 api to find the distance between source and destination
       * Note that - source and destination are no longer Location objects, but 
       * address of source and destination. 
       * For now we are assuming 100 kms distance between source and destination
       */
      int distance = 100;
      
      // Using distance in kilometers now. See findDistance(..) method in this class.
      if (distance > 1000) {
        return findLongDistanceTravelOptions();
      } else if (distance > 500) {
        return findMediumDistanceTravelOptions();
      } else if (distance > 0){
        return findShortDistanceTravelOptions();
      } else {
        return Response.ok(getErrorJSON("Distance is 0.").toString()).build();
      }
    } catch (Exception e) {
      return Response.ok(getErrorJSON(e.getMessage()).toString()).build();
    }
  }
  
  /**
   * Queries Flight API<br>
   * Add the JSON we get from Flight API to
   * final JSON<br>
   * Add {@link RankingSystem} results to Final JSON<br>
   * @return {@link Response} with Final JSON data
   */
  /* 
   * Hint - Add more parameters in this method according to your needs.
   */
  private Response findLongDistanceTravelOptions(){
    return Response.ok(getErrorJSON("Long Distance Travel options are not implemented yet").toString()).build();
  }

  /**
   * Queries both Flight API and Train API<br>
   * Add the JSON we get from both APIs to
   * final JSON<br>
   * Add {@link RankingSystem} results to Final JSON<br>
   * @return {@link Response} with Final JSON data
   */
  /* 
   * Hint - Add more parameters in this method according to your needs.
   *        For example see ExampleService class
   */
  private Response findMediumDistanceTravelOptions() {
    return Response.ok(getErrorJSON("Medium Distance Travel options are not implemented yet").toString()).build();
  }

  /**
   * Queries all Mobility Apis - Rental car API, Train API & Flight API<br>
   * Add the JSON we get from all APIs to
   * final JSON<br>
   * Add {@link RankingSystem} results to Final JSON<br>
   * @return {@link Response} with Final JSON data
   */
  /* 
   * Hint - Add more parameters in this method according to your needs.
   *        For example see ExampleService class
   */
  private Response findShortDistanceTravelOptions(){
    return Response.ok(getErrorJSON("Short Distance Travel options are not implemented yet").toString()).build();
  }

  /**
   * @return Car Class depending upon user Class and the distance between locations<br>
   * For distance < 5 kms return Taxi<br>
   * else find the car class mapped with user class
   */
  private static String findCarClass(Location origin, Location destination, String userClass) {
    if (findDistance(origin, destination) <= 5.0) {
      return "Taxi";
    } else {
      return getCarClass(userClass);
    }
  }

  /**
   * Maps user class with car class<br>
   * Student - Bike<br>
   * Business - PremiumCar<br>
   * Family - MidsizeSUV
   */
  private static String getCarClass(String userClass) {
    switch (userClass) {
      case "Student":
        return "Bike";
      case "Business":
        return "PremiumCar";
      case "Family":
        return "MidsizeSUV";
      default:
        throw new IllegalArgumentException(userClass + " is an invalid User Class");
    }
  }

  /**
   * @return JsonObject containing error message.
   */
  private static JsonObject getErrorJSON(String message) {
    JsonObject errorJSON = new JsonObject();
    errorJSON.addProperty("Success", false);
    errorJSON.addProperty("Error", message);
    return errorJSON;
  }

  /**
   * @return the distance in Kilometers
   * between 2 locations
   */
  /*
   * Shifted from Utils as now its only being used by MobilityServices.
   */
  static double findDistance(Location initialLoc, Location finalLoc) {
    double dLat = Math.toRadians(finalLoc.getLatitude() - initialLoc.getLatitude());
    double dLon = Math.toRadians(finalLoc.getLongitude() - initialLoc.getLongitude());
    double lat1 = Math.toRadians(initialLoc.getLatitude());
    double lat2 = Math.toRadians(finalLoc.getLatitude());
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2)
        * Math.cos(lat1) * Math.cos(lat2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return EARTH_RADIUS_KM * c;
  }
}
