package de.tum.in.eist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UtilsTest {
  
  /* Allowed delta for floating-point comparisons */
  private static final double DELTA = 0;
  
  @Test(expected = IllegalArgumentException.class)
  public void round_toNegativePlaces() {
    Utils.round(9.999, -1);
  }

  @Test
  public void round_halfRoundUp() {
    assertEquals(8.9, Utils.round(8.85, 1), DELTA);
  }

  @Test
  public void round_bigDecimalPart_16Decimals() {
    assertEquals(1.1234567891234568, Utils.round(1.1234567891234567891, 16), DELTA);
  }

  @Test
  public void round_bigDecimalPart_18Decimals_incorrect() {
    assertEquals(1.1234567891234568, Utils.round(1.1234567891234567891, 18),DELTA);
  }
  
  @Test
  public void findDistance_allZeroInputs() {
    assertEquals(0.0,Utils.findDistance(new Location(0.0, 0.0), new Location(0.0, 0.0)),DELTA);
  }

  @Test
  public void findDistance_allNegativeInputs() {
    assertEquals(0.0,Utils.findDistance(new Location(-1.0, -1.0), new Location(-1.0, -1.0)),DELTA);
  }
  
  @Test
  public void findDistance_maxMinCoordinates() {
    assertTrue(Utils.findDistance(new Location(90.0, 180.0), new Location(-90.0, -180.0))>=20014000.0);
    assertTrue(Utils.findDistance(new Location(90.0, 180.0), new Location(-90.0, -180.0))<=20016000.0);
  }
  
  @Test
  public void findDistance_NorthPoleSouthPole() {
    assertTrue(Utils.findDistance(new Location(90.0, 0.0),new Location(-90.0, 0.0))>=20014000.0);
    assertTrue(Utils.findDistance(new Location(90.0, 0.0), new Location(-90.0, 0.0))<=20016000.0);
  }
}
