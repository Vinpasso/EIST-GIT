package de.tum.in.eist;

import de.tum.in.eist.rentalcar.RentalCar;
import de.tum.in.eist.rentalcar.RentalCarTrip;
import de.tum.in.eist.train.TrainTrip;

/**
 * Ranking System calculates total for each travel option
 * and compares them based on userClass
 */
public class RankingSystem {
  public double totalPrice = 0;
  public double totalDuration = 0;
  public double totalDistance = 0;

  /**
   * Calculates total of car travel option
   */
  public void calculateCarTotal(RentalCar rentalCar, RentalCarTrip rentalCarTrip) {
    totalDuration = Utils.round((rentalCar.getDurationValue() + rentalCarTrip.getDurationValue()), 2);
    totalDistance = Utils.round((rentalCar.getDistanceValue() + rentalCarTrip.getDistanceValue()), 1);
    totalPrice = Utils.round(rentalCarTrip.getPriceValue(), 2);
  }

  /**
   * Calculates total of train travel option
   */
  public void calculateTrainTotal(RentalCar rentalCar, RentalCarTrip sourceCarTrip,
      TrainTrip trainTrip, RentalCarTrip destCarTrip) {
    totalDuration =
        Utils.round(
            (rentalCar.getDurationValue() + sourceCarTrip.getDurationValue()
                + trainTrip.getDurationValue() + destCarTrip.getDurationValue()), 2);
    totalDistance =
        Utils.round(
            (rentalCar.getDistanceValue() + sourceCarTrip.getDistanceValue()
                + trainTrip.getDistanceValue() + destCarTrip.getDistanceValue()), 1);
    totalPrice =
        Utils.round(
            (sourceCarTrip.getPriceValue() + trainTrip.getPriceValue() + destCarTrip
                .getPriceValue()),
            2);
  }

  /**
   * finds the most optimal options for a particular
   * class of customer.<br>
   * For eg - Business - Shortest Time<br>
   *          Family - Longest Time<br>
   *          Student - Cheapest<br>
   */
  static String findOptimalOption(String userClass, Trip trip) {
    RankingSystem betterOption = null;
    if (trip.trainApiSuccess && trip.carApiSuccess)
      betterOption = compare(trip.trainTravel, trip.carTravel, userClass);
    else
      return "None";
    if (betterOption.equals(trip.carTravel))
      return "Car";
    else if (betterOption.equals(trip.trainTravel))
      return "Train";
    else
      return "None";
  }

  /**
   * Compares two travel options based on the userClass
   */
  static RankingSystem compare(RankingSystem option1, RankingSystem option2, String userClass) {
    switch (userClass) {
      case "Business":
        if (option1.totalDuration <= option2.totalDuration)
          return option1;
        else
          return option2;
      case "Family":
        if (option1.totalDuration >= option2.totalDuration)
          return option1;
        else
          return option2;
      case "Student":
        if (option1.totalPrice <= option2.totalPrice)
          return option1;
        else
          return option2;
    }
    return null;
  }
}
