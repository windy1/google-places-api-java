package se.walkercrou.places;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class GooglePlacesTest {
    private static final double EMPIRE_STATE_BUILDING_LATITUDE = 40.748444;
    private static final double EMPIRE_STATE_BUILDING_LONGITUDE = -73.985658;
    private static final String API_KEY_FILE_NAME = "places_api.key";
    private static final String TEST_PLACE_NAME = "University of Vermont";
    private static final double TEST_PLACE_LAT = 44.478025, TEST_PLACE_LNG = -73.196475;
    private GooglePlaces google;

    @Before
    public void setUp() {
        try {
            InputStream in = GooglePlacesTest.class.getResourceAsStream("/" + API_KEY_FILE_NAME);
            if (in == null)
                throw new RuntimeException("API key not found.");
            google = new GooglePlaces(IOUtils.toString(in));
            google.setDebugModeEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetNearbyPlaces() {
        System.out.println("******************** getNearbyPlaces ********************");
        if (!findPlace(google.getNearbyPlaces(TEST_PLACE_LAT, TEST_PLACE_LNG, GooglePlaces.MAXIMUM_RADIUS,
                GooglePlaces.MAXIMUM_RESULTS), TEST_PLACE_NAME))
            fail("Test place could not be found at coordinates.");

        if(!hasAtLeastAPlace(google.getNearbyPlaces(TEST_PLACE_LAT, TEST_PLACE_LNG, GooglePlaces.MAXIMUM_RADIUS,GooglePlaces.TypeParam.name(GooglePlaces.STRING_TYPES).value(Arrays.asList(GooglePlaces.TYPE_BAR,GooglePlaces.TYPE_RESTAURANT)))))
            fail("Test place could not be found at coordinates.");

        // contain within one method to prevent threading problems
        testGetPlacesByQuery();
    }


    public void testGetPlacesByQuery() {
        System.out.println("******************** getPlacesByQuery ********************");
        if (!findPlace(google.getPlacesByQuery(TEST_PLACE_NAME, GooglePlaces.MAXIMUM_RESULTS), TEST_PLACE_NAME))
            fail("Test place could not be found by name");
        testGetPlacesByRadar();
    }

    public void testGetPlacesByRadar() {
        System.out.println("******************** getPlacesByRadar ********************");
        List<Place> places = google.getPlacesByRadar(TEST_PLACE_LAT, TEST_PLACE_LNG, GooglePlaces.MAXIMUM_RADIUS,
                GooglePlaces.MAXIMUM_RESULTS, GooglePlaces.Param.name("name").value(TEST_PLACE_NAME));
        boolean found = false;
        for (Place place : places) {
            if (place.getDetails().getName().equals(TEST_PLACE_NAME))
                found = true;
        }
        if (!found)
            fail("Test place could not be found using the radar method.");
    }

    private boolean findPlace(List<Place> places, String name) {
        boolean found = false;
        for (Place place : places) {
            if (place.getName().equals(name))
                found = true;
        }
        return found;
    }

    private boolean hasAtLeastAPlace(List<Place> places){
        return (places != null) && places.size() > 0;
    }
}
