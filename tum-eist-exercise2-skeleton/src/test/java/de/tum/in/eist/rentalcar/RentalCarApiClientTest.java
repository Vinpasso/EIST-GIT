package de.tum.in.eist.rentalcar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.urlfetch.FakeHTTPResponse;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.gson.JsonObject;

import de.tum.in.eist.FakeURLFetchService;
import de.tum.in.eist.JsonHelper;
import de.tum.in.eist.Location;

/**
 * Tests for {@link RentalCarApiClient}.
 */
public class RentalCarApiClientTest {

  private RentalCarApiClient client;
  private FakeURLFetchService service;

  @Before
  public void setup() {
    service = new FakeURLFetchService();
    client = new RentalCarApiClient(service);
  }

  @Test
  public void getRentalCar() throws IOException {
    // Use string literal for conciseness, but parse so we know it's valid JSON.
    JsonObject json = JsonHelper.parse(
        "{\"latitude\": 10.014277,\"longitude\": 11.545284,"
        + "\"distance\": \"312.6 Meters\",\"duration\": \"7.3 Minutes\","
        + "\"typicalSeating\": 2,\"carTypeName\": \"Bike\",\"status\": \"ok\"}");

    FakeHTTPResponse response = new FakeHTTPResponse(200);
    response.setContent(json.toString());

    service.expectRequest(HTTPMethod.GET, new URL(
        "http://localhost:8080/rentalCarApi/getRentalCar/Bike/10.012345/11.54321"), response);

    RentalCar expectedResponse = new RentalCar("312.6 Meters",
        new Location(10.014277, 11.545284),"7.3 Minutes", 2, "Bike");

    RentalCar actualResponse = client.getRentalCar("Bike", new Location(10.012345, 11.54321));
    assertEquals(expectedResponse, actualResponse);
  }

  @Test(expected = IOException.class)
  public void getRentalCar_invalidJson() throws IOException {
    FakeHTTPResponse response = new FakeHTTPResponse(200);
    response.setContent("this ain't json");

    service.expectRequest(HTTPMethod.GET, new URL(
        "http://localhost:8080/rentalCarApi/getRentalCar/Bike/10.012345/11.54321"), response);

    client.getRentalCar("Bike", new Location(10.012345, 11.54321));
  }

  @Test(expected = IllegalArgumentException.class)
  public void toRentalCar_ErrorCheck_NoStatus() {
    RentalCarApiClient.toRentalCar(new JsonObject());
  }

  @Test(expected = IllegalArgumentException.class)
  public void toRentalCar_ErrorCheck_ErrorStatus() {
    JsonObject json = JsonHelper.parse("{\"status\": \"Error: Distance is greater than 500 Kms\"}");
    RentalCarApiClient.toRentalCar(json);
  }
}
