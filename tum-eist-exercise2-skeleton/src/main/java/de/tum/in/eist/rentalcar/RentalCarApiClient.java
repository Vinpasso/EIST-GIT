package de.tum.in.eist.rentalcar;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.gson.JsonObject;

import de.tum.in.eist.JsonHelper;
import de.tum.in.eist.Location;
import de.tum.in.eist.URLFetchServiceHelper;

/**
 * Client to query {@link RentalCarAPI} and parse the JSON response to
 * respective POJOs
 */
public class RentalCarApiClient {

	private final URLFetchService service;

	@Inject
	public RentalCarApiClient(URLFetchService service) {
		this.service = service;
	}

	/**
	 * Sends a http GET request to {@link RentalCarAPI} to get a rental Car.
	 */
	public RentalCar getRentalCar(String carClass, Location origin)
			throws IOException {

		/*
		 * By default app runs locally at port 8080. In case if you define the
		 * running port, change the port in following URL accordingly
		 */

		/*
		 * Prepare url in same format as defined in RentalCarAPI i.e.
		 * http://localhost:8080/rentalCarApi/getRentalCar/{Car Class}/{source
		 * Latitude}/{source Longitude} where {} are our inputs
		 */
		URL url = new URL("http://localhost:8080/rentalCarApi/getRentalCar/"
				+ carClass + "/" + origin.getLatitude() + "/"
				+ origin.getLongitude());

		// Send a http get request to the above url
		HTTPResponse response = service.fetch(url);

		// Get the data from response as String
		String jsonString = URLFetchServiceHelper.toString(response);
		try {

			// Parse JSON from string
			JsonObject json = JsonHelper.parse(jsonString);

			// Validate the data and return data in RentalCar POJO
			return toRentalCar(json);
		} catch (Exception e) {
			throw new IOException("Failed to parse JSON: " + jsonString, e);
		}
	}

	/**
	 * Sends a http GET request to {@link RentalCarAPI} to make rental car trip
	 */
	public RentalCarTrip rentalCarTrip(String carClass, Location origin,
			Location destination) throws IOException {
		/*
		 * Develop a similar method as above for Rental Car API makeTrip call
		 * Hint - See format of makeTrip call in RentalCarAPI Hint - Make
		 * toRentalCarTrip(JsonObject response) to validate and return data in
		 * RentalCarTrip POJO
		 */
		URL url = new URL("http://localhost:8080/rentalCarApi/rentalCarTrip/"
				+ carClass + "/" + origin.getLatitude() + "/"
				+ origin.getLongitude() + "/" + destination.getLatitude() + "/"
				+ destination.getLatitude());
		HTTPResponse response = service.fetch(url);
		String jsonString = URLFetchServiceHelper.toString(response);
		try {
			JsonObject json = JsonHelper.parse(jsonString);
			return toRentalCarTrip(json);
		} catch (Exception e) {
			throw new IOException("Failed to parse JSON: " + jsonString, e);
		}
	}
	
	public static RentalCarTrip toRentalCarTrip(JsonObject response)
	{
		if (!response.has("status")) {
			throw new IllegalArgumentException("No Status set from Car API");
		} else {
			if (!response.get("status").getAsString().equals("ok"))
				throw new IllegalArgumentException(response.get("status")
						.getAsString());
			else {
				return new RentalCarTrip(
						response.get("distance").getAsString(), 
						response.get("duration").getAsString(), 
						response.get("totalPrice").getAsString(), 
						response.get("carTypeName").getAsString());
			}
		}
	}

	/**
	 * Checks the API data from {@link #getRentalCar(String, Location)} for
	 * errors. Maps this JSON data to {@link RentalCar} POJO
	 */
	public static RentalCar toRentalCar(JsonObject response) {

		// Check if there is a status set by RentalCarAPI
		if (!response.has("status")) {
			throw new IllegalArgumentException("No Status set from Car API");
		} else {

			// Check if the status is not 'ok' i.e. error in API
			if (!response.get("status").getAsString().equals("ok"))
				throw new IllegalArgumentException(response.get("status")
						.getAsString());
			else {

				// Extract coordinates of rental car from JSON
				Location rentalCarLoc = toLocation(response);

				// Extract duration of rental car as String from JSON
				String duration = response.get("duration").getAsString();

				// and so on...
				String distance = response.get("distance").getAsString();
				int seats = response.get("typicalSeating").getAsInt();
				String type = response.get("carTypeName").getAsString();

				// Finally return a RentalCar POJO with all the data
				return new RentalCar(distance, rentalCarLoc, duration, seats,
						type);
			}
		}
	}

	/**
	 * Converts JSON latitude and longitude of any location to {@link Location}
	 * POJO
	 */
	private static Location toLocation(JsonObject car) {
		return new Location(car.get("latitude").getAsDouble(), car.get(
				"longitude").getAsDouble());
	}
}
