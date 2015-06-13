package de.tum.in.eist.config;

import com.google.inject.AbstractModule;

import de.tum.in.eist.MobilityServices;
import de.tum.in.eist.rentalcar.RentalCarAPI;
import de.tum.in.eist.train.TrainAPI;

/*
 * Register Jersey resource classes here by binding them.
 */
public class JerseyModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(RentalCarAPI.class);
    bind(TrainAPI.class);
    bind(MobilityServices.class);
  }

}
