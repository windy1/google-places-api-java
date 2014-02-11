package me.windwaker.places;

import me.windwaker.places.exception.GooglePlacesException;
import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static me.windwaker.places.HttpUtil.DEFAULT_CLIENT;
import static me.windwaker.places.HttpUtil.getResponse;

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

	public static String addExtraParams(String base, Param... extraParams) {
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
	 * @throws IOException
	 * @throws JSONException
	 */
	public List<Place> getNearbyPlaces(double lat, double lng, double radius, int limit, Param... extraParams)
			throws IOException, JSONException {
		String uri = String.format("%s%s/json?key=%s&location=%f,%f&radius=%f&sensor=%b",
				API_URL, METHOD_NEARBY_SEARCH, apiKey, lat, lng, radius, sensor);
		uri = addExtraParams(uri, extraParams);
		return getPlaces(uri, METHOD_NEARBY_SEARCH, sensor, limit);
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
	 * @throws IOException
	 * @throws JSONException
	 */
	public List<Place> getNearbyPlaces(double lat, double lng, double radius, Param... extraParams)
			throws IOException, JSONException {
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
	 * @throws IOException
	 * @throws JSONException
	 */
	public List<Place> getPlacesByQuery(String query, int limit, Param... extraParams)
			throws IOException, JSONException {
		// build base uri
		String uri = String.format("%stextsearch/json?query=%s&key=%s&sensor=%b",
				API_URL, query, apiKey, sensor);
		uri = addExtraParams(uri, extraParams);
		return getPlaces(uri, METHOD_TEXT_SEARCH, sensor, limit);
	}

	/**
	 * Returns the places that match the specified search query. No more than {@link #DEFAULT_RESULTS} will be returned
	 * and no more than one HTTP GET request will be sent. The 'sensor' parameter defaults to false.
	 *
	 * @param query       search query
	 * @param extraParams any extra parameters to include in the request URL
	 * @return a list of places that were found
	 * @throws IOException
	 * @throws JSONException
	 */
	public List<Place> getPlacesByQuery(String query, Param... extraParams) throws IOException, JSONException {
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
	 * @throws IOException
	 * @throws JSONException
	 */
	public List<Place> getPlacesByRadar(double lat, double lng, double radius, int limit, Param... extraParams)
			throws IOException, JSONException {
		String uri = String.format("%sradarsearch/json?key=%s&location=%f,%f&radius=%f&sensor=%b",
				API_URL, apiKey, lat, lng, radius, sensor);
		uri = addExtraParams(uri, extraParams);
		return getPlaces(uri, METHOD_RADAR_SEARCH, sensor, limit);
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
	 * @throws IOException
	 * @throws JSONException
	 */
	public List<Place> getPlacesByRadar(double lat, double lng, double radius, Param... extraParams)
			throws IOException, JSONException {
		return getPlacesByRadar(lat, lng, radius, MAXIMUM_RESULTS, extraParams);
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
	public static final String INTEGER_RATING = "rating"; // Reviews use integer ratings.
	public static final String INTEGER_UTC_OFFSET = "utc_offset"; // Minutes that a location is of from UTC

	// LONGS
	public static final String LONG_START_TIME = "start_time"; // The start time for an event
	public static final String LONG_TIME = "time"; // Used for the date of a review

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

	private List<Place> getPlaces(String uri, String method, boolean sensor, int limit)
			throws IOException, JSONException {

		limit = Math.min(limit, MAXIMUM_RESULTS); // max of 60 results possible
		int pages = limit / 20;

		List<Place> places = new ArrayList<Place>();
		// new request for each page
		for (int i = 0; i < pages; i++) {
			String raw = getResponse(client, uri);
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

	/**
	 * Parses the specified raw json String into a list of places.
	 *
	 * @param places to parse into
	 * @param str    raw json
	 * @param limit  the maximum amount of places to return
	 * @return list of parsed places
	 * @throws JSONException
	 */
	public static String parse(GooglePlaces client, List<Place> places, String str, int limit) throws JSONException {

		// parse json
		JSONObject json = new JSONObject(str);

		// check root elements
		String statusCode = json.getString(STRING_STATUS);
		if (statusCode.equals(STATUS_ZERO_RESULTS)) {
			return null;
		} else if (statusCode.equals(STATUS_OVER_QUERY_LIMIT)) {
			throw new GooglePlacesException(statusCode,
					"You have fufilled the maximum amount of queries permitted by your API key.");
		} else if (statusCode.equals(STATUS_REQUEST_DENIED)) {
			throw new GooglePlacesException(statusCode,
					"The request to the server was denied. (are you missing the sensor parameter?)");
		} else if (statusCode.equals(STATUS_INVALID_REQUEST)) {
			throw new GooglePlacesException(statusCode,
					"The request sent to the server was invalid. (are you missing a required parameter?)");
		}

		JSONArray results = json.getJSONArray(ARRAY_RESULTS);
		parseResults(client, places, results, limit);

		return json.optString(STRING_NEXT_PAGE_TOKEN, null);
	}

	private static int parseResults(GooglePlaces client, List<Place> places, JSONArray results, int limit) throws JSONException {
		limit = Math.min(limit, MAXIMUM_PAGE_RESULTS);
		for (int i = 0; i < limit; i++) {

			// reached the end of the page
			if (i >= results.length()) {
				return results.length();
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

			// get the events going on at the place
			List<Event> events = new ArrayList<Event>();
			JSONArray jsonEvents = result.optJSONArray(ARRAY_EVENTS);
			if (jsonEvents != null) {
				for (int b = 0; b < jsonEvents.length(); b++) {
					JSONObject event = jsonEvents.getJSONObject(b);
					String eventId = event.getString(STRING_EVENT_ID);
					String summary = event.optString(STRING_SUMMARY, null);
					String url = event.optString(STRING_URL, null);
					events.add(new Event(eventId).setSummary(summary).setUrl(url));
				}
			}

			// build a place object
			places.add(new Place(client, id).setLatitude(lat).setLongitude(lon).setIconUrl(iconUrl).setName(name)
					.setAddress(addr).setRating(rating).setReferenceId(reference).setStatus(status).setPrice(price)
					.addTypes(types).setVicinity(vicinity).addEvents(events));
		}

		return limit;
	}
}
