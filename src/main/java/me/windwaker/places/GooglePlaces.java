package me.windwaker.places;

import me.windwaker.places.exception.GooglePlacesException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static me.windwaker.places.HttpUtil.DEFAULT_CLIENT;
import static me.windwaker.places.HttpUtil.get;
import static me.windwaker.places.HttpUtil.post;

/**
 * A Java binding for the Google Places API:
 * <a href="https://developers.google.com/places/">https://developers.google.com/places/</a>
 */
public class GooglePlaces {
	/**
	 * The URL of which Google Places API is located.
	 */
	public static final String API_URL = "https://maps.googleapis.com/maps/api/place/";
	private final String apiKey;
	private final HttpClient client;
	private boolean sensor;

	/**
	 * Creates a new GooglePlaces object using the specified API key and the specified HttpClient.
	 *
	 * @param apiKey that has been registered on the Google Developer Console
	 * @param client to handle HTTP traffic
	 */
	public GooglePlaces(String apiKey, HttpClient client) {
		this.apiKey = apiKey;
		this.client = client;
	}

	/**
	 * Creates a new GooglePlaces object using the specified API key and the default HttpClient.
	 *
	 * @param apiKey that has been registered on the Google Developer Console
	 */
	public GooglePlaces(String apiKey) {
		this(apiKey, DEFAULT_CLIENT);
	}

	/**
	 * Set this to true if the device you are using has a location detector such as GPS.
	 *
	 * @param sensor if sensor is enabled
	 */
	public void setSensorEnabled(boolean sensor) {
		this.sensor = sensor;
	}

	/**
	 * Returns true if the device has a location sensor.
	 *
	 * @return true if has location sensor
	 */
	public boolean isSensorEnabled() {
		return sensor;
	}

	/**
	 * Returns the API key associated with this GooglePlaces object.
	 *
	 * @return api key
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * Returns the http client that is handling http traffic.
	 *
	 * @return http client
	 */
	public HttpClient getHttpClient() {
		return client;
	}

	/**
	 * Represents an extra, optional parameter that can be specified.
	 */
	public static class Param {
		private final String name;
		private String value;

		private Param(String name) {
			this.name = name;
		}

		/**
		 * Sets the value of the Param.
		 *
		 * @param value of param
		 * @return this param
		 */
		public Param value(Object value) {
			this.value = value.toString();
			return this;
		}

		/**
		 * Returns a new param with the specified name.
		 *
		 * @param name to create Param from
		 * @return new param
		 */
		public static Param name(String name) {
			return new Param(name);
		}
	}

	protected static String addExtraParams(String base, Param... extraParams) {
		for (Param param : extraParams) {
			base += "&" + param.name + (param.value != null ? "=" + param.value : "");
		}
		return base;
	}

	/**
	 * The maximum amount of results that can be on one single page.
	 */
	public static final int MAXIMUM_PAGE_RESULTS = 20;
	/**
	 * The maximum results that can be returned.
	 */
	public static final int MAXIMUM_RESULTS = 60;
	/**
	 * The default amount of results that will be returned in a single request.
	 */
	public static final int DEFAULT_RESULTS = MAXIMUM_PAGE_RESULTS;
	/**
	 * The maximum search radius for places.
	 */
	public static final double MAXIMUM_RADIUS = 50000;

	public static final String METHOD_NEARBY_SEARCH = "nearbysearch";
	public static final String METHOD_TEXT_SEARCH = "textsearch";
	public static final String METHOD_RADAR_SEARCH = "radarsearch";
	public static final String METHOD_DETAILS = "details";
	public static final String METHOD_ADD = "add";
	public static final String METHOD_DELETE = "delete";
	public static final String METHOD_EVENT_DETAILS = "event/details";
	public static final String METHOD_EVENT_ADD = "event/add";
	public static final String METHOD_EVENT_DELETE = "event/delete";
	public static final String METHOD_BUMP = "bump";
	public static final String METHOD_AUTOCOMPLETE = "autocomplete";

