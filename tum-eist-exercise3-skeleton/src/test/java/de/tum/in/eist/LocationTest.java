package de.tum.in.eist;

import static org.junit.Assert.assertEquals;

import com.google.gson.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LocationTest {

  @Test
  public void toJSONTest() {
    Location location = new Location(10.1212, 11.121212);
    JsonObject expectedJson = new JsonObject();
    expectedJson.addProperty("latitude", 10.1212);
    expectedJson.addProperty("longitude", 11.121212);
    assertEquals(expectedJson, location.toJSON());
  }
}
