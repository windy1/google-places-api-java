package se.walkercrou.places;

import se.walkercrou.places.exception.GooglePlacesException;

import java.util.List;

/**
 * A Java binding for the Google Places API:
 * <a href="https://developers.google.com/places/">https://developers.google.com/places/</a>
 */
public interface GooglePlacesInterface extends Types, Statuses {

    /**
     * The URL of which Google Places API is located.
     */
    String API_URL = "https://maps.googleapis.com/maps/api/place/";

    /**
     * The maximum amount of results that can be on one single page.
     */
    int MAXIMUM_PAGE_RESULTS = 20;

    /**
     * The default amount of results that will be returned in a single request.
     */
    int DEFAULT_RESULTS = MAXIMUM_PAGE_RESULTS;

    /**
     * The maximum results that can be returned.
     */
    int MAXIMUM_RESULTS = 60;
    
    /**
     * The maximum radar results that can be returned.
     */
    int MAXIMUM_RADAR_RESULTS = 200;

    /**
     * The maximum search radius for places.
     */
    double MAXIMUM_RADIUS = 50000;

    // METHODS
    String METHOD_NEARBY_SEARCH = "nearbysearch";
    String METHOD_TEXT_SEARCH = "textsearch";
    String METHOD_RADAR_SEARCH = "radarsearch";
    String METHOD_DETAILS = "details";
    String METHOD_ADD = "add";
    String METHOD_DELETE = "delete";
    String METHOD_AUTOCOMPLETE = "autocomplete";
    String METHOD_QUERY_AUTOCOMPLETE = "queryautocomplete";

    int MAX_PHOTO_SIZE = 1600;

    /**
     * Types of place
     */
    String ARRAY_TYPES = "types";

    /**
     * Array for results
     */
    String ARRAY_RESULTS = "results";

    /**
     * Signifies the hours of operation of a place
     */
    String ARRAY_PERIODS = "periods";

    /**
     * Array containing photo information
     */
    String ARRAY_PHOTOS = "photos";

    /**
     * An array containing each element in a places full address
     */
    String ARRAY_ADDRESS_COMPONENTS = "address_components";

    /**
     * Array of reviews of a Place
     */
    String ARRAY_REVIEWS = "reviews";

    /**
     * Array of aspects of a review
     */
    String ARRAY_ASPECTS = "aspects";

    /**
     * Array of autocomplete predictions
     */
    String ARRAY_PREDICTIONS = "predictions";

    /**
     * Array of terms describing a autocomplete prediction description
     */
    String ARRAY_TERMS = "terms";

    /**
     * Used for matching matched substrings for autocompletion
     */
    String ARRAY_MATCHED_SUBSTRINGS = "matched_substrings";

    /**
     * Defines alternate entries for a specified place.
     */
    String ARRAY_ALT_IDS = "alt_ids";

    /**
     * If the place is opened now
     */
    String BOOLEAN_OPENED = "open_now";

    /**
     * Latitude of place
     */
    String DOUBLE_LATITUDE = "lat";

    /**
     * Longitude of place
     */
    String DOUBLE_LONGITUDE = "lng";

    /**
     * The "rating" of the place
     */
    String DOUBLE_RATING = "rating";

    /**
     * How expensive the place is
     */
    String INTEGER_PRICE_LEVEL = "price_level";

    /**
     * Day represented by an int 0-6, starting with Sunday
     */
    String INTEGER_DAY = "day";

    /**
     * Used for describing a photo's width
     */
    String INTEGER_WIDTH = "width";

    /**
     * Used for describing a photo's height
     */
    String INTEGER_HEIGHT = "height";

    /**
     * Reviews use integer ratings
     */
    String INTEGER_RATING = "rating";

    /**
     * Minutes that a location is of from UTC
     */
    String INTEGER_UTC_OFFSET = "utc_offset";

    /**
     * Accuracy of location, in meters
     */
    String INTEGER_ACCURACY = "accuracy";

    /**
     * Used for autocomplete predictions
     */
    String INTEGER_OFFSET = "offset";

