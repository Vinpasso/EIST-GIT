package de.tum.in.eist.train;

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
 * <p>Train API<br>
 * Gives response is in JSON format<br>
 * <b>YOU ARE NOT SUPPOSED TO CHANGE THE CONTENTS OF THIS CLASS!!!</b><br>
 * <b>YOU ARE NOT SUPPOSED TO USE THIS CLASS ANYWHERE</b></p>
 * 
 * <p>API Gives following information - <br><br>
 * 
 *    1. Train travel distance in kms.<br>
 *    2. Trip duration in minutes/hrs.<br>
 *    3. Ticket price in EUR<br>
 *    4. Type of cabin<br>
 *    5. Number of seats user booked</p>
 *    
 * <p>Input Parameters Comment - <br>
 *    String cabinClass = FirstClass, Economy, ThirdClass</p>
 */

@Path("/trainApi")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class TrainAPI {

  /**
   * Query this method through following http GET call <br>
   * http://localhost:8080/trainApi/makeTrip/{cabinClass}/{seats}/{source Latitude}/{sourceLat}/{sourceLong}/{destLat}/{destLong}<br>
   * where {} are your inputs
   */
  @GET
  @Path("/makeTrip/{cabinClass}/{seats}/{sourceLat}/{sourceLong}/{destLat}/{destLong}")
  public Response MakeTrip(@PathParam("cabinClass") String cabinClass,
      @PathParam("seats") int seats,
      @PathParam("sourceLat") double sourceLat,
      @PathParam("sourceLong") double sourceLong,
      @PathParam("destLat") double destLat,
      @PathParam("destLong") double destLong) {
    if(!cabinClass.equals("Economy")&&!cabinClass.equals("FirstClass")&&!cabinClass.equals("ThirdClass"))
      return getError("Invalid cabinClass "+ cabinClass);
    Location sourceLoc, destLoc;
    try{
      sourceLoc = new Location(sourceLat, sourceLong);
      destLoc = new Location(destLat, destLong);
    } catch(IllegalArgumentException e){
      return getError(e.getMessage());
    }
    double distance = Utils.round(Utils.findDistance(sourceLoc, destLoc), 1);
    if (distance > 1000000) {
      return getError("Distance is greater than 1000 Kms");
    }
    if (seats > 9) {
      return getError("Seats are greater than 9");
    }
    if (seats < 1) {
      return getError("Seats can't be less than 1");
    }
    TrainData trip = new TrainData(cabinClass, distance, seats);
    trip.calculate();

    JsonObject json = new JsonObject();
    json.addProperty("distance", trip.distance + " Kms");
    json.addProperty("duration", trip.duration + " " + trip.durationUnit);
    json.addProperty("totalPrice", trip.totalPrice + " EUR");
    json.addProperty("seats", trip.seats);
    json.addProperty("cabin", trip.cabinType);
    json.addProperty("status", "ok");

    return Response.ok(json.toString()).build();
  }

  static Response getError(String error) {
    JsonObject json = new JsonObject();
    json.addProperty("status", "Error: " + error);
    return Response.ok(json.toString()).build();
  }

}
