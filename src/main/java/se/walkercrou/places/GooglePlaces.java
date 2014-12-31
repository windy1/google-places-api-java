package se.walkercrou.places;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import se.walkercrou.places.exception.GooglePlacesException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A Java binding for the Google Places API:
 * <a href="https://developers.google.com/places/">https://developers.google.com/places/</a>
 */
public class GooglePlaces {
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
    public static final String STRING_TYPES = "types"; // Restricts the results to places matching at least one of the specified types.
    public static final String STRING_AUTHOR_NAME = "author_name"; // Name of a review author
    public static final String STRING_AUTHOR_URL = "author_url"; // Url of author
    public static final String STRING_LANGUAGE = "language"; // Language for review localization
    public static final String STRING_TEXT = "text"; // Review content
    public static final String STRING_DESCRIPTION = "description"; // Description of autocomplete prediction
    public static final String STRING_VALUE = "value"; // Used for autocomplete terms

    //TYPES
    public static final String TYPE_ACCOUNTING = "accounting";
    public static final String TYPE_AIRPORT = "airport";
    public static final String TYPE_AMUSEMENT_PARK = "amusement_park";
    public static final String TYPE_AQUARIUM = "aquarium";
    public static final String TYPE_ART_GALLERY = "art_gallery";
    public static final String TYPE_ATM = "atm";
    public static final String TYPE_BAKERY = "bakery";
    public static final String TYPE_BANK = "bank";
    public static final String TYPE_BAR = "bar";
    public static final String TYPE_BEAUTY_SALON = "beauty_salon";
    public static final String TYPE_BICYCLE_STORE = "bicycle_store";
    public static final String TYPE_BOOK_STORE = "book_store";
    public static final String TYPE_BOWLING_ALLEY = "bowling_alley";
    public static final String TYPE_BUS_STATION = "bus_station";
    public static final String TYPE_CAFE = "cafe";
    public static final String TYPE_CAMPGROUND = "campground";
    public static final String TYPE_CAR_DEALER = "car_dealer";
    public static final String TYPE_CAR_RENTAL = "car_rental";
    public static final String TYPE_CAR_REPAIR = "car_repair";
    public static final String TYPE_CAR_WASH = "car_wash";
    public static final String TYPE_CASINO = "casino";
    public static final String TYPE_CEMETERY = "cemetery";
    public static final String TYPE_CHURCH = "church";
    public static final String TYPE_CITY_HALL = "city_hall";
    public static final String TYPE_CLOTHING_STORE = "clothing_store";
    public static final String TYPE_CONVENIENCE_STORE = "convenience_store";
    public static final String TYPE_COURTHOUSE = "courthouse";
    public static final String TYPE_DENTIST = "dentist";
    public static final String TYPE_DEPARTMENT_STORE = "department_store";
    public static final String TYPE_DOCTOR = "doctor";
    public static final String TYPE_ELECTRICIAN = "electrician";
    public static final String TYPE_ELECTRONICS_STORE = "electronics_store";
    public static final String TYPE_EMBASSY = "embassy";
    public static final String TYPE_ESTABLISHMENT = "establishment";
    public static final String TYPE_FINANCE = "finance";
    public static final String TYPE_FIRE_STATION = "fire_station";
    public static final String TYPE_FLORIST = "florist";
    public static final String TYPE_FOOD = "food";
    public static final String TYPE_FUNERAL_HOME = "funeral_home";
    public static final String TYPE_FURNITURE_STORE = "furniture_store";
    public static final String TYPE_GAS_STATION = "gas_station";
    public static final String TYPE_GENERAL_CONTRACTOR = "general_contractor";
    public static final String TYPE_GROCERY_OR_SUPERMARKET = "grocery_or_supermarket";
    public static final String TYPE_GYM = "gym";
    public static final String TYPE_HAIR_CARE = "hair_care";
    public static final String TYPE_HARDWARE_STORE = "hardware_store";
    public static final String TYPE_HEALTH = "health";
    public static final String TYPE_HINDU_TEMPLE = "hindu_temple";
    public static final String TYPE_HOME_GOODS_STORE = "home_goods_store";
    public static final String TYPE_HOSPITAL = "hospital";
    public static final String TYPE_INSURANCE_AGENCY = "insurance_agency";
    public static final String TYPE_JEWELRY_STORE = "jewelry_store";
    public static final String TYPE_LAUNDRY = "laundry";
    public static final String TYPE_LAWYER = "lawyer";
    public static final String TYPE_LIBRARY = "library";
    public static final String TYPE_LIQUOR_STORE = "liquor_store";
    public static final String TYPE_LOCAL_GOVERNMENT_OFFICE = "local_government_office";
    public static final String TYPE_LOCKSMITH = "locksmith";
    public static final String TYPE_LODGING = "lodging";
    public static final String TYPE_MEAL_DELIVERY = "meal_delivery";
    public static final String TYPE_MEAL_TAKEAWAY = "meal_takeaway";
    public static final String TYPE_MOSQUE = "mosque";
    public static final String TYPE_MOVIE_RENTAL = "movie_rental";
    public static final String TYPE_MOVIE_THEATER = "movie_theater";
    public static final String TYPE_MOVING_COMPANY = "moving_company";
    public static final String TYPE_MUSEUM = "museum";
    public static final String TYPE_NIGHT_CLUB = "night_club";
    public static final String TYPE_PAINTER = "painter";
    public static final String TYPE_PARK = "park";
    public static final String TYPE_PARKING = "parking";
    public static final String TYPE_PET_STORE = "pet_store";
    public static final String TYPE_PHARMACY = "pharmacy";
    public static final String TYPE_PHYSIOTHERAPIST = "physiotherapist";
    public static final String TYPE_PLACE_OF_WORSHIP = "place_of_worship";
    public static final String TYPE_PLUMBER = "plumber";
    public static final String TYPE_POLICE = "police";
    public static final String TYPE_POST_OFFICE = "post_office";
    public static final String TYPE_REAL_ESTATE_AGENCY = "real_estate_agency";
    public static final String TYPE_RESTAURANT = "restaurant";
    public static final String TYPE_ROOFING_CONTRACTOR = "roofing_contractor";
    public static final String TYPE_RV_PARK = "rv_park";
    public static final String TYPE_SCHOOL = "school";
    public static final String TYPE_SHOE_STORE = "shoe_store";
    public static final String TYPE_SHOPPING_MALL = "shopping_mall";
    public static final String TYPE_SPA = "spa";
    public static final String TYPE_STADIUM = "stadium";
    public static final String TYPE_STORAGE = "storage";
    public static final String TYPE_STORE = "store";
    public static final String TYPE_SUBWAY_STATION = "subway_station";
    public static final String TYPE_SYNAGOGUE = "synagogue";
    public static final String TYPE_TAXI_STAND = "taxi_stand";
    public static final String TYPE_TRAIN_STATION = "train_station";
    public static final String TYPE_TRAVEL_AGENCY = "travel_agency";
    public static final String TYPE_UNIVERSITY = "university";
    public static final String TYPE_VETERINARY_CARE = "veterinary_care";
    public static final String TYPE_ZOO = "zoo";

