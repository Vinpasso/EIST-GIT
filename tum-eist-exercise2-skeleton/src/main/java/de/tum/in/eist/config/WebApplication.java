package de.tum.in.eist.config;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.api.container.filter.GZIPContentEncodingFilter;
import com.sun.jersey.api.container.filter.LoggingFilter;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * Glues everything together. No user-serviceable parts inside.
 *
 * <p>GuiceFilter and this class are the only things that need to be configured in web.xml,
 * everything else is then configured by this class.</p>
 */
public class WebApplication extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new JerseyServletModule() {
      /**
       * Configures the Guice container of JAX-RS.
       */
      @Override
      protected void configureServlets() {
        install(new JerseyModule());
        install(new AppEngineModule());

        // Specify Jersey filters using their FQCN separated by ";"
        String filterNames = Joiner.on(';').join(
              GZIPContentEncodingFilter.class.getName(),
              LoggingFilter.class.getName());

        // Enable JAX-RS and Guice on all paths.
        filter("/*").through(GuiceContainer.class, ImmutableMap.of(
            ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, filterNames,
            ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS, filterNames,

            // Only log HTTP headers, not the request entity/body@JsonIgnoreProperties
            LoggingFilter.FEATURE_LOGGING_DISABLE_ENTITY, Boolean.TRUE.toString(),

            // Always render Lists as JSON arrays, the default renders renders single-element lists
            // as JSON object which needlessly complicates client-side parsing.
            JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE.toString(),

            // Dispatch unhandled URLs to container for static files (HTML, JS, CSS, images, ...)
            ServletContainer.FEATURE_FILTER_FORWARD_ON_404, Boolean.TRUE.toString()));
      }
    });
  }
}
