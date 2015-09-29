package se.walkercrou.places;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import se.walkercrou.places.exception.GooglePlacesException;

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

    protected static void checkStatus(String statusCode, String errorMessage) {
        GooglePlacesException e = GooglePlacesException.parse(statusCode, errorMessage);
        if (e != null)
            throw e;
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
        checkStatus(statusCode, json.optString(STRING_ERROR_MESSAGE));
        if (statusCode.equals(STATUS_ZERO_RESULTS))
            return null;

        JSONArray results = json.getJSONArray(ARRAY_RESULTS);
        parseResults(client, places, results, limit);

        return json.optString(STRING_NEXT_PAGE_TOKEN, null);
    }

    private static void parseResults(GooglePlaces client, List<Place> places, JSONArray results, int limit) {
        limit = Math.min(limit, MAXIMUM_PAGE_RESULTS);
        for (int i = 0; i < limit; i++) {

            // reached the end of the page
            if (i >= results.length())
                return;

            JSONObject result = results.getJSONObject(i);
	        JSONObject wrapper = new JSONObject();
	        wrapper.put("result", result);
	        places.add(Place.parseDetails(client, wrapper.toString()));
	        //            // location
	        //            JSONObject location = result.getJSONObject(OBJECT_GEOMETRY).getJSONObject(OBJECT_LOCATION);
	        //            double lat = location.getDouble(DOUBLE_LATITUDE);
	        //            double lon = location.getDouble(DOUBLE_LONGITUDE);
	        //
	        //            String placeId = result.getString(STRING_PLACE_ID);
	        //            String iconUrl = result.optString(STRING_ICON, null);
	        //            String name = result.optString(STRING_NAME);
	        //            String addr = result.optString(STRING_ADDRESS, null);
	        //            double rating = result.optDouble(DOUBLE_RATING, -1);
	        //            String vicinity = result.optString(STRING_VICINITY, null);
	        //
	        //            // see if the place is open, fail-safe if opening_hours is not present
	        //            JSONObject hours = result.optJSONObject(OBJECT_HOURS);
	        //            boolean hoursDefined = hours != null && hours.has(BOOLEAN_OPENED);
	        //            Status status = Status.NONE;
	        //            if (hoursDefined) {
	        //                boolean opened = hours.getBoolean(BOOLEAN_OPENED);
	        //                status = opened ? Status.OPENED : Status.CLOSED;
	        //            }
	        //
	        //            // get the price level for the place, fail-safe if not defined
	        //            boolean priceDefined = result.has(INTEGER_PRICE_LEVEL);
	        //            Price price = Price.NONE;
	        //            if (priceDefined) {
	        //                price = Price.values()[result.getInt(INTEGER_PRICE_LEVEL)];
	        //            }
	        //
	        //            // the place "types"
	        //            List<String> types = new ArrayList<>();
	        //            JSONArray jsonTypes = result.optJSONArray(ARRAY_TYPES);
	        //            if (jsonTypes != null) {
	        //                for (int a = 0; a < jsonTypes.length(); a++) {
	        //                    types.add(jsonTypes.getString(a));
	        //                }
	        //            }
	        //
	        //            Place place = new Place();
	        //
	        //            // build a place object
	        //            places.add(place.setClient(client).setPlaceId(placeId).setLatitude(lat).setLongitude(lon).setIconUrl(iconUrl).setName(name)
	        //                    .setAddress(addr).setRating(rating).setStatus(status).setPrice(price)
	        //                    .addTypes(types).setVicinity(vicinity).setJson(result));
        }
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
    public PlaceResponse getNearbyPlaces(double lat, double lng, double radius, int limit, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_NEARBY_SEARCH, String.format(Locale.ENGLISH, "key=%s&location=%f,%f&radius=%f",
                    apiKey, lat, lng, radius), extraParams);
	        return getPlaces(uri, limit);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public PlaceResponse getNearbyPlaces(double lat, double lng, double radius, Param... extraParams) {
        return getNearbyPlaces(lat, lng, radius, DEFAULT_RESULTS, extraParams);
    }

	@Override
	public PlaceResponse getNearbyPlaces(String nextPageToken) {
		String uri = buildUrl(METHOD_NEARBY_SEARCH, String.format("key=%s&pagetoken=%s", apiKey, nextPageToken));
		try {
			return getPlacesNextPage(uri);
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

	@Override
	public PlaceResponse getNearbyPlacesRankedByDistance(double lat, double lng, int limit, Param... params) {
		try {
			String uri = buildUrl(METHOD_NEARBY_SEARCH, String.format("key=%s&location=%f,%f&rankby=distance",
				apiKey, lat, lng), params);
			return getPlaces(uri, limit);
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

    @Override
    public PlaceResponse getNearbyPlacesRankedByDistance(double lat, double lng, Param... params) {
        return getNearbyPlacesRankedByDistance(lat, lng, DEFAULT_RESULTS, params);
    }

	@Override
	public PlaceResponse getNearbyPlacesRankedByDistance(String nextPageToken) {
		String uri = buildUrl(METHOD_NEARBY_SEARCH, String.format("key=%s&pagetoken=%s", apiKey, nextPageToken));
		try {
			return getPlacesNextPage(uri);
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

    @Override
    public PlaceResponse getPlacesByQuery(String query, int limit, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_TEXT_SEARCH, String.format("query=%s&key=%s", query, apiKey),
                    extraParams);
	        return getPlaces(uri, limit);
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public PlaceResponse getPlacesByQuery(String query, Param... extraParams) {
        return getPlacesByQuery(query, DEFAULT_RESULTS, extraParams);
    }

	@Override
	public PlaceResponse getPlacesByQuery(String nextPageToken) {
		String uri = buildUrl(METHOD_TEXT_SEARCH, String.format("key=%s&pagetoken=%s", apiKey, nextPageToken));
		try {
			return getPlacesNextPage(uri);
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

	@Override
	public PlaceResponse getPlacesByRadar(double lat, double lng, double radius, int limit, Param... extraParams) {
		try {
			String uri = buildUrl(METHOD_RADAR_SEARCH, String.format(Locale.ENGLISH, "key=%s&location=%f,%f&radius=%f",
				apiKey, lat, lng, radius), extraParams);
			return getPlaces(uri, limit);
		} catch (Exception e) {
			throw new GooglePlacesException(e);
		}
	}

    @Override
    public PlaceResponse getPlacesByRadar(double lat, double lng, double radius, Param... extraParams) {
        return getPlacesByRadar(lat, lng, radius, MAXIMUM_RESULTS, extraParams);
    }

    @Override
    public Place getPlaceById(String placeId, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_DETAILS, String.format("key=%s&placeid=%s", apiKey, placeId), extraParams);
            return Place.parseDetails(this, requestHandler.get(uri));
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public Place addPlace(PlaceBuilder builder, boolean returnPlace, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_ADD, String.format("key=%s", apiKey));
            JSONObject input = builder.toJson();
            HttpPost post = new HttpPost(uri);
            post.setEntity(new StringEntity(input.toString()));
            JSONObject response = new JSONObject(requestHandler.post(post));
            String status = response.getString(STRING_STATUS);
            checkStatus(status, response.optString(STRING_ERROR_MESSAGE));
            return returnPlace ? getPlaceById(response.getString(STRING_PLACE_ID)) : null;
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public void deletePlaceById(String placeId, Param... extraParams) {
        try {
            String uri = buildUrl(METHOD_DELETE, String.format("key=%s", apiKey), extraParams);
            JSONObject input = new JSONObject().put(STRING_PLACE_ID, placeId);
            HttpPost post = new HttpPost(uri);
            post.setEntity(new StringEntity(input.toString()));
            JSONObject response = new JSONObject(requestHandler.post(post));
            String status = response.getString(STRING_STATUS);
            checkStatus(status, response.optString(STRING_ERROR_MESSAGE));
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

    @Override
    public void deletePlace(Place place, Param... extraParams) {
        deletePlaceById(place.getPlaceId(), extraParams);
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
	        return download(getPhotoUrlByReference(photo.getReference(), maxWidth, maxHeight, extraParams));
        } catch (Exception e) {
            throw new GooglePlacesException(e);
        }
    }

	public String getPhotoUrlByReference(String reference, int maxWidth, int maxHeight, Param... extraParams) {
		String uri = String.format("%sphoto?photoreference=%s&key=%s", API_URL, reference, apiKey);

		List<Param> params = new ArrayList<>(Arrays.asList(extraParams));
		if (maxHeight != -1) {
			params.add(Param.name("maxheight").value(maxHeight));
		}
		if (maxWidth != -1) {
			params.add(Param.name("maxwidth").value(maxWidth));
		}
		extraParams = params.toArray(new Param[params.size()]);
		return addExtraParams(uri, extraParams);
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
    public List<Prediction> getPlacePredictions(String input, int offset, int lat, int lng, int radius,
                                                Param... extraParams) {
        List<Param> params = new ArrayList<>();
        if (offset != -1)
            params.add(Param.name("offset").value(offset));
        if (lat != -1 && lng != -1)
            params.add(Param.name("location").value(lat + "," + lng));
        params.addAll(new ArrayList<>(Arrays.asList(extraParams)));

        return getPredictions(input, METHOD_AUTOCOMPLETE, params.toArray(new Param[params.size()]));
    }

    @Override
    public List<Prediction> getPlacePredictions(String input, int offset, Param... extraParams) {
        return getPlacePredictions(input, offset, -1, -1, -1, extraParams);
    }

    @Override
    public List<Prediction> getPlacePredictions(String input, Param... extraParams) {
        return getPlacePredictions(input, -1, extraParams);
    }

    @Override
    public List<Prediction> getQueryPredictions(String input, int offset, int lat, int lng, int radius,
                                                Param... extraParams) {
        List<Param> params = new ArrayList<>();
        if (offset != -1)
            params.add(Param.name("offset").value(offset));
        if (lat == -1 && lng == -1)
            params.add(Param.name("location").value(lat + "," + lng));
        params.addAll(new ArrayList<>(Arrays.asList(extraParams)));

        return getPredictions(input, METHOD_QUERY_AUTOCOMPLETE, params.toArray(new Param[params.size()]));
    }

    @Override
    public List<Prediction> getQueryPredictions(String input, int offset, Param... extraParams) {
        return getQueryPredictions(input, offset, -1, -1, -1, extraParams);
    }

    @Override
    public List<Prediction> getQueryPredictions(String input, Param... extraParams) {
        return getQueryPredictions(input, -1, extraParams);
    }

	private PlaceResponse getPlaces(String uri, int limit) throws IOException {
		limit = Math.min(limit, MAXIMUM_RESULTS); // max of 20 results possible

        List<Place> places = new ArrayList<>();
		String raw = requestHandler.get(uri);
		debug(raw);
		String nextPage = parse(this, places, raw, limit);

		return new PlaceResponse(nextPage, places);
	}

	private PlaceResponse getPlacesNextPage(String uri) throws IOException {
		List<Place> places = new ArrayList<>();
		String raw = requestHandler.get(uri);
		debug(raw);
		String nextPage = parse(this, places, raw, MAXIMUM_RESULTS);
		return new PlaceResponse(nextPage, places);
	}
}
