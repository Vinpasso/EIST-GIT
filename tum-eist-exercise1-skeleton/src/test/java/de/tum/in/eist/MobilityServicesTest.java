package de.tum.in.eist;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.tum.in.eist.rentalcar.RentalCarAPI;

@RunWith(JUnit4.class)
public class MobilityServicesTest {

	@Test
	public void testGetCarClass() throws Exception
	{
		Method method = MobilityServices.class.getDeclaredMethod("getCarClass", String.class);
		method.setAccessible(true);
		assertEquals("getCarClass Student", "Bike", method.invoke(null, "student"));
		assertEquals("getCarClass Student", "Bike", method.invoke(null, "Student"));
		assertEquals("getCarClass Student", "Bike", method.invoke(null, "STUDENT"));
		assertEquals("getCarClass Business", "PremiumCar", method.invoke(null, "business"));
		assertEquals("getCarClass Business", "PremiumCar", method.invoke(null, "Business"));
		assertEquals("getCarClass Business", "PremiumCar", method.invoke(null, "BUSINESS"));
		assertEquals("getCarClass Family", "MidsizeSUV", method.invoke(null, "family"));
		assertEquals("getCarClass Family", "MidsizeSUV", method.invoke(null, "Family"));
		assertEquals("getCarClass Family", "MidsizeSUV", method.invoke(null, "FAMILY"));
	}

	@Test
	public void testCalculateTripOptions() throws Exception
	{
		Method method = MobilityServices.class.getDeclaredMethod("calculateTripOptions", String.class, Integer.TYPE, Location.class, Location.class);
		method.setAccessible(true);
		Trip trip = (Trip) method.invoke(null, "student", 1, new Location(0, 0), new Location(0, 0));
		assertEquals("calculateTripOptions Distance 0", true, trip.carApiSuccess);
		assertEquals("calculateTripOptions Distance 0", true, trip.trainApiSuccess);
		assertEquals("calculateTripOptions Distance 0", "0.0 Meters", trip.rentalCarTrip.getDistance());
		assertEquals("calculateTripOptions Distance 0", "0.0 Meters", trip.rentalCarTrip.getDuration());
		assertEquals("calculateTripOptions Distance 0", "0.0 Meters", trip.rentalCarTrip.getPrice());

	}
}
