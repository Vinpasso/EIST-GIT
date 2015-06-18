package de.tum.in.eist;

import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.urlfetch.FakeHTTPResponse;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;

/**
 * Tests for {@link FakeURLFetchService}.
 */
public class FakeURLFetchServiceTest {

  private FakeURLFetchService service;

  @Before
  public void setup() {
    service = new FakeURLFetchService();
  }

  @Test
  public void fetch_url() throws IOException {
    HTTPResponse expectedResponse = new FakeHTTPResponse(200);
    service.expectRequest(HTTPMethod.GET, new URL("http://www.tum.de"), expectedResponse);

    HTTPResponse actualResponse = service.fetch(new URL("http://www.tum.de"));
    // HTTPResponse doesn't implement equals(), so check for sameness, not equality.
    assertSame(expectedResponse, actualResponse);
  }

  @Test
  public void fetch_request() throws IOException {
    HTTPResponse expectedResponse = new FakeHTTPResponse(200);
    service.expectRequest(HTTPMethod.POST, new URL("http://www.tum.de"), expectedResponse);

    HTTPRequest request = new HTTPRequest(new URL("http://www.tum.de"), HTTPMethod.POST);
    HTTPResponse actualResponse = service.fetch(request);
    // HTTPResponse doesn't implement equals(), so check for sameness, not equality.
    assertSame(expectedResponse, actualResponse);
  }

  @Test(expected = IllegalStateException.class)
  public void fetch_nothingExpected() throws IOException {
    service.fetch(new URL("http://www.tum.de"));
  }

  @Test(expected = IllegalStateException.class)
  public void fetch_differentMethodExpected() throws IOException {
    service.expectRequest(HTTPMethod.POST, new URL("http://www.tum.de"), new FakeHTTPResponse(200));

    service.fetch(new URL("http://www.tum.de")); // uses GET method
  }
}
