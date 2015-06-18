package com.google.appengine.api.urlfetch;

import java.nio.charset.StandardCharsets;

/**
 * A mutable fake implementation of {@link HTTPResponse}.
 *
 * This class needs to live in the com.google.appengine.api.urlfetch package to have access to some
 * of the package-private methods of the HTTPResponse class it extends.
 *
 * Use only in tests, do not deploy to App Engine -- the sandbox there will reject it because this
 * class is placing code in a package owned by App Engine.
 */
@SuppressWarnings("serial")
public class FakeHTTPResponse extends HTTPResponse {

  public FakeHTTPResponse(int responseCode) {
    super(responseCode);
  }

  public void setContent(String content) {
    setContent(content.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public void setContent(byte[] content) {
    super.setContent(content);
  }

  @Override
  public void addHeader(String name, String value) {
    super.addHeader(name, value);
  }
}
