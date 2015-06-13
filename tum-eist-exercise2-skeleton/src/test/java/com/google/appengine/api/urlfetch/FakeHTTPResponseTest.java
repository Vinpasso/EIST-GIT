package com.google.appengine.api.urlfetch;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Collections;

/**
 * Tests for {@link FakeHTTPResponse}.
 */
public class FakeHTTPResponseTest {

  @Test
  public void responseCode() {
    FakeHTTPResponse response = new FakeHTTPResponse(200);
    assertEquals(200, response.getResponseCode());
  }

  @Test
  public void content() {
    byte[] expectedContent = new byte[] { 0xC, 0xA, 0xF, 0xE };
    FakeHTTPResponse response = new FakeHTTPResponse(200);
    response.setContent(expectedContent);
    assertArrayEquals(expectedContent, response.getContent());
  }

  @Test
  public void addHeader() {
    FakeHTTPResponse response = new FakeHTTPResponse(200);
    response.addHeader("name", "value");

    assertEquals(1, response.getHeaders().size());
    HTTPHeader header = response.getHeaders().get(0);
    assertEquals("name", header.getName());
    assertEquals("value", header.getValue());
  }

  @Test
  public void addHeader_empty() {
    FakeHTTPResponse response = new FakeHTTPResponse(200);
    assertEquals(Collections.emptyList(), response.getHeaders());
  }
}
