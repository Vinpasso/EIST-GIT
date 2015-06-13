package de.tum.in.eist.config;

import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.inject.AbstractModule;

/**
 * Provides App Engine services, so they can be injected when running the app (locally or on App
 * Engine) and during tests, fake implementations of the services can be used.
 */
public class AppEngineModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(URLFetchService.class).toInstance(URLFetchServiceFactory.getURLFetchService());
  }
}