    /**
     * Used for autocomplete substring length
     */
    String INTEGER_LENGTH = "length";

    /**
     * Used for the date of a review
     */
    String LONG_TIME = "time";

    /**
     * Used for responses with single results
     */
    String OBJECT_RESULT = "result";

    /**
     * Geographic information
     */
    String OBJECT_GEOMETRY = "geometry";

    /**
     * Contains latitude and longitude coordinates
     */
    String OBJECT_LOCATION = "location";

    /**
     * Contains open_now
     */
    String OBJECT_HOURS = "opening_hours";

    /**
     * The beginning of a period
     */
    String OBJECT_OPEN = "open";

    /**
     * The end of a period
     */
    String OBJECT_CLOSE = "close";

    /**
     * The unique, stable, identifier for this place
     */
    String STRING_PLACE_ID = "place_id";

    /**
     * Url to the icon to represent this place
     */
    String STRING_ICON = "icon";

    /**
     * The name of the place
     */
    String STRING_NAME = "name";

    /**
     * The address of the place
     */
    String STRING_ADDRESS = "formatted_address";

    /**
     * The vicinity of which the place can be found (sometimes replaces formatted_address)
     */
    String STRING_VICINITY = "vicinity";

    /**
     * The url for an event at a place
     */
    String STRING_URL = "url";

    /**
     * A root element to indicate the status of the query.
     */
    String STRING_STATUS = "status";

    /**
     * A message that may or may not be present when an error occurs.
     */
    String STRING_ERROR_MESSAGE = "error_message";

    /**
     * A token used for getting the next page of results
     */
    String STRING_NEXT_PAGE_TOKEN = "next_page_token";

    /**
     * The phone number of the place
     */
    String STRING_PHONE_NUMBER = "formatted_phone_number";

    /**
     * The phone number of the place with an international country code
     */
    String STRING_INTERNATIONAL_PHONE_NUMBER = "international_phone_number";

    /**
     * The website associated with a place
     */
    String STRING_WEBSITE = "website";

    /**
     * A time represented by an hhmm format
     */
    String STRING_TIME = "time";

    /**
     * A reference to an actual photo
     */
    String STRING_PHOTO_REFERENCE = "photo_reference";

    /**
     * Represents an address component's long name
     */
    String STRING_LONG_NAME = "long_name";

    /**
     * Represents an address component's short name
     */
    String STRING_SHORT_NAME = "short_name";

    /**
     * Name of a review author
     */
    String STRING_AUTHOR_NAME = "author_name";

    /**
     * Url of author
     */
    String STRING_AUTHOR_URL = "author_url";

    /**
     * Language for review localization
     */
    String STRING_LANGUAGE = "language";

    /**
     * Review content
     */
    String STRING_TEXT = "text";

    /**
     * Description of autocomplete prediction
     */
    String STRING_DESCRIPTION = "description";

    /**
     * Used for autocomplete terms
     */
    String STRING_VALUE = "value";

    /**
     * Used for singular types in review aspects
     */
    String STRING_TYPE = "type";

    /**
     * Restricts the results to places matching at least one of the specified types.
     */
    String STRING_TYPES = "types";

    /**
     * Defines what scope a location resides in.
     *
     * @see se.walkercrou.places.Scope
     */
    String STRING_SCOPE = "scope";

    /**
     * Returns true if the client is running in debug mode.
     *
     * @return true if debug mode
     */
    boolean isDebugModeEnabled();

    /**
     * Sets if the client should run in debug mode.
     *
     * @param debugModeEnabled true if in debug mode
     */
    void setDebugModeEnabled(boolean debugModeEnabled);

    /**
     * Returns the API key associated with this GooglePlaces object.
     *
     * @return api key
     */
    String getApiKey();

    /**
     * Sets the API key associated with this GooglePlaces object.
     *
     * @param apiKey to set
     */
    void setApiKey(String apiKey);

    /**
     * Returns the interface that handles HTTP requests to Google's server.
     *
     * @return request handler for HTTP traffic
     */
    RequestHandler getRequestHandler();

