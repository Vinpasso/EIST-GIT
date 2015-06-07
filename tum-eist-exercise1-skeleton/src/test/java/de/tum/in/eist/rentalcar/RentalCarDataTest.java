package de.tum.in.eist.rentalcar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.tum.in.eist.rentalcar.RentalCarData;

@RunWith(JUnit4.class)
public class RentalCarDataTest {
  
  /* Allowed delta for floating-point comparisons */
  private static final double DELTA = 0;

  @Test
  public void calculate_invalidCarClass() {
    RentalCarData rentalCar = new RentalCarData("Invalid", 100.00, "Walk");
    rentalCar.calculate();
    assertEquals(0.0,rentalCar.totalPrice,DELTA);
    assertEquals(0,rentalCar.typicalSeating,DELTA);
  }

  @Test
  public void calculate_invalidModeIsWalk() {
    RentalCarData rentalCar1 = new RentalCarData("Bike", 40000.00, "Invalid");
    rentalCar1.calculate();
    RentalCarData rentalCar2 = new RentalCarData("Bike", 40000.00, "Walk");
    rentalCar2.calculate();
    assertEquals(rentalCar1.totalPrice,rentalCar2.totalPrice,DELTA);
    assertEquals(rentalCar1.duration,rentalCar2.duration,DELTA);
    assertEquals(rentalCar1.durationUnit,rentalCar2.durationUnit);
    assertEquals(rentalCar1.distance,rentalCar2.distance,DELTA);
  }

  @Test
  public void calculate_negativeDistance() {
    RentalCarData rentalCarTrip = new RentalCarData("Bike", -100.00, "Vehicle");
    rentalCarTrip.calculate();
    assertTrue(rentalCarTrip.totalPrice<=10.0);
    assertTrue(rentalCarTrip.duration<=12.0);
  }

  @Test
  public void calculate_zeroDistance() {
    RentalCarData rentalCarTrip = new RentalCarData("Bike", 0.0, "Vehicle");
    rentalCarTrip.calculate();
    assertEquals(10.0,rentalCarTrip.totalPrice,DELTA);
    assertEquals(12.0,rentalCarTrip.duration,DELTA);
  }

}