	/**
	 * Returns the places at the specified latitude and longitude within the specified radius. If the specified limit
	 * is greater than {@link #MAXIMUM_PAGE_RESULTS}, multiple HTTP GET requests may be made if necessary.
	 *
	 * @param lat         latitude
	 * @param lng         longitude
	 * @param radius      radius
	 * @param limit       the maximum amount of places to return
	 * @param extraParams any extra parameters to include in the request URL
	 * @return a list of places that were found
	 */
	public List<Place> getNearbyPlaces(double lat, double lng, double radius, int limit, Param... extraParams) {
		try {
			String uri = String.format("%s%s/json?key=%s&location=%f,%f&radius=%f&sensor=%b",
					API_URL, METHOD_NEARBY_SEARCH, apiKey, lat, lng, radius, sensor);
			uri = addExtraParams(uri, extraParams);
			return getPlaces(uri, METHOD_NEARBY_SEARCH, sensor, limit);
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

	/**
	 * Returns the places at the specified latitude and longitude within the specified radius. No more than
	 * {@link #DEFAULT_RESULTS} will be returned and no more than one HTTP GET request will be sent. The 'sensor'
	 * parameter defaults to false.
	 *
	 * @param lat         latitude
	 * @param lng         longitude
	 * @param radius      radius
	 * @param extraParams any extra parameters to include in the request URL
	 * @return a list of places that were found
	 */
	public List<Place> getNearbyPlaces(double lat, double lng, double radius, Param... extraParams) {
		return getNearbyPlaces(lat, lng, radius, DEFAULT_RESULTS, extraParams);
	}

	/**
	 * Returns the places that match the specified search query.  If the specified limit
	 * is greater than {@link #MAXIMUM_PAGE_RESULTS}, multiple HTTP GET requests may be made if necessary.
	 *
	 * @param query       search query
	 * @param limit       the maximum amount of places to return
	 * @param extraParams any extra parameters to include in the request URL
	 * @return a list of places that were found
	 */
	public List<Place> getPlacesByQuery(String query, int limit, Param... extraParams) {
		try {
			// build base uri
			String uri = String.format("%s%s/json?query=%s&key=%s&sensor=%b",
					API_URL, METHOD_TEXT_SEARCH, query, apiKey, sensor);
			uri = addExtraParams(uri, extraParams);
			return getPlaces(uri, METHOD_TEXT_SEARCH, sensor, limit);
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

	/**
	 * Returns the places that match the specified search query. No more than {@link #DEFAULT_RESULTS} will be returned
	 * and no more than one HTTP GET request will be sent. The 'sensor' parameter defaults to false.
	 *
	 * @param query       search query
	 * @param extraParams any extra parameters to include in the request URL
	 * @return a list of places that were found
	 */
	public List<Place> getPlacesByQuery(String query, Param... extraParams) {
		return getPlacesByQuery(query, DEFAULT_RESULTS, extraParams);
	}

	/**
	 * Returns the places at the specified latitude and longitude according to the "radar" method specified by Google
	 * Places API.  If the specified limit is greater than {@link #MAXIMUM_PAGE_RESULTS}, multiple HTTP GET requests
	 * may be made if necessary.
	 *
	 * @param lat         latitude
	 * @param lng         longitude
	 * @param radius      radius
	 * @param limit       the maximum amount of places to return
	 * @param extraParams any extra parameters to include in the request URL
	 * @return a list of places that were found
	 */
	public List<Place> getPlacesByRadar(double lat, double lng, double radius, int limit, Param... extraParams) {
		try {
			String uri = String.format("%s%s/json?key=%s&location=%f,%f&radius=%f&sensor=%b",
					API_URL, METHOD_RADAR_SEARCH, apiKey, lat, lng, radius, sensor);
			uri = addExtraParams(uri, extraParams);
			return getPlaces(uri, METHOD_RADAR_SEARCH, sensor, limit);
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

	/**
	 * Returns the places at the specified latitude and longitude according to the "radar" method specified by Google
	 * Places API. No more than {@link #DEFAULT_RESULTS} will be returned and no more than one HTTP GET request will
	 * be sent.
	 *
	 * @param lat         latitude
	 * @param lng         longitude
	 * @param radius      radius
	 * @param extraParams any extra parameters to include in the request URL
	 * @return a list of places that were found
	 */
	public List<Place> getPlacesByRadar(double lat, double lng, double radius, Param... extraParams) {
		return getPlacesByRadar(lat, lng, radius, MAXIMUM_RESULTS, extraParams);
	}

	/**
	 * Returns the place using the specified reference ID.
	 *
	 * @param reference id
	 * @param extraParams to append to request url
	 * @return place
	 */
	public Place getPlace(String reference, Param... extraParams) {
		try {
			String uri = String.format("%s%s/json?key=%s&reference=%s&sensor=%b", API_URL,
					METHOD_DETAILS, apiKey, reference, sensor);
			uri = GooglePlaces.addExtraParams(uri, extraParams);
			return Place.parseDetails(this, get(client, uri));
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

	/**
	 * Adds a new place to Places API and gets the newly created place if returnPlace is set to true.
	 *
	 * @param name of place
	 * @param lang language of place
	 * @param lat latitude coordinate
	 * @param lng longitude coordinate
	 * @param accuracy of coordinates in meters
	 * @param types collection of types
	 * @param returnPlace true if the newly created place should be returned
	 * @param extraParams to append to request url
	 * @return newly created place
	 */
	public Place addPlace(String name, String lang, double lat, double lng, int accuracy, Collection<String> types,
						  boolean returnPlace, Param... extraParams) {
		try {
			String uri = String.format("%s%s/json?sensor=%b&key=%s", API_URL, METHOD_ADD, sensor, apiKey);
			uri = GooglePlaces.addExtraParams(uri, extraParams);
			JSONObject input = Place.buildInput(lat, lng, accuracy, name, types, lang);
			System.out.println("Input: " + input);
			HttpPost post = new HttpPost(uri);
			post.setEntity(new StringEntity(input.toString()));
			JSONObject response = new JSONObject(post(client, post));
			String status = response.getString(STRING_STATUS);
			checkStatus(status);
			return returnPlace ? getPlace(response.getString(STRING_REFERENCE)) : null;
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

	/**
	 * Adds a new place to Places API and returns the newly created Place.
	 *
	 * @param name of place
	 * @param lang language of place
	 * @param lat latitude coordinate
	 * @param lng longitude coordinate
	 * @param accuracy of coordinates in meters
	 * @param types collection of types
	 * @return newly created place
\	 */
	public Place addPlace(String name, String lang, double lat, double lng, int accuracy, Collection<String> types,
						  Param... extraParams) {
		return addPlace(name, lang, lat, lng, accuracy, types, true, extraParams);
	}

	/**
	 * Adds a new place to Places API and returns the newly created Place.
	 *
	 * @param name of place
	 * @param lang language of place
	 * @param lat latitude coordinate
	 * @param lng longitude coordinate
	 * @param accuracy of coordinates in meters
	 * @param type type of place
	 * @return newly created place
	 */
	public Place addPlace(String name, String lang, double lat, double lng, int accuracy, String type,
						  Param... extraParams) {
		return addPlace(name, lang, lat, lng, accuracy, Arrays.asList(type), extraParams);
	}

	/**
	 * Adds a new place to Places API and returns the newly created Place.
	 *
	 * @param name of place
	 * @param lang language of place
	 * @param lat latitude coordinate
	 * @param lng longitude coordinate
	 * @param accuracy of coordinates in meters
	 * @param type type of place
	 * @return newly created place
	 */
	public Place addPlace(String name, String lang, double lat, double lng, int accuracy, String type,
						  boolean returnPlace, Param... extraParams) {
		return addPlace(name, lang, lat, lng, accuracy, Arrays.asList(type), returnPlace, extraParams);
	}

	/**
	 * Deletes the place specified by the specified reference ID.
	 *
	 * @param reference id
	 */
	public void deletePlace(String reference, Param... extraParams) {
		try {
			String uri = String.format("%s%s/json?sensor=%b&key=%s", API_URL, METHOD_DELETE, sensor, apiKey);
			uri = addExtraParams(uri, extraParams);
			JSONObject input = new JSONObject().put(STRING_REFERENCE, reference);
			System.out.println("Input: " + input);
			HttpPost post = new HttpPost(uri);
			post.setEntity(new StringEntity(input.toString()));
			JSONObject response = new JSONObject(post(client, post));
			String status = response.getString(STRING_STATUS);
			checkStatus(status);
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

	/**
	 * Deletes the specified place.
	 *
	 * @param place to delete
	 */
	public void deletePlace(Place place, Param... extraParams) {
		deletePlace(place.getReferenceId(), extraParams);
	}

	/**
	 * Bumps a place within the application. Bumps are reflected in your place searches for your application only.
	 * Bumping a place makes it appear higher in the result set.
	 *
	 * @param place to bump
	 * @param extraParams to append to request url
	 */
	public void bumpPlace(Place place, Param... extraParams) {
		try {
			String uri = String.format("%s%s/json?sensor=%b&key=%s", API_URL, METHOD_BUMP, sensor, apiKey);
			uri = addExtraParams(uri, extraParams);
			HttpPost post = new HttpPost(uri);
			JSONObject input = new JSONObject().put(STRING_REFERENCE, place.getReferenceId());
			post.setEntity(new StringEntity(input.toString()));
			JSONObject response = new JSONObject(post(client, post));
			checkStatus(response.getString(STRING_STATUS));
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

	/**
	 * Bumps an event within the application. Bumps are reflected in your place searches for your application only.
	 * Bumping an event makes it appear higher in the result set.
	 *
	 * @param event to bump
	 * @param extraParams to append to request url
	 */
	public void bumpEvent(Event event, Param... extraParams) {
		try {
			String uri = String.format("%s%s/json?sensor=%b&key=%s", API_URL, METHOD_BUMP, sensor, apiKey);
			uri = addExtraParams(uri, extraParams);
			HttpPost post = new HttpPost(uri);
			JSONObject input = new JSONObject().put(STRING_REFERENCE, event.getPlace().getReferenceId())
					.put(STRING_EVENT_ID, event.getId());
			post.setEntity(new StringEntity(input.toString()));
			JSONObject response = new JSONObject(post(client, post));
			checkStatus(response.getString(STRING_STATUS));
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

	/**
	 * Returns an ImageInputStream from the specified photo reference.
	 *
	 * @param photo to get image of
	 * @param maxWidth of image, -1 for none
	 * @param maxHeight of image, -1 for none
	 * @param extraParams to append to request url
	 * @return image input stream
	 */
	public ImageInputStream getImageInputStream(Photo photo, int maxWidth, int maxHeight, Param... extraParams) {
		try {
			String uri = String.format("%sphoto?photoreference=%s&sensor=%b&key=%s", API_URL, photo.getReference(),
					sensor, apiKey);

			List<Param> params = new ArrayList<Param>(Arrays.asList(extraParams));
			if (maxHeight != -1) params.add(Param.name("maxheight").value(maxHeight));
			if (maxWidth != -1) params.add(Param.name("maxwidth").value(maxWidth));
			extraParams = params.toArray(new Param[params.size()]);

			uri = addExtraParams(uri, extraParams);
			System.out.println("URL: " + uri);
			HttpGet get = new HttpGet(uri);
			InputStream in = client.execute(get).getEntity().getContent();
			System.out.println(in);
			return ImageIO.createImageInputStream(in);
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

	/**
	 * Returns the image in it's original form.
	 *
	 * @param photo to return
	 * @param extraParams to append to request url
	 * @return input stream
	 */
	public ImageInputStream getImageInputStream(Photo photo, Param... extraParams) {
		return getImageInputStream(photo, -1, -1, extraParams);
	}

	/**
	 * Returns an Image from the specified photo reference.
	 *
	 * @param photo to get image of
	 * @param maxWidth of image
	 * @param maxHeight of image
	 * @param extraParams to append to request url
	 * @return image
	 */
	public BufferedImage getImage(Photo photo, int maxWidth, int maxHeight, Param... extraParams) {
		try {
			return ImageIO.read(getImageInputStream(photo, maxWidth, maxHeight, extraParams));
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

	public static final int MAX_PHOTO_SIZE = 1600;

	/**
	 * Returns an image in it's original form.
	 *
	 * @param photo to get
	 * @param extraParams to append to request url
	 * @return image
	 */
	public Image getImage(Photo photo, Param... extraParams) {
		return getImage(photo, MAX_PHOTO_SIZE, MAX_PHOTO_SIZE, extraParams);
	}

	/**
	 * Returns the event at the specified place with the specified event id.
	 *
	 * @param place reference to place the event is at
	 * @param eventId id of event
	 * @param extraParams to append to request url
	 * @return event
	 */
	public Event getEvent(Place place, String eventId, Param... extraParams) {
		try {
			String uri = String.format("%s%s/json?sensor=%b&key=%s&reference=%s&event_id=%s", API_URL, METHOD_EVENT_DETAILS,
					sensor, apiKey, place.getReferenceId(), eventId);
			uri = addExtraParams(uri, extraParams);
			String response = get(client, uri);
			return Event.parseDetails(response).setPlace(place);
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

	/**
	 * Adds a new Event to Google Places API.
	 *
	 * @param place to add to
	 * @param summary of event
	 * @param lang language of event
	 * @param url url of event
	 * @param duration length of event in seconds
	 * @param returnEvent if GET request should be made to retrieve the newly created event
	 * @param extraParams to append to request url
	 * @return newly created event
	 */
	public Event addEvent(Place place, String summary, long duration, String lang, String url, boolean returnEvent,
						  Param... extraParams) {
		try {
			String uri = String.format("%s%s/json?sensor=%b&key=%s", API_URL, METHOD_EVENT_ADD, sensor, apiKey);
			uri = addExtraParams(uri, extraParams);
			HttpPost post = new HttpPost(uri);
			JSONObject input = Event.buildInput(duration, lang, place.getReferenceId(), summary, url);
			System.out.println("Input: " + input);
			post.setEntity(new StringEntity(input.toString()));
			JSONObject response = new JSONObject(post(client, post));
			String status = response.getString(STRING_STATUS);
			checkStatus(status);
			return returnEvent ? getEvent(place, response.getString(STRING_EVENT_ID)) : null;
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

	/**
	 * Adds a new Event to Google Places API.
	 *
	 * @param place to add to
	 * @param summary of event
	 * @param lang language of event
	 * @param url url of event
	 * @param duration length of event in seconds
	 * @param extraParams to append to request url
	 * @return newly created event
	 */
	public Event addEvent(Place place, String summary, long duration, String lang, String url, Param... extraParams) {
		return addEvent(place, summary, duration, lang, url, true, extraParams);
	}

	/**
	 * Adds a new Event to Google Places API.
	 *
	 * @param place to add to
	 * @param summary of event
	 * @param duration length of event in seconds
	 * @param extraParams to append to request url
	 * @return newly created event
	 */
	public Event addEvent(Place place, String summary, long duration, boolean returnEvent, Param... extraParams) {
		return addEvent(place, summary, duration, null, null, returnEvent, extraParams);
	}

	/**
	 * Adds a new Event to Google Places API.
	 *
	 * @param place to add to
	 * @param summary of event
	 * @param duration length of event in seconds
	 * @param extraParams to append to request url
	 * @return newly created event
	 */
	public Event addEvent(Place place, String summary, long duration, Param... extraParams) {
		return addEvent(place, summary, duration, true, extraParams);
	}

	/**
	 * Deletes the specified event from Places API.
	 *
	 * @param placeReference that contains event
	 * @param eventId unique event id
	 * @param extraParams to append to request url
	 */
	public void deleteEvent(String placeReference, String eventId, Param... extraParams) {
		try {
			String uri = String.format("%s%s/json?sensor=%b&key=%s", API_URL, METHOD_EVENT_DELETE, sensor, apiKey);
			uri = addExtraParams(uri, extraParams);
			HttpPost post = new HttpPost(uri);
			JSONObject input = new JSONObject().put(STRING_REFERENCE, placeReference)
					.put(STRING_EVENT_ID, eventId);
			System.out.println("Input: " + input);
			post.setEntity(new StringEntity(input.toString()));
			JSONObject response = new JSONObject(post(client, post));
			checkStatus(response.getString(STRING_STATUS));
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

	/**
	 * Deletes the specified event from Places API.
	 *
	 * @param event to delete
	 * @param extraParams to append to request url
	 */
	public void deleteEvent(Event event, Param... extraParams) {
		deleteEvent(event.getPlace().getReferenceId(), event.getId(), extraParams);
	}

	public List<Prediction> getPredictions(String input, int offset, double lat, double lng, double radius, String lang,
									String type, String country, Param... extraParams) {
		try {
			String uri = String.format("%s%s/json?input=%s&sensor=%b&key=%s", API_URL, METHOD_AUTOCOMPLETE, input, sensor,
					apiKey);

			List<Param> params = new ArrayList<Param>(Arrays.asList(extraParams));
			if (offset != -1) params.add(Param.name("offset").value(offset));
			if (lat != -1 && lng != -1) params.add(Param.name("location").value(lat + "," + lng));
			if (radius != -1) params.add(Param.name("radius").value(radius));
			if (lang != null) params.add(Param.name("language").value(lang));
			if (type != null) params.add(Param.name("types").value(type));
			if (country != null) params.add(Param.name("components").value("country:" + country));
			extraParams = params.toArray(new Param[params.size()]);

			uri = addExtraParams(uri, extraParams);
			String response = get(client, uri);
			return Prediction.parse(this, response);
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

	public List<Prediction> getPredictions(String input, Param... extraParams) {
		return getPredictions(input, -1, -1, -1, -1, null, null, null, extraParams);
	}

	// ARRAYS
	public static final String ARRAY_RESULTS = "results"; // Array for results
	public static final String ARRAY_TYPES = "types"; // Types of place
	public static final String ARRAY_EVENTS = "events"; // The events occurring at the place
	public static final String ARRAY_PERIODS = "periods"; // Signifies the hours of operation of a place
	public static final String ARRAY_PHOTOS = "photos";  // Array containing photo information
	public static final String ARRAY_ADDRESS_COMPONENTS = "address_components"; // An array containing each element in a places full address
	public static final String ARRAY_REVIEWS = "reviews"; // Array of reviews of a Place
	public static final String ARRAY_ASPECTS = "aspects"; // Array of aspects of a review
	public static final String ARRAY_PREDICTIONS = "predictions"; // Array of autocomplete predictions
	public static final String ARRAY_TERMS = "terms"; // Array of terms describing a autocomplete prediction description
	public static final String ARRAY_MATCHED_SUBSTRINGS = "matched_substrings"; // Used for matching matched substrings for autocompletion

	// BOOLEANS
	public static final String BOOLEAN_OPENED = "open_now"; // If the place is opened now

	// DOUBLES
	public static final String DOUBLE_LATITUDE = "lat"; // Latitude of place
	public static final String DOUBLE_LONGITUDE = "lng"; // Longitude of place
	public static final String DOUBLE_RATING = "rating"; // The "rating" of the place

	// INTEGERS
	public static final String INTEGER_PRICE_LEVEL = "price_level"; // How expensive the place is
	public static final String INTEGER_DAY = "day"; // Day represented by an int 0-6, starting with Sunday
	public static final String INTEGER_WIDTH = "width"; // Used for describing a photo's width
	public static final String INTEGER_HEIGHT = "height"; // Used for describing a photo's height
	public static final String INTEGER_RATING = "rating"; // Reviews use integer ratings
	public static final String INTEGER_UTC_OFFSET = "utc_offset"; // Minutes that a location is of from UTC
	public static final String INTEGER_ACCURACY = "accuracy"; // Accuracy of location, in meters
	public static final String INTEGER_OFFSET = "offset"; // Used for autocomplete predictions
	public static final String INTEGER_LENGTH = "length"; // Used for autocomplete substring length

	// LONGS
	public static final String LONG_START_TIME = "start_time"; // The start time for an event
	public static final String LONG_TIME = "time"; // Used for the date of a review
	public static final String LONG_DURATION = "duration"; // Returns the duration of an event

	// OBJECTS
	public static final String OBJECT_RESULT = "result"; // Used for responses with single results
	public static final String OBJECT_GEOMETRY = "geometry"; // Geographic information
	public static final String OBJECT_LOCATION = "location"; // Contains latitude and longitude coordinates
	public static final String OBJECT_HOURS = "opening_hours"; // Contains open_now
	public static final String OBJECT_OPEN = "open"; // The beginning of a period
	public static final String OBJECT_CLOSE = "close"; // The end of a period

	// STATUS CODES
	public static final String STATUS_OK = "OK"; // Indicates the request was successful.
	public static final String STATUS_ZERO_RESULTS = "ZERO_RESULTS"; // Indicates that nothing went wrong during the request, but no places were found
	public static final String STATUS_OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT"; // Indicates that you are over the quota for queries to Google Places API
	public static final String STATUS_REQUEST_DENIED = "REQUEST_DENIED"; // Indicates that the request was denied
	public static final String STATUS_INVALID_REQUEST = "INVALID_REQUEST"; // Indicates the request was invalid, generally indicating a missing parameter
	public static final String STATUS_UNKNOWN_ERROR = "UNKNOWN_ERROR"; // Indicates an internal server-side error
	public static final String STATUS_NOT_FOUND = "NOT_FOUND"; // Indicates that a resource was could not be resolved

	// STRINGS
	public static final String STRING_ID = "id"; // The unique, stable, identifier for this place
	public static final String STRING_ICON = "icon"; // Url to the icon to represent this place
	public static final String STRING_NAME = "name"; // The name of the place
	public static final String STRING_ADDRESS = "formatted_address"; // The address of the place
	public static final String STRING_REFERENCE = "reference"; // The reference to use to get more details about the place
	public static final String STRING_VICINITY = "vicinity"; // The vicinity of which the place can be found (sometimes replaces formatted_address)
	public static final String STRING_EVENT_ID = "event_id"; // The unique identifier for an event at a place
	public static final String STRING_SUMMARY = "summary"; // The summary of an event
	public static final String STRING_URL = "url"; // The url for an event at a place
	public static final String STRING_STATUS = "status"; // A root element to indicate the status of the query.
	public static final String STRING_NEXT_PAGE_TOKEN = "next_page_token"; // A token used for getting the next page of results
	public static final String STRING_PHONE_NUMBER = "formatted_phone_number"; // The phone number of the place
	public static final String STRING_INTERNATIONAL_PHONE_NUMBER = "international_phone_number"; // The phone number of the place with an international country code
	public static final String STRING_WEBSITE = "website"; // The website associated with a place
	public static final String STRING_TIME = "time"; // A time represented by an hhmm format
	public static final String STRING_PHOTO_REFERENCE = "photo_reference"; // A reference to an actual photo
	public static final String STRING_LONG_NAME = "long_name"; // Represents an address component's long name
	public static final String STRING_SHORT_NAME = "short_name"; // Represents an address component's short name
	public static final String STRING_TYPE = "type"; // Used for singular types in review aspects
	public static final String STRING_AUTHOR_NAME = "author_name"; // Name of a review author
	public static final String STRING_AUTHOR_URL = "author_url"; // Url of author
	public static final String STRING_LANGUAGE = "language"; // Language for review localization
	public static final String STRING_TEXT = "text"; // Review content
	public static final String STRING_DESCRIPTION = "description"; // Description of autocomplete prediction
	public static final String STRING_VALUE = "value"; // Used for autocomplete terms

	private List<Place> getPlaces(String uri, String method, boolean sensor, int limit)
			throws IOException, JSONException {

		limit = Math.min(limit, MAXIMUM_RESULTS); // max of 60 results possible
		int pages = limit / MAXIMUM_PAGE_RESULTS;

		List<Place> places = new ArrayList<Place>();
		// new request for each page
		for (int i = 0; i < pages; i++) {
			String raw = get(client, uri);
			String nextPage = parse(this, places, raw, limit);
			if (nextPage != null) {
				limit -= MAXIMUM_PAGE_RESULTS;
				uri = String.format("%s%s/json?pagetoken=%s&sensor=%b&key=%s",
						API_URL, method, nextPage, sensor, apiKey);
				sleep(3000); // Page tokens have a delay before they are available
			} else {
				break;
			}
		}

		return places;
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static void checkStatus(String statusCode) {
		if (statusCode.equals(STATUS_OVER_QUERY_LIMIT)) {
			throw new GooglePlacesException(statusCode,
					"You have fufilled the maximum amount of queries permitted by your API key.");
		} else if (statusCode.equals(STATUS_REQUEST_DENIED)) {
			throw new GooglePlacesException(statusCode,
					"The request to the server was denied. (are you missing the sensor parameter?)");
		} else if (statusCode.equals(STATUS_INVALID_REQUEST)) {
			throw new GooglePlacesException(statusCode,
					"The request sent to the server was invalid. (are you missing a required parameter?)");
		} else if (statusCode.equals(STATUS_UNKNOWN_ERROR)) {
			throw new GooglePlacesException(statusCode,
					"An internal server-side error occurred. Trying again may be successful.");
		} else if (statusCode.equals(STATUS_NOT_FOUND)) {
			throw new GooglePlacesException(statusCode, "The requested resource was not found.");
		}
	}

	/**
	 * Parses the specified raw json String into a list of places.
	 *
	 * @param places to parse into
	 * @param str    raw json
	 * @param limit  the maximum amount of places to return
	 * @return list of parsed places
	 */
	public static String parse(GooglePlaces client, List<Place> places, String str, int limit) {

		// parse json
		JSONObject json = new JSONObject(str);

		// check root elements
		String statusCode = json.getString(STRING_STATUS);
		checkStatus(statusCode);
		if (statusCode.equals(STATUS_ZERO_RESULTS)) {
			return null;
		}

		JSONArray results = json.getJSONArray(ARRAY_RESULTS);
		parseResults(client, places, results, limit);

		return json.optString(STRING_NEXT_PAGE_TOKEN, null);
	}

	private static void parseResults(GooglePlaces client, List<Place> places, JSONArray results, int limit) {
		limit = Math.min(limit, MAXIMUM_PAGE_RESULTS);
		for (int i = 0; i < limit; i++) {

			// reached the end of the page
			if (i >= results.length()) {
				return;
			}

			JSONObject result = results.getJSONObject(i);

			// location
			JSONObject location = result.getJSONObject(OBJECT_GEOMETRY).getJSONObject(OBJECT_LOCATION);
			double lat = location.getDouble(DOUBLE_LATITUDE);
			double lon = location.getDouble(DOUBLE_LONGITUDE);

			String id = result.getString(STRING_ID);
			String iconUrl = result.optString(STRING_ICON, null);
			String name = result.getString(STRING_NAME);
			String addr = result.optString(STRING_ADDRESS, null);
			double rating = result.optDouble(DOUBLE_RATING, -1);
			String reference = result.optString(STRING_REFERENCE, null);
			String vicinity = result.optString(STRING_VICINITY, null);

			// see if the place is open, fail-safe if opening_hours is not present
			JSONObject hours = result.optJSONObject(OBJECT_HOURS);
			boolean hoursDefined = hours != null && hours.has(BOOLEAN_OPENED);
			Status status = Status.NONE;
			if (hoursDefined) {
				boolean opened = hours.getBoolean(BOOLEAN_OPENED);
				status = opened ? Status.OPENED : Status.CLOSED;
			}

			// get the price level for the place, fail-safe if not defined
			boolean priceDefined = result.has(INTEGER_PRICE_LEVEL);
			Price price = Price.NONE;
			if (priceDefined) {
				price = Price.values()[result.getInt(INTEGER_PRICE_LEVEL)];
			}

			// the place "types"
			List<String> types = new ArrayList<String>();
			JSONArray jsonTypes = result.optJSONArray(ARRAY_TYPES);
			if (jsonTypes != null) {
				for (int a = 0; a < jsonTypes.length(); a++) {
					types.add(jsonTypes.getString(a));
				}
			}

			Place place = new Place();

			// get the events going on at the place
			List<Event> events = new ArrayList<Event>();
			JSONArray jsonEvents = result.optJSONArray(ARRAY_EVENTS);
			if (jsonEvents != null) {
				for (int b = 0; b < jsonEvents.length(); b++) {
					JSONObject event = jsonEvents.getJSONObject(b);
					String eventId = event.getString(STRING_EVENT_ID);
					String summary = event.optString(STRING_SUMMARY, null);
					String url = event.optString(STRING_URL, null);
					events.add(new Event().setId(eventId).setSummary(summary).setUrl(url).setPlace(place));
				}
			}

			// build a place object
			places.add(place.setClient(client).setId(id).setLatitude(lat).setLongitude(lon).setIconUrl(iconUrl).setName(name)
					.setAddress(addr).setRating(rating).setReferenceId(reference).setStatus(status).setPrice(price)
					.addTypes(types).setVicinity(vicinity).addEvents(events).setJson(result));
		}
	}
}