    /**
     * Sets the request handler to delegate HTTP traffic.
     *
     * @param requestHandler to handle HTTP traffic
     */
    void setRequestHandler(RequestHandler requestHandler);

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
    List<Place> getNearbyPlaces(double lat, double lng, double radius, int limit, Param... extraParams);

    /**
     * Returns the places at the specified latitude and longitude within the specified radius. No more than
     * {@link #DEFAULT_RESULTS} will be returned and no more than one HTTP GET request will be sent.
     *
     * @param lat         latitude
     * @param lng         longitude
     * @param radius      radius
     * @param extraParams any extra parameters to include in the request URL
     * @return a list of places that were found
     */
    List<Place> getNearbyPlaces(double lat, double lng, double radius, Param... extraParams);

    /**
     * Returns the places at the specified latitude and longitude in order of proximity to the specified location. If
     * the specified limit is greater than {@link #MAXIMUM_PAGE_RESULTS}, multiple HTTP GET requests may be made if
     * necessary. One or more of the parameters 'keyword', 'name', or 'types' is required or else a
     * {@link se.walkercrou.places.exception.GooglePlacesException} will be thrown.
     *
     * @param lat    latitude
     * @param lng    longitude
     * @param limit  the maximum amount of places to return
     * @param params parameters to append to url, one or more being 'keyword', 'name', or 'types'
     * @return list of places in order of proximity to the specified location
     * @throws se.walkercrou.places.exception.GooglePlacesException if 'keyword', 'name' or 'types' is not included.
     */
    List<Place> getNearbyPlacesRankedByDistance(double lat, double lng, int limit, Param... params)
            throws GooglePlacesException;

    /**
     * Returns the places at the specified latitude and longitude in order of proximity to the specified location. No
     * more than {@link #DEFAULT_RESULTS} will be returned and no more than one HTTP GET request will be sent. One or
     * more of the parameters 'keyword', 'name', or 'types' is required or else a
     * {@link se.walkercrou.places.exception.GooglePlacesException} will be thrown.
     *
     * @param lat    latitude
     * @param lng    longitude
     * @param params parameters to append to url, one or more being 'keyword', 'name', or 'types'
     * @return list of places in order of proximity to the specified location
     * @throws se.walkercrou.places.exception.GooglePlacesException if 'keyword', 'name' or 'types' is not included.
     */
    List<Place> getNearbyPlacesRankedByDistance(double lat, double lng, Param... params)
            throws GooglePlacesException;

    /**
     * Returns the places that match the specified search query.  If the specified limit
     * is greater than {@link #MAXIMUM_PAGE_RESULTS}, multiple HTTP GET requests may be made if necessary.
     *
     * @param query       search query
     * @param limit       the maximum amount of places to return
     * @param extraParams any extra parameters to include in the request URL
     * @return a list of places that were found
     */
    List<Place> getPlacesByQuery(String query, int limit, Param... extraParams);

    /**
     * Returns the places that match the specified search query. No more than {@link #DEFAULT_RESULTS} will be returned
     * and no more than one HTTP GET request will be sent. The 'sensor' parameter defaults to false.
     *
     * @param query       search query
     * @param extraParams any extra parameters to include in the request URL
     * @return a list of places that were found
     */
    List<Place> getPlacesByQuery(String query, Param... extraParams);

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
    List<Place> getPlacesByRadar(double lat, double lng, double radius, int limit, Param... extraParams);

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
    List<Place> getPlacesByRadar(double lat, double lng, double radius, Param... extraParams);

    /**
     * Returns the place specified by the 'placeid'.
     *
     * @param placeId     to get
     * @param extraParams params to append to url
     * @return place
     */
    Place getPlaceById(String placeId, Param... extraParams);

    /**
     * Adds a new place to the Places API and gets the newly created place if returnPlace is set to true.
     *
     * @param builder     to get place details from
     * @param returnPlace true if the newly created place should be returned
     * @param extraParams to append to request url
     * @return newly created place
     */
    Place addPlace(PlaceBuilder builder, boolean returnPlace, Param... extraParams);

