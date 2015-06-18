package de.tum.in.eist.rentalcar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.JsonObject;

@RunWith(JUnit4.class)
public class RentalCarTripTest {

  /* Allowed delta for floating-point comparisons */
  private static final double DELTA = 0;

  @Test
  public void toJSONTest() {
    RentalCarTrip rentalCarTrip =
        new RentalCarTrip("100.5 Meters", "5.6 Minutes", "20.50 EUR", "Bike");
    JsonObject expectedJson = new JsonObject();
    expectedJson.addProperty("distance", "100.5 Meters");
    expectedJson.addProperty("duration", "5.6 Minutes");
    expectedJson.addProperty("price", "20.50 EUR");
    expectedJson.addProperty("carClass", "Bike");
    assertEquals(expectedJson, rentalCarTrip.toJSON());
  }

  @Test
  public void getDurationValue_Minutes() {
    RentalCarTrip rentalCarTrip =
        new RentalCarTrip("100.5 Meters", "5.6 Minutes", "20.50 EUR", "Bike");
    assertEquals(0.09333333333333333, rentalCarTrip.getDurationValue(), DELTA);
  }

  @Test
  public void getDurationValue_Hours() {
    RentalCarTrip rentalCarTrip = new RentalCarTrip("200.5 Kms", "2.11 Hours", "80.50 EUR", "Bike");
    assertEquals(2.11, rentalCarTrip.getDurationValue(), DELTA);
  }

  @Test
  public void getDistanceValue_Minutes() {
    RentalCarTrip rentalCarTrip =
        new RentalCarTrip("100.5 Meters", "5.6 Minutes", "20.50 EUR", "Bike");
    assertEquals(0.1005, rentalCarTrip.getDistanceValue(), DELTA);
  }

  @Test
  public void getDistanceValue_Hours() {
    RentalCarTrip rentalCarTrip = new RentalCarTrip("200.5 Kms", "2.11 Hours", "80.50 EUR", "Bike");
    assertEquals(200.5, rentalCarTrip.getDistanceValue(), DELTA);
  }

  @Test
  public void getPriceValue() {
    RentalCarTrip rentalCarTrip =
        new RentalCarTrip("100.5 Meters", "5.6 Minutes", "20.50 EUR", "Bike");
    assertEquals(20.50, rentalCarTrip.getPriceValue(), DELTA);
  }
}
