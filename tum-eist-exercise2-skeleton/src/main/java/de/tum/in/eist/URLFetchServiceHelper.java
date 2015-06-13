package de.tum.in.eist;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPResponse;

public class URLFetchServiceHelper {

  private static final String CHARSET_KEY = "charset=";

  /**
   * Converts the content of a response into a string.
   */
  public static String toString(HTTPResponse response) {
    String contentType = getHeader(response, "Content-Type");
    Charset charset = getCharset(contentType);
    return new String(response.getContent(), charset == null ? StandardCharsets.UTF_8 : charset);
  }

  static Charset getCharset(String contentType) {
    if (contentType == null) {
      return null;
    }
    // Format of Content-Type header is "application/json; charset=UTF-8".
    int charsetIndex = contentType.indexOf(CHARSET_KEY);
    if (charsetIndex == -1) {
      return null;
    }
    return Charset.forName(contentType.substring(charsetIndex + CHARSET_KEY.length()));
  }

  /**
   * Returns the value of the first header with a given name, or {@code null} if there is no header
   * with that name.
   */
  public static String getHeader(HTTPResponse response, String name) {
    for (HTTPHeader header : response.getHeadersUncombined()) {
      if (header.getName().equals(name)) {
        return header.getValue();
      }
    }
    return null;
  }
}
