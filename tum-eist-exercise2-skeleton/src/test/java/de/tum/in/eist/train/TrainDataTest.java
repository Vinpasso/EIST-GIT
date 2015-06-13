package de.tum.in.eist.train;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.tum.in.eist.train.TrainData;

@RunWith(JUnit4.class)
public class TrainDataTest {
  
  /* Allowed delta for floating-point comparisons */
  private static final double DELTA = 0;
  
  @Test
  public void calculate_invalidCabinClass() {
    TrainData trainTrip = new TrainData("Invalid", 100.00, 2);
    trainTrip.calculate();
    assertEquals(0.0,trainTrip.totalPrice,DELTA);
  }
  
  @Test
  public void calculate_negativeDistance() {
    TrainData trainTrip = new TrainData("ThirdClass", -100.00, 2);
    trainTrip.calculate();
    assertTrue(trainTrip.totalPrice<0.0);
    assertTrue(trainTrip.duration<=0.0);
  }
  
  @Test
  public void calculate_zeroDistance() {
    TrainData trainTrip = new TrainData("ThirdClass", 0.0, 2);
    trainTrip.calculate();
    assertEquals(0.0,trainTrip.totalPrice,DELTA);
    assertEquals(0.0,trainTrip.duration,DELTA);
  }
  
  @Test
  public void calculate_negativeSeats() {
    TrainData trainTrip = new TrainData("ThirdClass", 100.00, -2);
    trainTrip.calculate();
    assertTrue(trainTrip.totalPrice<0.0);
  }
  
  @Test
  public void calculate_zeroSeats() {
    TrainData trainTrip = new TrainData("ThirdClass", 100.00, 0);
    trainTrip.calculate();
    assertEquals(0.0,trainTrip.totalPrice,DELTA);
  }
}
