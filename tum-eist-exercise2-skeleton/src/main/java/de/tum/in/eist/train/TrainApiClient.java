package de.tum.in.eist.train;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.gson.JsonObject;

import de.tum.in.eist.JsonHelper;
import de.tum.in.eist.Location;
import de.tum.in.eist.MobilityServices;
import de.tum.in.eist.URLFetchServiceHelper;

/**
 * Client to query {@link TrainAPI} and parse the JSON response to respective
 * POJO
 */
public class TrainApiClient {

	private final URLFetchService service;

	@Inject
	public TrainApiClient(URLFetchService service) {
		this.service = service;
	}

	/**
	 * Sends a http GET request to {@link TrainAPI} to make train trip.
	 */
	public TrainTrip trainTrip(String userClass, int seats, Location origin,
			Location destination) throws IOException {
		/*
		 * Develop a method for TrainAPI makeTrip call (For example see -
		 * RentalCarApiClient) Hint - See format of makeTrip call in TrainAPI
		 * Hint - Make toTrainTrip(JsonObject response) to validate and return
		 * data in TrainTrip POJO
		 */
		URL url = new URL("http://localhost:8080/makeTrip/"
				+ MobilityServices.getCabinClass(userClass) + "/" + seats + "/"
				+ origin.getLatitude() + "/" + origin.getLongitude() + "/"
				+ destination.getLatitude() + "/" + destination.getLongitude());

		// Send a http get request to the above url
		HTTPResponse response = service.fetch(url);

		// Get the data from response as String
		String jsonString = URLFetchServiceHelper.toString(response);
		try {

			// Parse JSON from string
			JsonObject json = JsonHelper.parse(jsonString);

			// Validate the data and return data in RentalCar POJO
			return toTrainTrip(json);
		} catch (Exception e) {
			throw new IOException("Failed to parse JSON: " + jsonString, e);
		}
	}

	private TrainTrip toTrainTrip(JsonObject json) {
		return new TrainTrip(json.get("distance").getAsString(), 
				json.get("duration").getAsString(),
				json.get("totalPrice").getAsString(), 
				json.get("cabin").getAsString(), 
				json.get("seats").getAsInt());
	}

}
