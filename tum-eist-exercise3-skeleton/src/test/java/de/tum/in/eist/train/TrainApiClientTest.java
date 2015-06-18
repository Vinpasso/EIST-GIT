package de.tum.in.eist.train;

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
 * Tests for {@link TrainApiClient}.
 */
public class TrainApiClientTest {

  private TrainApiClient client;
  private FakeURLFetchService service;

  @Before
  public void setup() {
    service = new FakeURLFetchService();
    client = new TrainApiClient(service);
  }

  @Test
  public void trainTrip() throws IOException {
    // Use string literal for conciseness, but parse so we know it's valid JSON.
    JsonObject json = JsonHelper.parse("{\"distance\": \"467.3 Kms\",\"duration\": \"2.34 Hours\","
        + "\"totalPrice\": \"542.07 EUR\",\"seats\": 2,"
        + "\"cabin\": \"FirstClass\",\"status\": \"ok\"}");

    FakeHTTPResponse response = new FakeHTTPResponse(200);
    response.setContent(json.toString());

    service.expectRequest(HTTPMethod.GET, new URL("http://tum-train-api.appspot.com"
        + "/makeTrip/FirstClass/2/10.012345/11.54321/13.014277/14.545284"), response);

    TrainTrip expectedResponse =
        new TrainTrip("467.3 Kms", "2.34 Hours", "542.07 EUR", "FirstClass", 2);

    TrainTrip actualResponse = client.trainTrip("Business", 2, new Location(10.012345, 11.54321),
        new Location(13.014277, 14.545284));
    assertEquals(expectedResponse, actualResponse);
  }

  @Test(expected = IOException.class)
  public void trainTrip_invalidJson() throws IOException {
    FakeHTTPResponse response = new FakeHTTPResponse(200);
    response.setContent("this ain't json");

    service.expectRequest(HTTPMethod.GET, new URL("http://tum-train-api.appspot.com"
        + "/makeTrip/FirstClass/2/10.012345/11.54321/13.014277/14.545284"), response);

    client.trainTrip("Business", 2, new Location(10.012345, 11.54321),
        new Location(13.014277, 14.545284));
  }

  @Test(expected = IllegalArgumentException.class)
  public void toTrainTrip_ErrorCheck_NoStatus() {
    TrainApiClient.toTrainTrip(new JsonObject());
  }

  @Test(expected = IllegalArgumentException.class)
  public void toRentalCar_ErrorCheck_ErrorStatus() {
    JsonObject json =
        JsonHelper.parse("{\"status\": \"Error: Distance is greater than 1000 Kms\"}");
    TrainApiClient.toTrainTrip(json);
  }
}