    private String apiKey;
    private RequestHandler requestHandler;
    private boolean debugModeEnabled;

    /**
     * Creates a new GooglePlaces object using the specified API key and the specified {@link RequestHandler}.
     *
     * @param apiKey         that has been registered on the Google Developer Console
     * @param requestHandler to handle HTTP traffic
     */
    public GooglePlaces(String apiKey, RequestHandler requestHandler) {
        this.apiKey = apiKey;
        this.requestHandler = requestHandler;
    }

    /**
     * Creates a new GooglePlaces object using the specified API key.
     *
     * @param apiKey that has been registered on the Google Developer Console
     */
    public GooglePlaces(String apiKey) {
        this(apiKey, new DefaultRequestHandler());
    }
    /**
     * Creates a new GooglePlaces object using the specified API key and character encoding. Using a character encoding
     * other than UTF-8 is not advised.
     *
     * @param apiKey            that has been registered on the Google Developer Console
     * @param characterEncoding to parse data with
     */
    public GooglePlaces(String apiKey, String characterEncoding) {
        this(apiKey, new DefaultRequestHandler(characterEncoding));
    }

    private static String addExtraParams(String base, Param... extraParams) {
        for (Param param : extraParams) {
            base += "&" + param.name + (param.value != null ? "=" + param.value : "");
        }
        return base;
    }

