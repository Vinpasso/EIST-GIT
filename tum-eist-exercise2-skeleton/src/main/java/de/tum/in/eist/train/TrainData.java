package de.tum.in.eist.train;

import de.tum.in.eist.Utils;

/**
 * This class is used by Train API<br>
 * <b>YOU ARE NOT SUPPOSED TO CHANGE THE CONTENTS OF THIS CLASS!!!</b>
 */
public class TrainData {

  double distance;
  int seats;
  double duration;
  double totalPrice;
  String cabinType;
  String durationUnit;

  public TrainData(String cabinClass, double distance, int seats) {
    this.cabinType = cabinClass;
    this.distance = distance;
    this.seats = seats;
  }

  void calculate() {
    switch (cabinType) {
      case "ThirdClass":
        totalPrice = 0.00028 * distance;
        break;
      case "FirstClass":
        totalPrice = 0.00058 * distance;
        break;
      case "Economy":
        totalPrice = 0.00038 * distance;
    }

    duration = 0.000009 * distance;
    if (duration < 1) {
      duration = Utils.round(duration * 60, 0);
      durationUnit = "Minutes";
    } else {
      duration = Utils.round(duration, 2);
      durationUnit = "Hours";
    }
    distance = Utils.round((distance / 1000), 1);
    totalPrice = Utils.round(totalPrice * seats, 2);
  }
}