    /**
     * Deletes the place of the specified placeId.
     *
     * @param placeId     place id
     * @param extraParams params to append to url
     */
    void deletePlaceById(String placeId, Param... extraParams);

    /**
     * Deletes the specified place.
     *
     * @param place to delete
     */
    void deletePlace(Place place, Param... extraParams);

    /**
     * Returns a list of auto-complete predictions for searching for a specific place. The 'offset' is the position, in
     * the input term, of the last character that the service uses to match predictions. For example, if the input is
     * 'Google' and the offset is 3, the service will match on 'Goo'. The string determined by the offset is matched
     * against the first word in the input term only. For example, if the input term is 'Google abc' and the offset is
     * 3, the service will attempt to match against 'Goo abc'. If no offset is supplied, the service will use the whole
     * term. The offset should generally be set to the position of the text caret. The lat, lng, and radius parameter
     * specify and area in which you would like to search.
     *
     * @param input       user input
     * @param offset      offset of text caret
     * @param lat         latitude
     * @param lng         longitude
     * @param radius      radius
     * @param extraParams to append to request url
     * @return list of predictions
     */
    List<Prediction> getPlacePredictions(String input, int offset, int lat, int lng, int radius,
                                         Param... extraParams);

    /**
     * Returns a list of auto-complete predictions for searching for a specific place. The 'offset' is the position, in
     * the input term, of the last character that the service uses to match predictions. For example, if the input is
     * 'Google' and the offset is 3, the service will match on 'Goo'. The string determined by the offset is matched
     * against the first word in the input term only. For example, if the input term is 'Google abc' and the offset is
     * 3, the service will attempt to match against 'Goo abc'. If no offset is supplied, the service will use the whole
     * term. The offset should generally be set to the position of the text caret.
     *
     * @param input  user input
     * @param offset offset of text caret
     * @return list of predictions
     */
    List<Prediction> getPlacePredictions(String input, int offset, Param... extraParams);

    /**
     * Returns a list of auto-complete predictions for searching for a specific place.
     *
     * @param input       user input
     * @param extraParams extra params to include in url
     * @return list of predictions
     */
    List<Prediction> getPlacePredictions(String input, Param... extraParams);

    /**
     * Returns a list of auto-complete predictions for searching for a place by a query. The 'offset' is the position,
     * in the input term, of the last character that the service uses to match predictions. For example, if the input is
     * 'Google' and the offset is 3, the service will match on 'Goo'. The string determined by the offset is matched
     * against the first word in the input term only. For example, if the input term is 'Google abc' and the offset is
     * 3, the service will attempt to match against 'Goo abc'. If no offset is supplied, the service will use the whole
     * term. The offset should generally be set to the position of the text caret. The lat, lng, and radius parameter
     * specify and area in which you would like to search.
     *
     * @param input       user input
     * @param extraParams to append to request url
     * @return list of predictions
     */
    List<Prediction> getQueryPredictions(String input, int offset, int lat, int lng, int radius,
                                         Param... extraParams);

    /**
     * Returns a list of auto-complete predictions for searching for a place by a query. The 'offset' is the position,
     * in the input term, of the last character that the service uses to match predictions. For example, if the input is
     * 'Google' and the offset is 3, the service will match on 'Goo'. The string determined by the offset is matched
     * against the first word in the input term only. For example, if the input term is 'Google abc' and the offset is
     * 3, the service will attempt to match against 'Goo abc'. If no offset is supplied, the service will use the whole
     * term. The offset should generally be set to the position of the text caret.
     *
     * @param input       user input
     * @param offset      offset of text caret
     * @param extraParams extra params to append to url
     * @return list of predictions
     */
    List<Prediction> getQueryPredictions(String input, int offset, Param... extraParams);

    /**
     * Returns a list of auto-complete predictions for searching for a place by a query.
     *
     * @param input       user input
     * @param extraParams extra parameters to append to url
     * @return list of predictions
     */
    List<Prediction> getQueryPredictions(String input, Param... extraParams);

}
