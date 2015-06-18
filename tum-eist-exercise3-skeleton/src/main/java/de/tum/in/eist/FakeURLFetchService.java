package de.tum.in.eist;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;

import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.common.base.MoreObjects;

/**
 * A fake implementation of {@link URLFetchService} to use in tests.
 *
 * Tell this class about expected requests with
 * {@link #expectRequest(HTTPMethod, URL, HTTPResponse)}, then use it like a real
 * {@link URLFetchService} by calling {@link #fetch(URL)} or {@link #fetch(HTTPRequest)}.
 *
 * Works well with {@link FakeHTTPResponse}.
 */
public class FakeURLFetchService implements URLFetchService {

  private final Map<MethodUrl, HTTPResponse> responses = new HashMap<>();

  /**
   * Expect a request to a given URL with a given method, e.g. "GET http://www.tum.de".
   *
   * You most likely want the response to be a {@link FakeHTTPResponse} to set its headers and
   * content.
   */
  public void expectRequest(HTTPMethod expectedMethod, URL expectedUrl, HTTPResponse response) {
    responses.put(new MethodUrl(expectedMethod, expectedUrl), response);
  }

  /**
   * Just calls {@link #fetch(HTTPRequest)}.
   */
  @Override
  public HTTPResponse fetch(URL url) throws IOException {
    return fetch(new HTTPRequest(url));
  }

  /**
   * Returns a fake response that this class was previously told to expect with
   * {@link #expectRequest(HTTPMethod, URL, HTTPResponse)}. Does not actually request anything
   * from another server.
   *
   * Throws an exception if the request is unexpected.
   */
  @Override
  public HTTPResponse fetch(HTTPRequest request) throws IOException {
    URL url = request.getURL();
    HTTPMethod method = request.getMethod();
    HTTPResponse response = responses.get(new MethodUrl(method, url));
    if (response == null) {
      throw new IllegalStateException("Was not expecting this request: " + method + " " + url);
    }
    return response;
  }

  @Override
  public Future<HTTPResponse> fetchAsync(URL url) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Future<HTTPResponse> fetchAsync(HTTPRequest request) {
    throw new UnsupportedOperationException("not implemented");
  }

  /**
   * A helper class to create a single, hashable key from a HTTPMethod and URL.
   *
   * This is necessary because {@link HTTPRequest} doesn't implement hashCode() or equals(), so it
   * would get lost in any hash-based data structure like {@link HashMap}.
   */
  private static class MethodUrl {
    private final HTTPMethod method;
    private final URL url;

    private MethodUrl(HTTPMethod method, URL url) {
      this.method = method;
      this.url = url;
    }

    @Override
    public int hashCode() {
      return Objects.hash(method, url);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof MethodUrl) {
        MethodUrl other = (MethodUrl) obj;
        return this.method == other.method && this.url.equals(url);
      }
      return false;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
          .add("method", method)
          .add("url", url)
          .toString();
    }
  }

}
