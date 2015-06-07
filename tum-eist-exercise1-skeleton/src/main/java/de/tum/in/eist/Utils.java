package de.tum.in.eist;

import java.util.Random;

/**
 * This class contain utility functions
 * that are required by our app.<br>
 * <b>YOU ARE NOT SUPPOSED TO CHANGE THE (GIVEN) CONTENTS OF THIS CLASS!!!</b><br>
 * However you can add more utility methods in this class. <br>
 */
public class Utils {
  
  /* 
   * Radius of the earth in meters. 
   */
  private static final int EARTH_RADIUS_M = 6371000;
  
  /**
   * Rounds up a double value to fixed decimal places.
   */
  //We used this several times in RankingSystem.
  public static double round(double value, int places) {
    if (places < 0)
      throw new IllegalArgumentException();
    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
  }
  
  /**
   * @return the distance in meters
   * between 2 locations
   */
  public static double findDistance(Location initialLoc, Location finalLoc) {
    double dLat = Math.toRadians(finalLoc.getLatitude() - initialLoc.getLatitude());
    double dLon = Math.toRadians(finalLoc.getLongitude() - initialLoc.getLongitude());
    double lat1 = Math.toRadians(initialLoc.getLatitude());
    double lat2 = Math.toRadians(finalLoc.getLatitude());
    double a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2)
            * Math.cos(lat1) * Math.cos(lat2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return EARTH_RADIUS_M * c;
  }

  /**
   * @return coordinates of a train station near queried location
   */
  /*
   * Hint - Use it to get the coordinates of source Train station
   * and destination train station before querying TrainAPI
   */
  public static Location getTrainStation(Location sourceLoc) {
    Random r = new Random();
    double dispFactorLat = Utils.round((r.nextDouble() + r.nextInt(6) + 1) / 100, 6);
    double dispFactorLong = Utils.round((r.nextDouble() + r.nextInt(6) + 1) / 100, 6);
    return new Location(Utils.round(sourceLoc.getLatitude() + dispFactorLat, 6), Utils.round(
            sourceLoc.getLongitude() + dispFactorLong, 6));
  }
}
