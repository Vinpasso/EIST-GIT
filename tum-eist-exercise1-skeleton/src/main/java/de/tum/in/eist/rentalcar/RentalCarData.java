package de.tum.in.eist.rentalcar;

import de.tum.in.eist.Utils;

/**
 * This class is used by Rental Car API<br>
 * <b>YOU ARE NOT SUPPOSED TO CHANGE THE CONTENTS OF THIS CLASS!!!</b>
 */
public class RentalCarData {
  String carClass;
  double distance;
  double duration;
  double totalPrice;
  String durationUnit;
  int typicalSeating;
  String distanceUnit;
  String mode;

  public RentalCarData(String carClass, double distance, String mode) {
    this.carClass = carClass;
    this.distance = distance;
    this.mode = mode;
  }
  
  /**
   * @return data of rental car trip in {@link RentalCarTrip} POJO
   */
  public RentalCarTrip toRentalCar() {
    return new RentalCarTrip(
        distance + " " + distanceUnit,
        duration + " " + durationUnit,
        totalPrice + " EUR",
        carClass);
  }

  public void calculate() {
    switch (carClass) {
      case "Bike":
        totalPrice = 10 + 0.0003 * distance;
        duration = 0.2 + 0.000012 * distance;
        typicalSeating = 2;
        break;
      case "PremiumCar":
        totalPrice = 25 + 0.0004 * distance;
        duration = 0.2 + 0.00001 * distance;
        typicalSeating = 2;
        break;
      case "MidsizeSUV":
        totalPrice = 20 + 0.0005 * distance;
        duration = 0.2 + 0.000014 * distance;
        typicalSeating = 4;
        break;
      case "Taxi":
        totalPrice = 5 + 0.00028 * distance;
        duration = 0.2 + 0.000013 * distance;
        typicalSeating = 4;
        break;
    }
    if (mode.equals("Vehicle")) {
      if (duration < 1) {
        duration = Utils.round(duration * 60, 0);
        durationUnit = "Minutes";
      } else {
        duration = Utils.round(duration, 2);
        durationUnit = "Hours";
      }
    } else {
      duration = Utils.round(distance * 0.02333333, 1);
      durationUnit = "Minutes";
    }
    if (distance > 1000) {
      distance = Utils.round(distance / 1000, 1);
      distanceUnit="Kms";
    } else {
      distanceUnit="Meters";
    }
    totalPrice = Utils.round(totalPrice, 2);
  }
}
