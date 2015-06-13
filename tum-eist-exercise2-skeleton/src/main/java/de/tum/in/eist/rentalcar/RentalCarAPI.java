package de.tum.in.eist.rentalcar;

import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;

import de.tum.in.eist.Location;
import de.tum.in.eist.Utils;

/**
 * <p>Rental Car API.<br>
 * Gives response is in JSON format<br>
 * <b>YOU ARE NOT SUPPOSED TO CHANGE THE CONTENTS OF THIS CLASS!!!</b><br>
 * <b>YOU ARE NOT SUPPOSED TO USE THIS CLASS DIRECTLY</b></p>
 * 
 * <p>API Gives following information - <br><br>
 * 
 *    1. To get rental car - <br>
 *       a. Coordinates of rental car<br>
 *       b. Walk distance in meters<br>
 *       c. Walk duration in minutes<br>
 *       d. Type of rental car<br>
 *       e. Typical seats in that type of rental car<br><br>
 *       
 *    2. To travel by rental car - <br>
 *       a. Distance covered by rental car in meters/kms<br>
 *       b. Driving duration in minutes/hrs<br>
 *       c. Fare in EUR</p>
 */

@Path("/rentalCarApi")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class RentalCarAPI {

  /**
   * Query this method through following http GET call <br>
   * http://localhost:8080/rentalCarApi/getRentalCar/{carClass}/{source Latitude}/{source longitude}<br>
   * where {} are your inputs
   */
  @GET
  @Path("/getRentalCar/{carClass}/{latitude}/{longitude}")
  public Response GetRentalCar(
      @PathParam("carClass") String carClass,
      @PathParam("latitude") double sourceLatitude,
      @PathParam("longitude") double sourceLongitude)
    {
    if(!carClass.equals("Bike")&&!carClass.equals("PremiumCar")&&!carClass.equals("MidsizeSUV")&&!carClass.equals("Taxi"))
      return getError("Invalid carClass "+ carClass);
    Random r = new Random();
    double dispFactorLat = Utils.round((r.nextDouble() + r.nextInt(2) + 1) / 1000, 6);
    double dispFactorLong = Utils.round((r.nextDouble() + r.nextInt(2) + 1) / 1000, 6);
    double carLocLatitude = Utils.round(sourceLatitude + dispFactorLat, 6);
    double carLocLongitude = Utils.round(sourceLongitude + dispFactorLong, 6);
    Location sourceLoc, carLoc;
    try{
      sourceLoc = new Location(sourceLatitude, sourceLongitude);
      carLoc = new Location(carLocLatitude, carLocLongitude);
    } catch(IllegalArgumentException e){
      return getError(e.getMessage());
    }
    double distance =
        Utils.round(Utils.findDistance(sourceLoc, carLoc),
            1);
    RentalCarData trip = new RentalCarData(carClass, distance, "Walk");
    trip.calculate();
    JsonObject json = new JsonObject();
    json.addProperty("latitude", carLocLatitude);
    json.addProperty("longitude", carLocLongitude);
    json.addProperty("distance", trip.distance + " " + trip.distanceUnit);
    json.addProperty("duration", trip.duration + " " + trip.durationUnit);
    json.addProperty("typicalSeating", trip.typicalSeating);
    json.addProperty("carTypeName", trip.carClass);
    json.addProperty("mode", trip.mode);
    json.addProperty("status", "ok");
    return Response.ok(json.toString()).build();
  }
  
  /**
   * Query this method through following http GET call <br>
   * http://localhost:8080/rentalCarApi/makeTrip/{carClass}/{sourceLat}/{sourceLong}/{destLat}/{destLong}<br>
   * where {} are your inputs
   */
  @GET
  @Path("/makeTrip/{carClass}/{sourceLat}/{sourceLong}/{destLat}/{destLong}")
  public Response MakeTrip(
      @PathParam("carClass") String carClass,
      @PathParam("sourceLat") double sourceLat, 
      @PathParam("sourceLong") double sourceLong,
      @PathParam("destLat") double destLat,
      @PathParam("destLong") double destLong) {
    if(!carClass.equals("Bike")&&!carClass.equals("PremiumCar")&&!carClass.equals("MidsizeSUV")&&!carClass.equals("Taxi"))
      return getError("Invalid carClass "+ carClass);
    Location sourceLoc, destLoc;
    try{
      sourceLoc = new Location(sourceLat, sourceLong);
      destLoc = new Location(destLat, destLong);
    } catch(IllegalArgumentException e){
      return getError(e.getMessage());
    }
    double distance = Utils.round(Utils.findDistance(sourceLoc, destLoc), 1);
    RentalCarData trip = new RentalCarData(carClass, distance, "Vehicle");
    trip.calculate();
    if (distance > 500000) {
      return getError("Distance is greater than 500 Kms");
    } else {
      JsonObject json = new JsonObject();
      json.addProperty("distance", trip.distance + " " + trip.distanceUnit);
      json.addProperty("duration", trip.duration + " " + trip.durationUnit);
      json.addProperty("totalPrice", trip.totalPrice + " EUR");
      json.addProperty("typicalSeating", trip.typicalSeating);
      json.addProperty("carTypeName", trip.carClass);
      json.addProperty("mode", trip.mode);
      json.addProperty("status", "ok");
      return Response.ok(json.toString()).build();
    }
  }

  static Response getError(String error) {
    JsonObject json = new JsonObject();
    json.addProperty("status", "Error: " + error);
    return Response.ok(json.toString()).build();
  }

}
