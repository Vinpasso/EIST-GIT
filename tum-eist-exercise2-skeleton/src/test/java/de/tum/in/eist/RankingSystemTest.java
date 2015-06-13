package de.tum.in.eist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import de.tum.in.eist.rentalcar.RentalCar;
import de.tum.in.eist.rentalcar.RentalCarTrip;
import de.tum.in.eist.train.TrainTrip;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RankingSystemTest {

  /* Allowed delta for floating-point comparisons */
  private static final double DELTA = 0;

  @Test
  public void calculateCarTotal() {
    RentalCarTrip rentalCarTrip = new RentalCarTrip("100.5 Meters", "5.6 Minutes", "20.50 EUR", "Bike");
    RentalCar rentalCar = new RentalCar("100 Meters", new Location(10, 10), "5 Minutes", 2, "Bike");
    RankingSystem carTripTotal = new RankingSystem();
    carTripTotal.calculateCarTotal(rentalCar, rentalCarTrip);
    assertEquals(0.2, carTripTotal.totalDistance, DELTA);
    assertEquals(0.18, carTripTotal.totalDuration, DELTA);
    assertEquals(20.50, carTripTotal.totalPrice, DELTA);
  }

  @Test
  public void calculateTrainTotal() {
    RentalCarTrip sourceCarTrip = new RentalCarTrip("100.5 Meters", "5.6 Minutes", "20.50 EUR", "Bike");
    RentalCar rentalCar = new RentalCar("100 Meters", new Location(10, 10), "5 Minutes", 2, "Bike");
    RentalCarTrip destCarTrip = new RentalCarTrip("78.3 Kms", "1.2 Hours", "20.50 EUR", "Bike");
    TrainTrip trainTrip = new TrainTrip("200.5 Kms", "2.11 Hours", "100.50 EUR", "First Class", 1);
    RankingSystem trainTripTotal = new RankingSystem();
    trainTripTotal.calculateTrainTotal(rentalCar, sourceCarTrip, trainTrip, destCarTrip);
    assertEquals(279.0, trainTripTotal.totalDistance, DELTA);
    assertEquals(3.49, trainTripTotal.totalDuration, DELTA);
    assertEquals(141.50, trainTripTotal.totalPrice, DELTA);
  }

  @Test
  public void findOptimalOption_Student() {
    Trip trip = new Trip();
    trip.carApiSuccess = true;
    trip.carTravel.totalDuration = 50;
    trip.carTravel.totalPrice = 100;
    trip.trainApiSuccess = true;
    trip.trainTravel.totalDuration = 60;
    trip.trainTravel.totalPrice = 120;
    assertEquals("Car", RankingSystem.findOptimalOption("Student", trip));
  }

  @Test
  public void findOptimalOption_Business() {
    Trip trip = new Trip();
    trip.carApiSuccess = true;
    trip.carTravel.totalDuration = 50;
    trip.carTravel.totalPrice = 100;
    trip.trainApiSuccess = true;
    trip.trainTravel.totalDuration = 60;
    trip.trainTravel.totalPrice = 120;
    assertEquals("Car", RankingSystem.findOptimalOption("Business", trip));
  }

  @Test
  public void findOptimalOption_Family() {
    Trip trip = new Trip();
    trip.carApiSuccess = true;
    trip.carTravel.totalDuration = 50;
    trip.carTravel.totalPrice = 100;
    trip.trainApiSuccess = true;
    trip.trainTravel.totalDuration = 60;
    trip.trainTravel.totalPrice = 120;
    assertEquals("Train", RankingSystem.findOptimalOption("Family", trip));
  }

  @Test(expected = NullPointerException.class)
  public void findMostOptimalOption_NullUserClass() {
    Trip trip = new Trip();
    trip.carApiSuccess = true;
    trip.carTravel.totalDuration = 50;
    trip.carTravel.totalPrice = 100;
    trip.trainApiSuccess = true;
    trip.trainTravel.totalDuration = 60;
    trip.trainTravel.totalPrice = 120;
    RankingSystem.findOptimalOption(null, trip);
  }

  @Test
  public void findOptimalOption_NoApiSuccess() {
    Trip trip = new Trip();
    assertEquals("None", RankingSystem.findOptimalOption("Student", trip));
  }

  @Test
  public void findOptimalOption_OneApiSuccess() {
    Trip trip = new Trip();
    trip.carApiSuccess = true;
    assertEquals("None", RankingSystem.findOptimalOption("Student", trip));
  }
  
  @Test
  public void compare_invalidUserClass() {
    RankingSystem option1 = new RankingSystem();
    RankingSystem option2 = new RankingSystem();
    assertNull(RankingSystem.compare(option1, option2, "invalidUserClass"));
  }

  @Test(expected = NullPointerException.class)
  public void compare_NullUserClass() {
    RankingSystem option1 = new RankingSystem();
    RankingSystem option2 = new RankingSystem();
    RankingSystem.compare(option1, option2, null);
  }

  @Test
  public void compare_Student() {
    RankingSystem option1 = new RankingSystem();
    option1.totalPrice = 20.78;
    RankingSystem option2 = new RankingSystem();
    option2.totalPrice = 20.77;
    assertEquals(option2, RankingSystem.compare(option1, option2, "Student"));
  }

  @Test
  public void compare_Business() {
    RankingSystem option1 = new RankingSystem();
    option1.totalDuration = 20.7;
    RankingSystem option2 = new RankingSystem();
    option2.totalDuration = 20.7;
    assertEquals(option1, RankingSystem.compare(option1, option2, "Business"));
  }

  @Test
  public void compare_Family() {
    RankingSystem option1 = new RankingSystem();
    option1.totalDuration = 0.2;
    RankingSystem option2 = new RankingSystem();
    option2.totalDuration = 10;
    assertEquals(option2, RankingSystem.compare(option1, option2, "Family"));
  }

}
