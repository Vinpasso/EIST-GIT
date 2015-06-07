package de.tum.in.eist.train;

import de.tum.in.eist.Location;
import de.tum.in.eist.Utils;

/**
 * <p>Train API. <br>
 * <b>YOU ARE NOT SUPPOSED TO CHANGE THE CONTENTS OF THIS CLASS!!!</b><br>
 * You can only use the method {@link #MakeTrip(String, int, Location, Location)} to query this API<br>
 * </p>
 * <p>API Gives following information - <br><br>
 * 
 *    1. Train travel distance in kms.<br>
 *    2. Trip duration in minutes/hrs.<br>
 *    3. Ticket price in EUR<br>
 *    4. Type of cabin<br>
 *    5. Number of seats user booked<br></p>
 *    
 * <p>Input Parameters Comment - <br>
 *    String cabinClass = FirstClass, Economy, ThirdClass </p>
 */
/*
 * Hint - Use suitable carClass for each userClass.
 */

public class TrainAPI {

  /**
   * Use this API call to travel by train from source train station
   * to destination train station. 
   * @return the information of Train trip in {@link TrainTrip} POJO
   */
  /*
   * Hint - Get the train station coordinates from Utils.getTrainStation() method
   * Hint - Use the POJO returned by this API call throughout the code. Eg - In RankingSystem
   */
  public static TrainTrip MakeTrip(String cabinClass, int seats, Location sourceLoc, Location destLoc) {
    if(!cabinClass.equals("Economy")&&!cabinClass.equals("FirstClass")&&!cabinClass.equals("ThirdClass"))
      getError("Invalid cabinClass "+ cabinClass);
    double distance = Utils.round(Utils.findDistance(sourceLoc, destLoc), 1);
    if (distance > 1000000) {
      getError("Distance is greater than 1000 Kms");
    }
    if (seats > 9) {
      getError("Seats are greater than 9");
    }
    if (seats < 1) {
      getError("Seats can't be less than 1");
    }

    TrainData trip = new TrainData(cabinClass, distance, seats);
    trip.calculate();
    return new TrainTrip(
        trip.distance + " Kms",
        trip.duration + " " + trip.durationUnit,
        trip.totalPrice + " EUR",
        trip.cabinType,
        trip.seats
        );
  }

  static void getError(String error) {
    throw new IllegalArgumentException("Train API error: " + error);
  }
}
