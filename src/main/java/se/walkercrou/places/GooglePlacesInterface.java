package se.walkercrou.places;

import java.util.Collection;
import java.util.List;

/**
 * A Java binding for the Google Places API:
 * <a href="https://developers.google.com/places/">https://developers.google.com/places/</a>
 */
public interface GooglePlacesInterface extends Types, Statuses {

    /**
     * The URL of which Google Places API is located.
     */
    public static final String API_URL = "https://maps.googleapis.com/maps/api/place/";

    /**
     * The maximum amount of results that can be on one single page.
     */
    public static final int MAXIMUM_PAGE_RESULTS = 20;

    /**
     * The default amount of results that will be returned in a single request.
     */
    public static final int DEFAULT_RESULTS = MAXIMUM_PAGE_RESULTS;

    /**
     * The maximum results that can be returned.
     */
    public static final int MAXIMUM_RESULTS = 60;

    /**
     * The maximum search radius for places.
     */
    public static final double MAXIMUM_RADIUS = 50000;

    // METHODS
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
    public static final String METHOD_QUERY_AUTOCOMPLETE = "queryautocomplete";

    public static final int MAX_PHOTO_SIZE = 1600;

    /**
     * Types of place
     */
    public static final String ARRAY_TYPES = "types";

    /**
     * Array for results
     */
    public static final String ARRAY_RESULTS = "results";

    /**
     * The events occurring at the place
     */
    public static final String ARRAY_EVENTS = "events";

    /**
     * Signifies the hours of operation of a place
     */
    public static final String ARRAY_PERIODS = "periods";

    /**
     * Array containing photo information
     */
    public static final String ARRAY_PHOTOS = "photos";

    /**
     * An array containing each element in a places full address
     */
    public static final String ARRAY_ADDRESS_COMPONENTS = "address_components";

    /**
     * Array of reviews of a Place
     */
    public static final String ARRAY_REVIEWS = "reviews";

    /**
     * Array of aspects of a review
     */
    public static final String ARRAY_ASPECTS = "aspects";

    /**
     * Array of autocomplete predictions
     */
    public static final String ARRAY_PREDICTIONS = "predictions";

    /**
     * Array of terms describing a autocomplete prediction description
     */
    public static final String ARRAY_TERMS = "terms";

    /**
     * Used for matching matched substrings for autocompletion
     */
    public static final String ARRAY_MATCHED_SUBSTRINGS = "matched_substrings";

    /**
     * If the place is opened now
     */
    public static final String BOOLEAN_OPENED = "open_now";

    /**
     * Latitude of place
     */
    public static final String DOUBLE_LATITUDE = "lat";

    /**
     * Longitude of place
     */
    public static final String DOUBLE_LONGITUDE = "lng";

    /**
     * The "rating" of the place
     */
    public static final String DOUBLE_RATING = "rating";

    /**
     * How expensive the place is
     */
    public static final String INTEGER_PRICE_LEVEL = "price_level";

    /**
     * Day represented by an int 0-6, starting with Sunday
     */
    public static final String INTEGER_DAY = "day";

    /**
     * Used for describing a photo's width
     */
    public static final String INTEGER_WIDTH = "width";

    /**
     * Used for describing a photo's height
     */
    public static final String INTEGER_HEIGHT = "height";

    /**
     * Reviews use integer ratings
     */
    public static final String INTEGER_RATING = "rating";

    /**
     * Minutes that a location is of from UTC
     */
    public static final String INTEGER_UTC_OFFSET = "utc_offset";

    /**
     * Accuracy of location, in meters
     */
    public static final String INTEGER_ACCURACY = "accuracy";

    /**
     * Used for autocomplete predictions
     */
    public static final String INTEGER_OFFSET = "offset";

    /**
     * Used for autocomplete substring length
     */
    public static final String INTEGER_LENGTH = "length";

    /**
     * The start time for an event
     */
    public static final String LONG_START_TIME = "start_time";

