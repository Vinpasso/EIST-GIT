package de.tum.in.eist;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;

import de.tum.in.eist.rentalcar.RentalCar;
import de.tum.in.eist.rentalcar.RentalCarApiClient;
import de.tum.in.eist.rentalcar.RentalCarTrip;
import de.tum.in.eist.train.TrainApiClient;
import de.tum.in.eist.train.TrainTrip;



/**
 * This is the TUM Mobility Services application. Functions - <br>
 * 1. Take User Inputs from Request <br>
 * 2. Query API Classes <br>
 * 3. Call {@link RankingSystem} to calculate totals<br> 
 * 4. Call {@link RankingSystem} to find optimal travel option<br> 
 * 5. Send the results in {@link Response}
 */
@Path("/exampleRequest")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class ExampleService {
  private final RentalCarApiClient rentalCarApi;
  private final TrainApiClient trainApi;

  @Inject
  public ExampleService(RentalCarApiClient rentalCarApi, TrainApiClient trainApi) {
    this.rentalCarApi = rentalCarApi;
    this.trainApi = trainApi;
  }

  @GET
  @Path("/{userClass}/{sourceLat}/{sourceLong}/{destLat}/{destLong}/{seats}")
  public Response findTravelOptions(
      @PathParam("userClass") String userClass,
      @PathParam("sourceLat") double sourceLat,
      @PathParam("sourceLong") double sourceLong,
      @PathParam("destLat") double destLat,
      @PathParam("destLong") double destLong,
      @PathParam("seats") int seats
      ) {
    
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
      Location sourceLoc = new Location(sourceLat, sourceLong);
      Location destLoc = new Location(destLat, destLong);
      double distance = Utils.findDistance(sourceLoc, destLoc);
      if (distance > 1000000) {
        return Response.ok(getErrorJSON("Distance is greater than 1000 Kms").toString()).build();
      } else if (distance > 500000) {
        return findMediumDistanceTravelOptions(userClass, sourceLoc, destLoc, distance, seats, trip);
      } else if (distance > 0) {
        return findShortDistanceTravelOptions(userClass, sourceLoc, destLoc, distance, seats, trip);
      } else {
        return Response.ok(getErrorJSON("Distance is 0.").toString()).build();
      }
    } catch (Exception e) {
      return Response.ok(getErrorJSON(e.getMessage()).toString()).build();
    }
  }

  /**
   * Queries Train API
   * Add the JSON we get from Train API to
   * final JSON
   * Add {@link RankingSystem} results to Final JSON
   * @return {@link Response} with Final JSON data
   */
  private Response findMediumDistanceTravelOptions(String userClass, Location sourceLoc,
      Location destLoc, double distance, int seats, Trip trip) {
    JsonObject response = new JsonObject();
    response.addProperty("distance", Utils.round((distance / 1000), 1));
    try {
      response.add("trainTrip", makeTrainApiCall(userClass, sourceLoc, destLoc, seats, trip));
    } catch (Exception e) {
      response.add("trainTrip", getErrorJSON(e.getMessage()));
    }
    response.addProperty("RecommendedOption", RankingSystem.findOptimalOption(userClass, trip));
    response.addProperty("Success", trip.trainApiSuccess);
    return Response.ok(response.toString()).build();
  }

  /**
   * Put error messages
   * to the Response JSON
   */
  private JsonObject getErrorJSON(String string) {
    JsonObject errorJSON = new JsonObject();
    errorJSON.addProperty("Success", false);
    errorJSON.addProperty("Error", string);
    return errorJSON;
  }
  
  /**
   * Queries both Rental car API and Train API
   * Add the JSON we get from both APIs to
   * final JSON
   * Add {@link RankingSystem} results to Final JSON
   * @return {@link Response} with Final JSON data
   */
  private Response findShortDistanceTravelOptions(String userClass, Location sourceLoc,
      Location destLoc, double distance, int seats, Trip trip) {
    JsonObject response = new JsonObject();
    response.addProperty("distance", Utils.round((distance / 1000), 1));
    try {
      response.add("carTrip", makeCarApiCall(userClass, sourceLoc, destLoc, distance, trip));
    } catch (Exception e) {
      response.add("carTrip", getErrorJSON(e.getMessage()));
    }
    try {
      response.add("trainTrip", makeTrainApiCall(userClass, sourceLoc, destLoc, seats, trip));
    } catch (Exception e) {
      response.add("trainTrip", getErrorJSON(e.getMessage()));
    }
    response.addProperty("RecommendedOption", RankingSystem.findOptimalOption(userClass, trip));
    response.addProperty("Success", trip.carApiSuccess || trip.trainApiSuccess);
    return Response.ok(response.toString()).build();
  }

  /**
   * Prepare all the calls for Train Option - <br>
   *    1. Get Rental Car/Taxi at near Source location
   *       depending upon distance between source
   *       location and source Train Station<br>
   *          i.e. if distance <= 5 Km --> Taxi<br>
   *               if distance > 5 Km --> Rental Car<br>
   *    2. Travel by rental Car/Taxi (that you got in 1)
   *       to source Train Station<br>
   *    3. Travel in train from source Train Station
   *       to destination Train Station<br>
   *    4. Finally, go by rental Car/Taxi (depending
   *       upon distance) to your
   *       destination<br>
   *  @return JSON of train Option
   */
  private JsonObject makeTrainApiCall(String userClass, Location sourceLoc, Location destLoc,
      int seats, Trip trip) throws IOException {
    Location sourceTrainStation = Utils.getTrainStation(sourceLoc);
    Location destTrainStation = Utils.getTrainStation(destLoc);
    String carClass = findCarClass(sourceLoc,
        sourceTrainStation, userClass);
    RentalCar rentalCar = rentalCarApi.getRentalCar(carClass, sourceLoc);
    RentalCarTrip sourceCarTrip =
        rentalCarApi.rentalCarTrip(carClass, rentalCar.getCoordinates(), sourceTrainStation);
    TrainTrip trainTrip =
        trainApi.trainTrip(userClass, seats, sourceTrainStation, destTrainStation);
    carClass = findCarClass(destTrainStation, destLoc, userClass);
    RentalCarTrip destCarTrip = rentalCarApi.rentalCarTrip(carClass, destTrainStation, destLoc);
    JsonObject trainPlan = new JsonObject();
    try {
      trainPlan.add("sourceCarLocation", rentalCar.toJSON());
      trainPlan.add("sourceCarTrip", sourceCarTrip.toJSON());
      trainPlan.add("trainTrip", trainTrip.toJSON());
      trainPlan.add("destCarTrip", destCarTrip.toJSON());
      trip.trainTravel.calculateTrainTotal(rentalCar, sourceCarTrip, trainTrip, destCarTrip);
      trainPlan.addProperty("totalTrainDuration", trip.trainTravel.totalDuration);
      trainPlan.addProperty("totalTrainPrice", trip.trainTravel.totalPrice);
      trainPlan.addProperty("totalTrainDistance", trip.trainTravel.totalDistance);
      trainPlan.addProperty("Success", true);
      trip.trainApiSuccess = true;
      return trainPlan;
    } catch (Exception e) {
      throw new IOException("Failed to query Train Api: " + e);
    }
  }

  /**
   * Prepare all the calls for Car Option - <br>
   *    1. Get Rental Car/Taxi at near Source location
   *       depending upon distance between source
   *       location and destination<br>
   *          i.e. if distance <= 5 Km --> Taxi<br>
   *               if distance > 5 Km --> Rental Car<br>
   *    2. Travel by rental Car/Taxi (that you got in 1)
   *       to destination<br>
   * @return JSON of Car Option
   */
  private JsonObject makeCarApiCall(String userClass, Location sourceLoc, Location destLoc,
      double distance, Trip trip) throws IOException {
    RentalCarTrip rentalCarTrip = null;
    RentalCar rentalCar = null;
    String carClass;
    if (distance <= 5000) {
      carClass = "Taxi";
    } else {
      carClass = getCarClass(userClass);
    }
    rentalCar = rentalCarApi.getRentalCar(carClass, sourceLoc);
    rentalCarTrip = rentalCarApi.rentalCarTrip(carClass, rentalCar.getCoordinates(), destLoc);
    JsonObject carPlan = new JsonObject();
    try {
      carPlan.add("sourceCarLocation", rentalCar.toJSON());
      carPlan.add("destCarTrip", rentalCarTrip.toJSON());
      trip.carTravel.calculateCarTotal(rentalCar, rentalCarTrip);
      carPlan.addProperty("totalCarDuration", trip.carTravel.totalDuration);
      carPlan.addProperty("totalCarPrice", trip.carTravel.totalPrice);
      carPlan.addProperty("totalCarDistance", trip.carTravel.totalDistance);
      carPlan.addProperty("Success", true);
      trip.carApiSuccess = true;
      return carPlan;
    } catch (Exception e) {
      throw new IOException("Failed to query Rental Car API: " + e);
    }
  }

  /**
   * @return Car Class depending upon user Class and the distance between locations<br>
   * For distance < 5 kms return Taxi<br>
   * else find the car class mapped with user class
   */
  private static String findCarClass(Location origin, Location destination, String userClass) {
    if (Utils.findDistance(origin, destination) <= 5000.0) {
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

}
