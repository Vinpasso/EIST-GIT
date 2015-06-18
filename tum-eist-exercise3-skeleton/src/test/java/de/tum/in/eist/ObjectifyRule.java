package de.tum.in.eist;

import java.io.Closeable;
import java.io.IOException;

import org.junit.rules.ExternalResource;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.base.Throwables;
import com.googlecode.objectify.ObjectifyService;

import de.tum.in.eist.config.ObjectifyModule;

/*
 * A JUnit Rule that sets up Objectify and the LocalDatastoreService for tests.
 */
public class ObjectifyRule extends ExternalResource {

  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
      new LocalDatastoreServiceTestConfig());

  private Closeable ofyTransaction;

  @Override
  protected void before() throws Throwable {
    super.before();
    helper.setUp();
    new ObjectifyModule().configure(); // register Ofy entities
    ofyTransaction = ObjectifyService.begin();
  }

  @Override
  protected void after() {
    super.after();
    try {
      ofyTransaction.close();
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
    helper.tearDown();
  }
}
