package de.tum.in.eist;

import java.util.concurrent.Future;

import de.tum.in.eist.rentalcar.RentalCarAPI;

/**
 * This is the TUM Mobility Services application.
 */
public class MobilityServices { 

  public static void main(String args[]) throws Exception {
    /* TODO
     * 1. Take User Inputs
     * 2. Query API Classes
     * 3. Call RankingSystem to calculate total
     * 4. Call RankingSystem to find optimal travel option
     * 5. Display all travel options with ranking/recommendation to user. 
     */
    
    // Example Location Object - Source Location coordinates
    Trip trip = new Trip();
    Location sourceLocation = new Location(48.18363, 13.49423);
    Location destLocation = new Location(50.23186, 17.70);
    
    // Example Synchronous API Call - Make Rental Car at trip
    trip.rentalCarTrip = RentalCarAPI.MakeTrip("Bike", sourceLocation, destLocation);
    
    // Example Asynchronous API Call - Make Rental Car trip
    System.out.println("Asynchonous API call started");
    Future<RentalCarAPI> sourceCarTrip = RentalCarAPI.asyncMakeTrip("PremiumCar", sourceLocation, destLocation);
    while(!sourceCarTrip.isDone()){
      System.out.println("Parellel work on client.");
    }
    trip.rentalCarTrip = sourceCarTrip.get().trip.toRentalCar();
    System.out.println("API Call completed");
    
    // Example Utils Function - Get Train Station at source
    Location sourceTrainStation = Utils.getTrainStation(sourceLocation);
    
    // Example Utils Function - Find distance between 2 Locations
    double distance = Utils.findDistance(sourceLocation, destLocation);
    
    /* Hint - if distance < 500 kms, user can go travel
    *         by rental car or by train.
    *         if 500 < distance < 1000, user can only
    *         travel by train.
    *         if distance > 1000 kms, then no travel option
    *         is possible
    */  
  }
  
  /**
   * Maps user class with car class<br>
   * Student - Bike<br>
   * Business - PremiumCar<br>
   * Family - MidsizeSUV
   */
  private static String getCarClass(String userClass) {
    return null;
  }
  
  /**
   * Maps user class with cabin class<br>
   * Student - ThirdClass<br>
   * Business - FirstClass<br>
   * Family - Economy
   */
  private static String getCabinClass(String userClass) {
    return null;
  }
  
  /**
   * @return Car Class depending upon user Class and the distance between locations<br>
   * For distance < 5 kms return Taxi<br>
   * else find the car class mapped with user class
   */
  private static String findCarClass(Location origin, Location destination, String userClass) {
    return null;
  }
}
