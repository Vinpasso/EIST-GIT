package de.tum.in.eist;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import de.tum.in.eist.rentalcar.RentalCarAPI;
import de.tum.in.eist.rentalcar.RentalCarTrip;
import de.tum.in.eist.train.TrainAPI;

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
	  
	  //1
		Location startLoc = null;
		Location endLoc = null;
		int userTypInt = -1; // -1 No UserTyp, 0 Geschäftskunde, 1 Student, 2
								// Familie
		int travlers = 1; // Standart is 1 travler, but a family can travel with
							// more persons together
		Scanner scanner = new Scanner(System.in); // reading Usertyp, the start
													// and end Location
		System.out.println("Welcome to EIST Travel APP\n Please state your User Typ (Geschäftskunde, Student, Familie)");
		while (userTypInt == -1) {
			String userTyp;
			userTyp = scanner.next();
			userTyp = userTyp.toLowerCase();
			switch (userTyp) {
			case "geschäftskunde":
				userTypInt = 0;
				break;
			case "geschaeftskunde":
				userTypInt = 0;
				break;
			case "student":
				userTypInt = 1;
				break;
			case "familie":
				userTypInt = 2;
				break;
			default:
				System.out.println("Sry Dave, i can't do that\nPlease state your User Typ (Geschäftskunde, Student, Familie)");
				break;
			}
		}
		if (userTypInt == 2) {
			System.out
					.println("With how many family Members do you wish to travel?");
			while (true) {
				try {
					travlers = Integer.parseInt(scanner.next());
					break;
				} catch (NumberFormatException e) {
					System.out.println("With how many family Members do you wish to travel? Please insert a Number");
				}
			}
		}
		
		System.out.println("What is your starting Location?");
		startLoc = getLocation(scanner);
		System.out.println("Where do you wish to travel to?");
		endLoc = getLocation(scanner);
		switch (userTypInt) {
		case 0:
			System.out.println("You travel as a businessman and want to go from "+startLoc.getLatitude()+"-"+startLoc.getLongitude()+" to "+endLoc.getLatitude()+"-"+endLoc.getLongitude());
			break;
		case 1:
			System.out.println("You travel as a student and want to go from "+startLoc.getLatitude()+"-"+startLoc.getLongitude()+" to "+endLoc.getLatitude()+"-"+endLoc.getLongitude());
			break;
		case 2:
			System.out.println("You travel as a family with "+travlers+" Members and want to go from "+startLoc.getLatitude()+"-"+startLoc.getLongitude()+" to "+endLoc.getLatitude()+"-"+endLoc.getLongitude());
			break;
		default:
			break;
		}
		
		/*
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
    */
    /* Hint - if distance < 500 kms, user can go travel
    *         by rental car or by train.
    *         if 500 < distance < 1000, user can only
    *         travel by train.
    *         if distance > 1000 kms, then no travel option
    *         is possible
    */  
		
  }

	private static Location getLocation(Scanner scanner) {
		Location tempLoc = null;
		double latitude = -1;
		double longitude = -1;
		while (latitude == -1) {
			try {
				System.out.println("Latitude:");
				latitude = Double.parseDouble(scanner.next());
			} catch (NumberFormatException e) {
				System.out.println("error");
			}
		}
		while (longitude == -1) {
			try {
				System.out.println("Longtitude:");
				longitude = Double.parseDouble(scanner.next());
			} catch (NumberFormatException e) {
				System.out.println("error");
			}
		}
		try {
			tempLoc = new Location(latitude, longitude);
		} catch (IllegalArgumentException e) {
			System.out.println("I don't know this place");
		}

		return tempLoc;
	}
  
  /**
   * Maps user class with car class<br>
   * Student - Bike<br>
   * Business - PremiumCar<br>
   * Family - MidsizeSUV
   */
   
     private static double calculateDistance(Location origin, Location destination)
  {
	  return Utils.findDistance(origin, destination);
  }
  
  private static Trip calculateTripOptions(String userClass, int passengers, Location origin, Location destination)
  {
	  double distance = calculateDistance(origin, destination);
	  Trip trip = new Trip();
	  if(distance < 500)
	  {
		  trip.rentalCarTrip = RentalCarAPI.MakeTrip(getCarClass(userClass), origin, destination);
	  }
	  if(distance < 1000)
	  {
		  Location originStation = Utils.getTrainStation(origin);
		  Location destinationStation = Utils.getTrainStation(destination);
		  trip.sourceCarTrip = RentalCarAPI.MakeTrip(getCarClass(userClass), origin, originStation);
		  trip.trainTrip = TrainAPI.MakeTrip(getCabinClass(userClass), passengers, originStation, destinationStation);
		  trip.destCarTrip = RentalCarAPI.MakeTrip(getCarClass(userClass), destinationStation, destination);
	  }
	  return trip;
  }

  private static RentalCarTrip synchronousRentalCarMakeTrip(String carClass, Location origin, Location destination)
  {
	  return RentalCarAPI.MakeTrip(carClass, origin, destination);
  }
  
  private static void asynchronousRentalCarMakeTrip(String carClass, Location origin, Location destination, final RentalCarTripListener listener)
  {
	  final Future<RentalCarAPI> future = RentalCarAPI.asyncMakeTrip(carClass, origin, destination);
	  new Thread(new Runnable() {
		
		@Override
		public void run() {
			try {
				listener.tripComputed(future.get().trip.toRentalCar());
			} catch (InterruptedException | ExecutionException e) {
				listener.computationFailed(e);
			}
		}
	}).start();
  }
  
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
