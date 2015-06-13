package de.tum.in.eist;

/**
 * Represents a trip being planned.
 */
public class Trip {
  /*
   * carApiSuccess flag represents whether Rental Car API call is successful or not. 
   * We use this flag in MobilityServices to call ranking system.
   */
  boolean carApiSuccess = false;

  /*
   * trainApiSuccess flag represents whether Train API call is successful or not.
   * We use this flag in MobilityServices to call ranking system.
   */
  boolean trainApiSuccess = false;
  
  /*
   * POJOs to calculate total Price, total duration and total distance
   * for Rental Car travel option and Train travel option
   */
  RankingSystem trainTravel = new RankingSystem();
  RankingSystem carTravel = new RankingSystem();
}
