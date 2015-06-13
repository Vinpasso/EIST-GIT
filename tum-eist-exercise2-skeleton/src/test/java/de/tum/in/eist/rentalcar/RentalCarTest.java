package de.tum.in.eist.rentalcar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.JsonObject;

import de.tum.in.eist.Location;

@RunWith(JUnit4.class)
public class RentalCarTest {

  /* Allowed delta for floating-point comparisons */
  private static final double DELTA = 0;

  @Test
  public void toJSON() {
    JsonObject coordinates = new JsonObject();
    coordinates.addProperty("latitude", 10.0);
    coordinates.addProperty("longitude", 20.0);

    JsonObject expectedJson = new JsonObject();
    expectedJson.addProperty("distance", "100 Meters");
    expectedJson.add("coordinates", coordinates);
    expectedJson.addProperty("duration", "5 Minutes");
    expectedJson.addProperty("seats", 2);
    expectedJson.addProperty("type", "Bike");

    RentalCar rentalCar = new RentalCar("100 Meters", new Location(10, 20), "5 Minutes", 2, "Bike");
    assertEquals(expectedJson, rentalCar.toJSON());
  }

  @Test
  public void getDurationValue_Minutes() {
    RentalCar rentalCar = new RentalCar("100 Meters", new Location(10, 10), "5 Minutes", 2, "Bike");
    assertEquals(0.08333333333333333, rentalCar.getDurationValue(), DELTA);
  }

  @Test
  public void getDurationValue_Hours() {
    RentalCar rentalCar = new RentalCar("200 Kms", new Location(10, 10), "2 Hours", 2, "Bike");
    assertEquals(2.0, rentalCar.getDurationValue(), DELTA);
  }

  @Test
  public void getDistanceValue_Minutes() {
    RentalCar rentalCar = new RentalCar("100 Meters", new Location(10, 10), "5 Minutes", 2, "Bike");
    assertEquals(0.1, rentalCar.getDistanceValue(), DELTA);
  }

  @Test
  public void getDistanceValue_Hours() {
    RentalCar rentalCar = new RentalCar("200 Kms", new Location(10, 10), "2 Hours", 2, "Bike");
    assertEquals(200.0, rentalCar.getDistanceValue(), DELTA);
  }
}
