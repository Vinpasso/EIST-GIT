package de.tum.in.eist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import org.junit.Test;

import com.google.appengine.api.urlfetch.FakeHTTPResponse;

/**
 * Tests for {@link URLFetchServiceHelper}.
 */
public class URLFetchServiceHelperTest {

  @Test
  public void toString_fullContentType() {
    FakeHTTPResponse response = new FakeHTTPResponse(200);
    response.setContent("knödelsuppe".getBytes(StandardCharsets.ISO_8859_1));
    response.addHeader("Content-Type", "foo/bar; charset=ISO-8859-1");

    assertEquals("knödelsuppe", URLFetchServiceHelper.toString(response));
  }

  @Test
  public void toString_contentTypeWithoutCharset() {
    FakeHTTPResponse response = new FakeHTTPResponse(200);
    response.setContent("knödelsuppe".getBytes(StandardCharsets.UTF_8));
    response.addHeader("Content-Type", "foo/bar"); // no charset

    assertEquals("knödelsuppe", URLFetchServiceHelper.toString(response));
  }

  @Test
  public void toString_withoutContentType() {
    FakeHTTPResponse response = new FakeHTTPResponse(200);
    response.setContent("knödelsuppe".getBytes(StandardCharsets.UTF_8));

    // should default to UTF-8
    assertEquals("knödelsuppe", URLFetchServiceHelper.toString(response));
  }

  @Test
  public void toString_charsetMismatch() {
    FakeHTTPResponse response = new FakeHTTPResponse(200);
    response.setContent("knödelsuppe".getBytes(StandardCharsets.ISO_8859_1));
    response.addHeader("Content-Type", "foo/bar; charset=ISO-8859-1");

    assertEquals("knödelsuppe", URLFetchServiceHelper.toString(response));
  }

  @Test
  public void getHeader() {
    FakeHTTPResponse response = new FakeHTTPResponse(200);
    response.addHeader("foo", "foofoo");
    response.addHeader("bar", "barbar");

    String header = URLFetchServiceHelper.getHeader(response, "foo");
    assertEquals("foofoo", header);
  }

  @Test
  public void getHeader_noHeaders() {
    FakeHTTPResponse response = new FakeHTTPResponse(200);

    String header = URLFetchServiceHelper.getHeader(response, "foo");
    assertNull(header);
  }

  @Test
  public void getHeader_returnsOnlyFirst() {
    FakeHTTPResponse response = new FakeHTTPResponse(200);
    response.addHeader("foo", "one");
    response.addHeader("foo", "two"); // should not be returned

    String header = URLFetchServiceHelper.getHeader(response, "foo");
    assertEquals("one", header);
  }

  @Test
  public void getCharset() {
    Charset charset = URLFetchServiceHelper.getCharset("application/json; charset=UTF-8");
    assertEquals(StandardCharsets.UTF_8, charset);
  }

  @Test
  public void getCharset_null() {
    Charset charset = URLFetchServiceHelper.getCharset(null);
    assertNull(charset);
  }

  @Test
  public void getCharset_noCharset() {
    Charset charset = URLFetchServiceHelper.getCharset("application/json");
    assertNull(charset);
  }

  @Test(expected = UnsupportedCharsetException.class)
  public void getCharset_invalidCharset() {
    Charset charset = URLFetchServiceHelper.getCharset("application/json; charset=doesntexist");
    assertNull(charset);
  }
}
