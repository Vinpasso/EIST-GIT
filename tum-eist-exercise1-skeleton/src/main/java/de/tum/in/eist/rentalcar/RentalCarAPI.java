package de.tum.in.eist.rentalcar;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.tum.in.eist.Location;
import de.tum.in.eist.Utils;

/**
 * <p>Rental Car API.<br>
 * <b>YOU ARE NOT SUPPOSED TO CHANGE THE CONTENTS OF THIS CLASS!!!</b><br>
 * To query the API synchronously, call static methods {@link #GetRentalCar(String, Location)}
 * or {@link #MakeTrip(String, Location, Location)} with the required parameters</p>
 * 
 * <p>To query MakeTrip asynchronously,<br>
 *    1. Submit a request to {@link #asyncMakeTrip(String, Location, Location)} method
 *       with the required parameters<br>
 *    2. Check if the your data is ready by isDone() method of {@link Future} interface<br>
 *    3. Get the data through get() method of {@link Future} interface and convert it to
 *       {@link RentalCarTrip} POJO using toRentalCar() method of {@link RentalCarData} class<br><p>
 * 
 * <p>API Gives following information - <br>
 * 
 *    1. To get rental car - <br>
 *       a. Coordinates of rental car<br>
 *       b. Walk distance in meters<br>
 *       c. Walk duration in minutes<br>
 *       d. Type of rental car<br>
 *       e. Typical seats in that type of rental car<br><br>
 *       
 *    2. To travel by rental car - <br>
 *       a. Distance covered by rental car in meters/kms<br>
 *       b. Driving duration in minutes/hrs<br>
 *       c. Fare in EUR</p>
 *       
 *  <p>Input Parameters - <br>
 *    String carClass = Taxi, Bike, MidsizeSUV, PremiumCar </p>
 */
/*
 * Hint - Take into consideration how many asynchronous requests you are
 *        sending to RentalCarAPI.
 * Hint - There is only one thread in RentalCarAPI, that means, asynchronous
 *        requests are processed in FIFO order.
 */
public class RentalCarAPI implements Callable<RentalCarAPI>{
  
  private String carClass;
  private Location origin;
  private Location destination;
  public RentalCarData trip;
  private final static ExecutorService executor = Executors.newFixedThreadPool(1);
  
  public RentalCarAPI(String carClass, Location origin, Location destination){
    this.carClass=carClass;
    this.origin = origin;
    this.destination=destination;
  }
  
  /** 
   * Use this API call to get a rental Car at source Location
   * @return the information of Rental Car in {@link RentalCar} POJO
   */
  /*
   * Hint - Use the POJO returned by this API call throughout the code. Eg - In RankingSystem
   */
  public static RentalCar GetRentalCar(String carClass, Location origin)
      throws IllegalArgumentException {
    if(!carClass.equals("Bike")&&!carClass.equals("PremiumCar")&&!carClass.equals("MidsizeSUV")&&!carClass.equals("Taxi"))
      getError("Invalid carClass "+ carClass);
    Random r = new Random();
    double dispFactorLat = Utils.round((r.nextDouble() + r.nextInt(2) + 1) / 1000, 6);
    double dispFactorLong = Utils.round((r.nextDouble() + r.nextInt(2) + 1) / 1000, 6);
    Location carLoc =
        new Location(Utils.round(origin.getLatitude() + dispFactorLat, 6), Utils.round(
            origin.getLongitude() + dispFactorLong, 6));
    double distance = Utils.round(Utils.findDistance(origin, carLoc), 1);
    RentalCarData trip = new RentalCarData(carClass, distance, "Walk");
    trip.calculate();
    return new RentalCar(
        trip.distance + " " + trip.distanceUnit,
        carLoc,
        trip.duration + " " + trip.durationUnit,
        trip.typicalSeating,
        trip.carClass);
  }

  /**
   * Use this API call to travel by rental car you obtained in above GetRentalCar API call.
   * @return the information of Rental Car trip in {@link RentalCarTrip} POJO
   */ 
  /*
   * Hint - Use the POJO returned by this API call throughout the code. Eg - In RankingSystem
   */
  public static RentalCarTrip MakeTrip(String carClass, Location origin, Location destination) {
    if(!carClass.equals("Bike")&&!carClass.equals("PremiumCar")&&!carClass.equals("MidsizeSUV")&&!carClass.equals("Taxi"))
      getError("Invalid carClass "+ carClass);
    double distance = Utils.round(Utils.findDistance(origin, destination), 1);
    RentalCarData trip = new RentalCarData(carClass, distance, "Vehicle");
    trip.calculate();
    if (distance > 500000)
      getError("Distance is greater than 500 Kms");
    return new RentalCarTrip(
        trip.distance + " " + trip.distanceUnit,
        trip.duration + " " + trip.durationUnit,
        trip.totalPrice + " EUR",
        trip.carClass);
  }
  
  /**
   * This is an alternative to the above {@link #MakeTrip(String, Location, Location)} in asynchronous way.
   * It doesn't return a {@link RentalCarTrip} object, instead it schedules the 
   * request to be processed by executor service.
   * 
   * The status of request could be checked at the client side through
   * isDone() method of {@link Future} Interface
   * 
   * If your response is ready, you can invoke get().trip.toRentalCar() 
   * on futureObject to get {@link RentalCarTrip} POJO containing information
   */
  /*
   * Hint - Try to print something on console while your request is
   *        being processed asynchronously.
   */
  public static Future<RentalCarAPI> asyncMakeTrip(String carClass, Location origin, Location destination){
    Callable<RentalCarAPI> asyncMakeTrip = new RentalCarAPI(carClass,origin,destination);
    Future<RentalCarAPI> futureCarTrip;
    futureCarTrip = executor.submit(asyncMakeTrip);
    return futureCarTrip;
  }

  static void getError(String error) {
    throw new IllegalArgumentException("Rental Car API error: " + error);
  }

  @Override
  public RentalCarAPI call() throws Exception {
    if(!this.carClass.equals("Bike")&&!this.carClass.equals("PremiumCar")&&!this.carClass.equals("MidsizeSUV")&&!this.carClass.equals("Taxi"))
      getError("Invalid carClass "+ this.carClass);
    double distance = Utils.round(Utils.findDistance(this.origin, this.destination), 1);
    this.trip = new RentalCarData(this.carClass, distance, "Vehicle");
    this.trip.calculate();
    Thread.sleep(10);
    if (distance > 500000)
      getError("Distance is greater than 500 Kms");
    return this;
  }

}