    /**
     * Used for the date of a review
     */
    public static final String LONG_TIME = "time";

    /**
     * Returns the duration of an event
     */
    public static final String LONG_DURATION = "duration";

    /**
     * Used for responses with single results
     */
    public static final String OBJECT_RESULT = "result";

    /**
     * Geographic information
     */
    public static final String OBJECT_GEOMETRY = "geometry";

    /**
     * Contains latitude and longitude coordinates
     */
    public static final String OBJECT_LOCATION = "location";

    /**
     * Contains open_now
     */
    public static final String OBJECT_HOURS = "opening_hours";

    /**
     * The beginning of a period
     */
    public static final String OBJECT_OPEN = "open";

    /**
     * The end of a period
     */
    public static final String OBJECT_CLOSE = "close";

    /**
     * The unique, stable, identifier for this place
     */
    public static final String STRING_ID = "id";

    /**
     * The unique identifier for retrieving information about a place
     */
    public static final String STRING_PLACE_ID = "place_id";

    /**
     * Url to the icon to represent this place
     */
    public static final String STRING_ICON = "icon";

    /**
     * The name of the place
     */
    public static final String STRING_NAME = "name";

    /**
     * The address of the place
     */
    public static final String STRING_ADDRESS = "formatted_address";

    /**
     * The reference to use to get more details about the place
     */
    public static final String STRING_REFERENCE = "reference";

    /**
     * The vicinity of which the place can be found (sometimes replaces formatted_address)
     */
    public static final String STRING_VICINITY = "vicinity";

    /**
     * The unique identifier for an event at a place
     */
    public static final String STRING_EVENT_ID = "event_id";

    /**
     * The summary of an event
     */
    public static final String STRING_SUMMARY = "summary";

    /**
     * The url for an event at a place
     */
    public static final String STRING_URL = "url";

    /**
     * A root element to indicate the status of the query.
     */
    public static final String STRING_STATUS = "status";

    /**
     * A token used for getting the next page of results
     */
    public static final String STRING_NEXT_PAGE_TOKEN = "next_page_token";

    /**
     * The phone number of the place
     */
    public static final String STRING_PHONE_NUMBER = "formatted_phone_number";

    /**
     * The phone number of the place with an international country code
     */
    public static final String STRING_INTERNATIONAL_PHONE_NUMBER = "international_phone_number";

    /**
     * The website associated with a place
     */
    public static final String STRING_WEBSITE = "website";

    /**
     * A time represented by an hhmm format
     */
    public static final String STRING_TIME = "time";

    /**
     * A reference to an actual photo
     */
    public static final String STRING_PHOTO_REFERENCE = "photo_reference";

    /**
     * Represents an address component's long name
     */
    public static final String STRING_LONG_NAME = "long_name";

    /**
     * Represents an address component's short name
     */
    public static final String STRING_SHORT_NAME = "short_name";

    /**
     * Name of a review author
     */
    public static final String STRING_AUTHOR_NAME = "author_name";

    /**
     * Url of author
     */
    public static final String STRING_AUTHOR_URL = "author_url";

    /**
     * Language for review localization
     */
    public static final String STRING_LANGUAGE = "language";

    /**
     * Review content
     */
    public static final String STRING_TEXT = "text";

    /**
     * Description of autocomplete prediction
     */
    public static final String STRING_DESCRIPTION = "description";

    /**
     * Used for autocomplete terms
     */
    public static final String STRING_VALUE = "value";

    /**
     * Used for singular types in review aspects
     */
    public static final String STRING_TYPE = "type";

    /**
     * Restricts the results to places matching at least one of the specified types.
     */
    public static final String STRING_TYPES = "types";

    /**
     * Returns true if the device has a location sensor.
     *
     * @return false
     * @deprecated the sensor parameter is no longer required
     */
    @Deprecated
    public boolean isSensorEnabled();

