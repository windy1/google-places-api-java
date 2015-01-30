package se.walkercrou.places;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import se.walkercrou.places.exception.GooglePlacesException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class GooglePlaces implements GooglePlacesInterface {

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
        String url = String.format(Locale.ENGLISH, "%s%s/json?%s", API_URL, method, params);
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

    @Override
    @Deprecated
    public boolean isSensorEnabled() {
        return false;
    }

    @Override
    @Deprecated
    public void setSensorEnabled(boolean sensor) {
    }

    @Override
    public boolean isDebugModeEnabled() {
        return debugModeEnabled;
    }

    @Override
    public void setDebugModeEnabled(boolean debugModeEnabled) {
        this.debugModeEnabled = debugModeEnabled;
    }

    private void debug(String msg) {
        if (debugModeEnabled)
            System.out.println(msg);
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

    @Override
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public List<Place> getNearbyPlaces(double lat, double lng, double radius, int limit, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_NEARBY_SEARCH, String.format("key=%s&location=%f,%f&radius=%f",
                    apiKey, lat, lng, radius), extraParams);
            return getPlaces(uri, METHOD_NEARBY_SEARCH, limit);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public List<Place> getNearbyPlaces(double lat, double lng, double radius, Param... extraParams) {
        return getNearbyPlaces(lat, lng, radius, DEFAULT_RESULTS, extraParams);
    }

    @Override
    public List<Place> getPlacesByQuery(String query, int limit, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_TEXT_SEARCH, String.format("query=%s&key=%s", query, apiKey),
                    extraParams);
            return getPlaces(uri, METHOD_TEXT_SEARCH, limit);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public List<Place> getPlacesByQuery(String query, Param... extraParams) {
        return getPlacesByQuery(query, DEFAULT_RESULTS, extraParams);
    }

    @Override
    public List<Place> getPlacesByRadar(double lat, double lng, double radius, int limit, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_RADAR_SEARCH, String.format("key=%s&location=%f,%f&radius=%f",
                    apiKey, lat, lng, radius), extraParams);
            return getPlaces(uri, METHOD_RADAR_SEARCH, limit);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public List<Place> getPlacesByRadar(double lat, double lng, double radius, Param... extraParams) {
        return getPlacesByRadar(lat, lng, radius, MAXIMUM_RESULTS, extraParams);
    }

    @Override
    public Place getPlace(String reference, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_DETAILS, String.format("key=%s&reference=%s", apiKey, reference),
                    extraParams);
            return Place.parseDetails(this, requestHandler.get(uri));
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
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

    @Override
    public Place addPlace(String name, String lang, double lat, double lng, int accuracy, Collection<String> types,
                          Param... extraParams) {
        return addPlace(name, lang, lat, lng, accuracy, types, true, extraParams);
    }

    @Override
    public Place addPlace(String name, String lang, double lat, double lng, int accuracy, String type,
                          Param... extraParams) {
        return addPlace(name, lang, lat, lng, accuracy, Arrays.asList(type), extraParams);
    }

    @Override
    public Place addPlace(String name, String lang, double lat, double lng, int accuracy, String type,
                          boolean returnPlace, Param... extraParams) {
        return addPlace(name, lang, lat, lng, accuracy, Arrays.asList(type), returnPlace, extraParams);
    }

    @Override
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

    @Override
    public void deletePlace(Place place, Param... extraParams) {
        deletePlace(place.getReferenceId(), extraParams);
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public Event addEvent(Place place, String summary, long duration, String lang, String url, Param... extraParams) {
        return addEvent(place, summary, duration, lang, url, true, extraParams);
    }

    @Override
    public Event addEvent(Place place, String summary, long duration, boolean returnEvent, Param... extraParams) {
        return addEvent(place, summary, duration, null, null, returnEvent, extraParams);
    }

    @Override
    public Event addEvent(Place place, String summary, long duration, Param... extraParams) {
        return addEvent(place, summary, duration, true, extraParams);
    }

    @Override
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

    @Override
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

    @Override
    public List<Prediction> getPlacePredictions(String input, Param... extraParams) {
        return getPredictions(input, METHOD_AUTOCOMPLETE, extraParams);
    }

    @Override
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

}
