package de.tum.in.eist.config;

import static com.googlecode.objectify.ObjectifyService.factory;

import com.google.inject.AbstractModule;

import de.tum.in.eist.UserData;

/*
 * Register Objectify entity classes here.
 */
public class ObjectifyModule extends AbstractModule {

  @Override
  public void configure() {
    factory().register(UserData.class);
  }

}