    private static String buildUrl(String method, String params, Param... extraParams) {
        String url = String.format("%s%s/json?%s", API_URL, method, params);
        url = addExtraParams(url, extraParams);
        url = url.replace(' ', '+');
        return url;
    }

    protected static void checkStatus(String statusCode) {
        switch (statusCode) {
            case STATUS_OVER_QUERY_LIMIT:
                throw new GooglePlacesException(statusCode,
                        "You have fufilled the maximum amount of queries permitted by your API key.");
            case STATUS_REQUEST_DENIED:
                throw new GooglePlacesException(statusCode,
                        "The request to the server was denied. (are you missing the sensor parameter?)");
            case STATUS_INVALID_REQUEST:
                throw new GooglePlacesException(statusCode,
                        "The request sent to the server was invalid. (are you missing a required parameter?)");
            case STATUS_UNKNOWN_ERROR:
                throw new GooglePlacesException(statusCode,
                        "An internal server-side error occurred. Trying again may be successful.");
            case STATUS_NOT_FOUND:
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
            String name = result.optString(STRING_NAME);
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
            List<String> types = new ArrayList<>();
            JSONArray jsonTypes = result.optJSONArray(ARRAY_TYPES);
            if (jsonTypes != null) {
                for (int a = 0; a < jsonTypes.length(); a++) {
                    types.add(jsonTypes.getString(a));
                }
            }

            Place place = new Place();

            // get the events going on at the place
            List<Event> events = new ArrayList<>();
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

    /**
     * Returns true if the device has a location sensor.
     *
     * @return false
     * @deprecated the sensor parameter is no longer required
     */
    @Deprecated
    public boolean isSensorEnabled() {
        return false;
    }

    /**
     * Set this to true if the device you are using has a location detector such as GPS.
     *
     * @param sensor if sensor is enabled
     * @deprecated the sensor parameter is no longer required
     */
    @Deprecated
    public void setSensorEnabled(boolean sensor) {
    }

    /**
     * Returns true if the client is running in debug mode.
     *
     * @return true if debug mode
     */
    public boolean isDebugModeEnabled() {
        return debugModeEnabled;
    }

    /**
     * Sets if the client should run in debug mode.
     *
     * @param debugModeEnabled true if in debug mode
     */
    public void setDebugModeEnabled(boolean debugModeEnabled) {
        this.debugModeEnabled = debugModeEnabled;
    }

    private void debug(String msg) {
        if (debugModeEnabled)
            System.out.println(msg);
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
     * Sets the API key associated with this GooglePlaces object.
     *
     * @param apiKey to set
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Returns the interface that handles HTTP requests to Google's server.
     *
     * @return request handler for HTTP traffic
     */
    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

    /**
     * Sets the request handler to delegate HTTP traffic.
     *
     * @param requestHandler to handle HTTP traffic
     */
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

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
            String uri = buildUrl(METHOD_NEARBY_SEARCH, String.format("key=%s&location=%f,%f&radius=%f",
                    apiKey, lat, lng, radius), extraParams);
            return getPlaces(uri, METHOD_NEARBY_SEARCH, limit);
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
            String uri = buildUrl(METHOD_TEXT_SEARCH, String.format("query=%s&key=%s", query, apiKey),
                    extraParams);
            return getPlaces(uri, METHOD_TEXT_SEARCH, limit);
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
            String uri = buildUrl(METHOD_RADAR_SEARCH, String.format("key=%s&location=%f,%f&radius=%f",
                    apiKey, lat, lng, radius), extraParams);
            return getPlaces(uri, METHOD_RADAR_SEARCH, limit);
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
     * @param reference   id
     * @param extraParams to append to request url
     * @return place
     */
    public Place getPlace(String reference, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_DETAILS, String.format("key=%s&reference=%s", apiKey, reference),
                    extraParams);
            return Place.parseDetails(this, requestHandler.get(uri));
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

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
                          boolean returnPlace, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_ADD, String.format("key=%s", apiKey));
            //The extraParams should go into the Place.buildInput method as according to this website, we cannot add extraParams to the url for add Places:
            //https://developers.google.com/places/documentation/actions#PlaceReports
            JSONObject input = Place.buildInput(lat, lng, accuracy, name, types, lang, extraParams);
            HttpPost post = new HttpPost(uri);
            post.setEntity(new StringEntity(input.toString()));
            JSONObject response = new JSONObject(requestHandler.post(post));
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
                          Param... extraParams) {
        return addPlace(name, lang, lat, lng, accuracy, types, true, extraParams);
    }

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
                          Param... extraParams) {
        return addPlace(name, lang, lat, lng, accuracy, Arrays.asList(type), extraParams);
    }

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
            String uri = buildUrl(METHOD_DELETE, String.format("key=%s", apiKey), extraParams);
            JSONObject input = new JSONObject().put(STRING_REFERENCE, reference);
            HttpPost post = new HttpPost(uri);
            post.setEntity(new StringEntity(input.toString()));
            JSONObject response = new JSONObject(requestHandler.post(post));
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
     * @param place       to bump
     * @param extraParams to append to request url
     */
    public void bumpPlace(Place place, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_BUMP, String.format("key=%s", apiKey), extraParams);
            HttpPost post = new HttpPost(uri);
            JSONObject input = new JSONObject().put(STRING_REFERENCE, place.getReferenceId());
            post.setEntity(new StringEntity(input.toString()));
            JSONObject response = new JSONObject(requestHandler.post(post));
            checkStatus(response.getString(STRING_STATUS));
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    /**
     * Bumps an event within the application. Bumps are reflected in your place searches for your application only.
     * Bumping an event makes it appear higher in the result set.
     *
     * @param event       to bump
     * @param extraParams to append to request url
     */
    public void bumpEvent(Event event, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_BUMP, String.format("key=%s", apiKey), extraParams);
            HttpPost post = new HttpPost(uri);
            JSONObject input = new JSONObject().put(STRING_REFERENCE, event.getPlace().getReferenceId())
                    .put(STRING_EVENT_ID, event.getId());
            post.setEntity(new StringEntity(input.toString()));
            JSONObject response = new JSONObject(requestHandler.post(post));
            checkStatus(response.getString(STRING_STATUS));
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    protected InputStream download(String uri) {
        try {
            InputStream in = requestHandler.getInputStream(uri);
            if (in == null)
                throw new GooglePlacesException("Could not attain input stream at " + uri);
            debug("Successfully attained InputStream at " + uri);
            return in;
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    protected InputStream downloadPhoto(Photo photo, int maxWidth, int maxHeight, Param... extraParams) {
        try {
            String uri = String.format("%sphoto?photoreference=%s&key=%s", API_URL, photo.getReference(),
                    apiKey);

            List<Param> params = new ArrayList<>(Arrays.asList(extraParams));
            if (maxHeight != -1) params.add(Param.name("maxheight").value(maxHeight));
            if (maxWidth != -1) params.add(Param.name("maxwidth").value(maxWidth));
            extraParams = params.toArray(new Param[params.size()]);
            uri = addExtraParams(uri, extraParams);

            return download(uri);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    /**
     * Returns the event at the specified place with the specified event id.
     *
     * @param place       reference to place the event is at
     * @param eventId     id of event
     * @param extraParams to append to request url
     * @return event
     */
    public Event getEvent(Place place, String eventId, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_EVENT_DETAILS, String.format("key=%s&reference=%s&event_id=%s",
                    apiKey, place.getReferenceId(), eventId), extraParams);
            String response = requestHandler.get(uri);
            return Event.parseDetails(response).setPlace(place);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

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
                          Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_EVENT_ADD, String.format("key=%s", apiKey), extraParams);
            HttpPost post = new HttpPost(uri);
            JSONObject input = Event.buildInput(duration, lang, place.getReferenceId(), summary, url);
            post.setEntity(new StringEntity(input.toString()));
            JSONObject response = new JSONObject(requestHandler.post(post));
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
     * @param place       to add to
     * @param summary     of event
     * @param lang        language of event
     * @param url         url of event
     * @param duration    length of event in seconds
     * @param extraParams to append to request url
     * @return newly created event
     */
    public Event addEvent(Place place, String summary, long duration, String lang, String url, Param... extraParams) {
        return addEvent(place, summary, duration, lang, url, true, extraParams);
    }

    /**
     * Adds a new Event to Google Places API.
     *
     * @param place       to add to
     * @param summary     of event
     * @param duration    length of event in seconds
     * @param extraParams to append to request url
     * @return newly created event
     */
    public Event addEvent(Place place, String summary, long duration, boolean returnEvent, Param... extraParams) {
        return addEvent(place, summary, duration, null, null, returnEvent, extraParams);
    }

    /**
     * Adds a new Event to Google Places API.
     *
     * @param place       to add to
     * @param summary     of event
     * @param duration    length of event in seconds
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
     * @param eventId        unique event id
     * @param extraParams    to append to request url
     */
    public void deleteEvent(String placeReference, String eventId, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_EVENT_DELETE, String.format("key=%s", apiKey), extraParams);
            HttpPost post = new HttpPost(uri);
            JSONObject input = new JSONObject().put(STRING_REFERENCE, placeReference)
                    .put(STRING_EVENT_ID, eventId);
            post.setEntity(new StringEntity(input.toString()));
            JSONObject response = new JSONObject(requestHandler.post(post));
            checkStatus(response.getString(STRING_STATUS));
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    /**
     * Deletes the specified event from Places API.
     *
     * @param event       to delete
     * @param extraParams to append to request url
     */
    public void deleteEvent(Event event, Param... extraParams) {
        deleteEvent(event.getPlace().getReferenceId(), event.getId(), extraParams);
    }

    private List<Prediction> getPredictions(String input, String method, Param... extraParams) {
        try {
            String uri = buildUrl(method, String.format("input=%s&key=%s", input, apiKey),
                    extraParams);
            String response = requestHandler.get(uri);
            return Prediction.parse(this, response);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    /**
     * Returns a list of auto-complete predictions for searching for a specific place.
     *
     * @param input       user input
     * @param extraParams to append to request url
     * @return list of predictions
     */
    public List<Prediction> getPlacePredictions(String input, Param... extraParams) {
        return getPredictions(input, METHOD_AUTOCOMPLETE, extraParams);
    }

    /**
     * Returns a list of auto-complete predictions for searching for a place by a query.
     *
     * @param input       user input
     * @param extraParams to append to request url
     * @return list of predictions
     */
    public List<Prediction> getQueryPredictions(String input, Param... extraParams) {
        return getPredictions(input, METHOD_QUERY_AUTOCOMPLETE, extraParams);
    }

    private List<Place> getPlaces(String uri, String method, int limit) throws IOException {

        limit = Math.min(limit, MAXIMUM_RESULTS); // max of 60 results possible
        int pages = (int) Math.ceil(limit / (double) MAXIMUM_PAGE_RESULTS);

        debug("Downloading and parsing place data from " + uri + "...");
        debug("Limit: " + limit);
        debug("Maximum pages: " + pages);

        List<Place> places = new ArrayList<>();
        // new request for each page
        for (int i = 0; i < pages; i++) {
            debug("Page: " + (i + 1));
            String raw = requestHandler.get(uri);
            String nextPage = parse(this, places, raw, limit);
            if (nextPage != null) {
                limit -= MAXIMUM_PAGE_RESULTS;
                uri = String.format("%s%s/json?pagetoken=%s&key=%s",
                        API_URL, method, nextPage, apiKey);
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
     * Represents an extra, optional parameter that can be specified.
     */
    public static class Param {
        //Please note the change from private to protected for the variable "name"
        protected final String name;
        protected String value;

        private Param(String name) {
            this.name = name;
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
    }

    /**
     * Represents an extra, optional type parameter that restricts the results to places matching at least one of the specified types.
     */
    public static class TypeParam extends Param{

        private TypeParam(String name) {
            super(name);
        }

        /**
         * Returns a new type param with the specified name.
         *
         * @param name to create TypeParam from
         * @return new param
         */
        public static TypeParam name(String name) {
            return new TypeParam(name);
        }

        /**
         * Sets the values of the Param.
         *
         * @param values of params
         * @return this params
         */
        public Param value(List<String> values) {
            StringBuilder valuesSb = new StringBuilder();
            for(int i=0; i< values.size(); i++){
                valuesSb.append(values.get(i));
                if(i != (values.size() -1)){
                    valuesSb.append("%7C"); // it represents a pipeline character |
                }
            }
            this.value = valuesSb.toString();
            return this;
        }


    }
}
