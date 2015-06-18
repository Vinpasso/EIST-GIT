package de.tum.in.eist.config;

import com.google.inject.AbstractModule;

import de.tum.in.eist.ExampleService;
import de.tum.in.eist.MobilityServices;
import de.tum.in.eist.UserAccountManager;

/*
 * Register Jersey resource classes here by binding them.
 */
public class JerseyModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(UserAccountManager.class);
    bind(MobilityServices.class);
    bind(ExampleService.class);
  }

}
