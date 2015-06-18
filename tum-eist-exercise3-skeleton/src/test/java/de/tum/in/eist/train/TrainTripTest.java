package de.tum.in.eist.train;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.JsonObject;

@RunWith(JUnit4.class)
public class TrainTripTest {

  /* Allowed delta for floating-point comparisons */
  private static final double DELTA = 0;

  @Test
  public void toJSONTest() {
    TrainTrip trainTrip =
        new TrainTrip("100.5 Meters", "5.6 Minutes", "20.50 EUR", "First Class", 1);
    JsonObject expectedJson = new JsonObject();
    expectedJson.addProperty("distance", "100.5 Meters");
    expectedJson.addProperty("duration", "5.6 Minutes");
    expectedJson.addProperty("price", "20.50 EUR");
    expectedJson.addProperty("cabin", "First Class");
    expectedJson.addProperty("seats", 1);
    assertEquals(expectedJson, trainTrip.toJSON());
  }

  @Test
  public void getDurationValue_Minutes() {
    TrainTrip trainTrip =
        new TrainTrip("100.5 Meters", "5.6 Minutes", "20.50 EUR", "First Class", 1);
    assertEquals(0.09333333333333333, trainTrip.getDurationValue(), DELTA);
  }

  @Test
  public void getDurationValue_Hours() {
    TrainTrip trainTrip = new TrainTrip("200.5 Kms", "2.11 Hours", "100.50 EUR", "First Class", 1);
    assertEquals(2.11, trainTrip.getDurationValue(), DELTA);
  }

  @Test
  public void getDistanceValue_Minutes() {
    TrainTrip trainTrip =
        new TrainTrip("100.5 Kms", "5.6 Minutes", "20.50 EUR", "First Class", 1);
    assertEquals(100.5, trainTrip.getDistanceValue(), DELTA);
  }

  @Test
  public void getDistanceValue_Hours() {
    TrainTrip trainTrip = new TrainTrip("200.5 Kms", "2.11 Hours", "100.50 EUR", "First Class", 1);
    assertEquals(200.5, trainTrip.getDistanceValue(), DELTA);
  }

  @Test
  public void getPriceValue() {
    TrainTrip trainTrip =
        new TrainTrip("100.5 Meters", "5.6 Minutes", "20.50 EUR", "First Class", 1);
    assertEquals(20.50, trainTrip.getPriceValue(), DELTA);
  }
}
