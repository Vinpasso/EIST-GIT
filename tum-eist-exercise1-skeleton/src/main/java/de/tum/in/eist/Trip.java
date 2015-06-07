package de.tum.in.eist;

import de.tum.in.eist.rentalcar.RentalCar;
import de.tum.in.eist.rentalcar.RentalCarTrip;
import de.tum.in.eist.train.TrainTrip;

/**
 * Represents a trip being planned.
 */
public class Trip {
  /*
   * POJOs for Rental Car API. 
   * carApiSuccess flag represents whether Rental Car API call is successful or not. 
   * We use this flag in MobilityServices to call ranking system/show output.
   */
  RentalCar rentalCar;
  RentalCarTrip rentalCarTrip;
  boolean carApiSuccess = false;

  /*
   * POJOs for Train API. 
   * trainApiSuccess flag represents whether Train API call is successful or not.
   * We use this flag in MobilityServices to call ranking system/show output.
   */
  RentalCar sourceRentalCar;
  RentalCarTrip sourceCarTrip;
  RentalCarTrip destCarTrip;
  TrainTrip trainTrip;
  boolean trainApiSuccess = false;
  
  /*
   * POJOs to calculate total Price, total duration and total distance
   * for Rental Car travel option and Train travel option
   */
  RankingSystem trainTravel = new RankingSystem();
  RankingSystem carTravel = new RankingSystem();
}