    /**
     * Set this to true if the device you are using has a location detector such as GPS.
     *
     * @param sensor if sensor is enabled
     * @deprecated the sensor parameter is no longer required
     */
    @Deprecated
    public void setSensorEnabled(boolean sensor);

    /**
     * Returns true if the client is running in debug mode.
     *
     * @return true if debug mode
     */
    public boolean isDebugModeEnabled();

    /**
     * Sets if the client should run in debug mode.
     *
     * @param debugModeEnabled true if in debug mode
     */
    public void setDebugModeEnabled(boolean debugModeEnabled);

    /**
     * Returns the API key associated with this GooglePlaces object.
     *
     * @return api key
     */
    public String getApiKey();

    /**
     * Sets the API key associated with this GooglePlaces object.
     *
     * @param apiKey to set
     */
    public void setApiKey(String apiKey);

    /**
     * Returns the interface that handles HTTP requests to Google's server.
     *
     * @return request handler for HTTP traffic
     */
    public RequestHandler getRequestHandler();

    /**
     * Sets the request handler to delegate HTTP traffic.
     *
     * @param requestHandler to handle HTTP traffic
     */
    public void setRequestHandler(RequestHandler requestHandler);

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
    public List<Place> getNearbyPlaces(double lat, double lng, double radius, int limit, Param... extraParams);

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
    public List<Place> getNearbyPlaces(double lat, double lng, double radius, Param... extraParams);

    /**
     * Returns the places that match the specified search query.  If the specified limit
     * is greater than {@link #MAXIMUM_PAGE_RESULTS}, multiple HTTP GET requests may be made if necessary.
     *
     * @param query       search query
     * @param limit       the maximum amount of places to return
     * @param extraParams any extra parameters to include in the request URL
     * @return a list of places that were found
     */
    public List<Place> getPlacesByQuery(String query, int limit, Param... extraParams);

    /**
     * Returns the places that match the specified search query. No more than {@link #DEFAULT_RESULTS} will be returned
     * and no more than one HTTP GET request will be sent. The 'sensor' parameter defaults to false.
     *
     * @param query       search query
     * @param extraParams any extra parameters to include in the request URL
     * @return a list of places that were found
     */
    public List<Place> getPlacesByQuery(String query, Param... extraParams);

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
    public List<Place> getPlacesByRadar(double lat, double lng, double radius, int limit, Param... extraParams);

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
    public List<Place> getPlacesByRadar(double lat, double lng, double radius, Param... extraParams);

    /**
     * Returns the place using the specified reference ID.
     *
     * @param reference   id
     * @param extraParams to append to request url
     * @return place
     */
    public Place getPlace(String reference, Param... extraParams);

    /**
     * Adds a new place to Places API and gets the newly created place if returnPlace is set to true.
     *
     * @param name        of place
     * @param lang        language of place
     * @param lat         latitude coordinate
     * @param lng         longitude coordinate
     * @param accuracy    of coordinates in meters
     * @param types       collection of types
     * @param returnPlace true if the newly created place should be returned
     * @param extraParams to append to request url
     * @return newly created place
     */
    public Place addPlace(String name, String lang, double lat, double lng, int accuracy, Collection<String> types,
                          boolean returnPlace, Param... extraParams);

    /**
     * Adds a new place to Places API and returns the newly created Place.
     *
     * @param name     of place
     * @param lang     language of place
     * @param lat      latitude coordinate
     * @param lng      longitude coordinate
     * @param accuracy of coordinates in meters
     * @param types    collection of types
     * @return newly created place
     * \
     */
    public Place addPlace(String name, String lang, double lat, double lng, int accuracy, Collection<String> types,
                          Param... extraParams);

    /**
     * Adds a new place to Places API and returns the newly created Place.
     *
     * @param name     of place
     * @param lang     language of place
     * @param lat      latitude coordinate
     * @param lng      longitude coordinate
     * @param accuracy of coordinates in meters
     * @param type     type of place
     * @return newly created place
     */
    public Place addPlace(String name, String lang, double lat, double lng, int accuracy, String type,
                          Param... extraParams);

