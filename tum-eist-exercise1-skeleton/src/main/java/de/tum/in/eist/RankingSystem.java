package de.tum.in.eist;

import de.tum.in.eist.rentalcar.RentalCar;
import de.tum.in.eist.rentalcar.RentalCarTrip;
import de.tum.in.eist.train.TrainTrip;

/**
 * Ranking System calculates total for each travel option
 * and compares them based on the userClass
 */
public class RankingSystem {
  public double totalPrice = 0;
  public double totalDuration = 0;
  public double totalDistance = 0;

  /**
   * Calculates total of car travel option
   */
  public void calculateCarTotal(RentalCar rentalCar, RentalCarTrip rentalCarTrip) {
    /*
     * TODO 
     * Extract the information from rentalCar and rentalCarTrip
     * and add them to calculate totalPrice, totalDuration and totalDistance
     * Note - Units of distance and duration may not be the same
     *        for rentalCar and rentalCarTrip. You have to find 
     *        a solution for this before adding them up.
     */
	  totalPrice = fixPrice(rentalCarTrip.getPrice());
	  totalDuration = fixDurations(rentalCar.getDuration()) + fixDurations(rentalCarTrip.getDuration());
	  totalDistance = fixDistances(rentalCar.getDistance()) + fixDistances(rentalCarTrip.getDistance());
  }

private double fixPrice(String price) {
	return Double.parseDouble(price.substring(0, price.indexOf(" ")));
}
  
  private double fixDurations(String duration)
  {
	  double time = Double.parseDouble(duration.substring(0, duration.indexOf(" ")));
	  String rest = duration.substring(duration.indexOf(" ") + 1);
	  switch(rest)
	  {
	  case "Minutes":
		  break;
	  case "Hours":
		  time *= 60;
	  }
	  return time;
  }
  
  private double fixDistances(String distance)
  {
	  double meters = Double.parseDouble(distance.substring(0, distance.indexOf(" ")));
	  String rest = distance.substring(distance.indexOf(" ") + 1);
	  switch(rest)
	  {
	  case "Meters":
		  break;
	  case "Kms":
		  meters *= 1000;
	  }
	  return meters;
  }

  /**
   * Calculates total of train travel option
   */
  public void calculateTrainTotal(RentalCar rentalCar, RentalCarTrip sourceCarTrip,
      TrainTrip trainTrip, RentalCarTrip destCarTrip) {
    /*
     * TODO
     * Extract the information from rentalCar, sourceCarTrip, trainTrip and destCarTrip
     * and add them to calculate totalPrice, totalDuration and totalDistance
     * Note - Units of distance and duration may not be the same
     *        for rentalCar and rentalCarTrip. You have to find 
     *        a solution for this before adding them up.
     */
	  totalPrice = fixPrice(sourceCarTrip.getPrice()) + fixPrice(destCarTrip.getPrice()) + fixPrice(trainTrip.getPrice());
	  totalDuration = fixDurations(rentalCar.getDuration()) + fixDurations(sourceCarTrip.getDuration()) + fixDurations(destCarTrip.getDuration()) + fixDurations(trainTrip.getDuration());
	  totalDistance = fixDistances(rentalCar.getDistance()) + fixDistances(sourceCarTrip.getDistance()) + fixDistances(destCarTrip.getDistance()) + fixDurations(trainTrip.getDistance());
  }

  /**
   * finds the most optimal options for a particular
   * class of customer.<br>
   * For eg - Business - Shortest Time<br>
   *          Family - Longest Time<br>
   *          Student - Cheapest<br>
   */
  static String findOptimalOption(String userClass, Trip trip) {
    /*
     * TODO
     * Calculate the most optimal travel option here.
     * You may return a String like "Car" or "Train" from this method
     * as recommended option or may choose your own approach to
     * return something else.
     * Hint - Make sure you have totalPrice, totalDuration and totalDistance
     * of each option before comparing them. See calculateTrainTotal for example.
     */
	  switch (userClass) {
	case "businessman": //lowest duration
	case "geschäftskunde":
	case "geschaeftskunde":
		if(trip.carTravel.totalDuration<trip.trainTravel.totalDuration){
			return "car";
		} else {
			return "train";
		}
	case "student": //lowest price
		if(trip.carTravel.totalPrice<trip.trainTravel.totalPrice){
			return "car";
		} else {
			return "train";
		}
	case "family": //highest duration
	case "familie":
		if(trip.carTravel.totalDuration>trip.trainTravel.totalDuration){
			return "car";
		} else {
			return "train";
		}
	default:
		break;
	}
    return null;
  }
}
