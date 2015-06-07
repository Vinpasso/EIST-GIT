package de.tum.in.eist;

import de.tum.in.eist.rentalcar.RentalCarTrip;

public interface RentalCarTripListener {

	public void tripComputed(RentalCarTrip trip);

	public void computationFailed(Exception e);
}