    /**
     * Adds a new place to Places API and returns the newly created Place.
     *
     * @param name     of place
     * @param lang     language of place
     * @param lat      latitude coordinate
     * @param lng      longitude coordinate
     * @param accuracy of coordinates in meters
     * @param type     type of place
     * @return newly created place
     */
    public Place addPlace(String name, String lang, double lat, double lng, int accuracy, String type,
                          boolean returnPlace, Param... extraParams);

    /**
     * Deletes the place specified by the specified reference ID.
     *
     * @param reference id
     */
    public void deletePlace(String reference, Param... extraParams);

    /**
     * Deletes the specified place.
     *
     * @param place to delete
     */
    public void deletePlace(Place place, Param... extraParams);

    /**
     * Bumps a place within the application. Bumps are reflected in your place searches for your application only.
     * Bumping a place makes it appear higher in the result set.
     *
     * @param place       to bump
     * @param extraParams to append to request url
     */
    public void bumpPlace(Place place, Param... extraParams);

    /**
     * Bumps an event within the application. Bumps are reflected in your place searches for your application only.
     * Bumping an event makes it appear higher in the result set.
     *
     * @param event       to bump
     * @param extraParams to append to request url
     */
    public void bumpEvent(Event event, Param... extraParams);

    /**
     * Returns the event at the specified place with the specified event id.
     *
     * @param place       reference to place the event is at
     * @param eventId     id of event
     * @param extraParams to append to request url
     * @return event
     */
    public Event getEvent(Place place, String eventId, Param... extraParams);

    /**
     * Adds a new Event to Google Places API.
     *
     * @param place       to add to
     * @param summary     of event
     * @param lang        language of event
     * @param url         url of event
     * @param duration    length of event in seconds
     * @param returnEvent if GET request should be made to retrieve the newly created event
     * @param extraParams to append to request url
     * @return newly created event
     */
    public Event addEvent(Place place, String summary, long duration, String lang, String url, boolean returnEvent,
                          Param... extraParams);

    /**
     * Adds a new Event to Google Places API.
     *
     * @param place       to add to
     * @param summary     of event
     * @param lang        language of event
     * @param url         url of event
     * @param duration    length of event in seconds
     * @param extraParams to append to request url
     * @return newly created event
     */
    public Event addEvent(Place place, String summary, long duration, String lang, String url, Param... extraParams);

    /**
     * Adds a new Event to Google Places API.
     *
     * @param place       to add to
     * @param summary     of event
     * @param duration    length of event in seconds
     * @param extraParams to append to request url
     * @return newly created event
     */
    public Event addEvent(Place place, String summary, long duration, boolean returnEvent, Param... extraParams);

    /**
     * Adds a new Event to Google Places API.
     *
     * @param place       to add to
     * @param summary     of event
     * @param duration    length of event in seconds
     * @param extraParams to append to request url
     * @return newly created event
     */
    public Event addEvent(Place place, String summary, long duration, Param... extraParams);

    /**
     * Deletes the specified event from Places API.
     *
     * @param placeReference that contains event
     * @param eventId        unique event id
     * @param extraParams    to append to request url
     */
    public void deleteEvent(String placeReference, String eventId, Param... extraParams);

    /**
     * Deletes the specified event from Places API.
     *
     * @param event       to delete
     * @param extraParams to append to request url
     */
    public void deleteEvent(Event event, Param... extraParams);

    /**
     * Returns a list of auto-complete predictions for searching for a specific place.
     *
     * @param input       user input
     * @param extraParams to append to request url
     * @return list of predictions
     */
    public List<Prediction> getPlacePredictions(String input, Param... extraParams);

    /**
     * Returns a list of auto-complete predictions for searching for a place by a query.
     *
     * @param input       user input
     * @param extraParams to append to request url
     * @return list of predictions
     */
    public List<Prediction> getQueryPredictions(String input, Param... extraParams);

}
