package me.windwaker.places;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class GooglePlacesTest {
	private static final double EMPIRE_STATE_BUILDING_LATITUDE = 40.748444;
	private static final double EMPIRE_STATE_BUILDING_LONGITUDE = -73.985658;
	private GooglePlaces google;

	@Before
	public void setUp() {
		try {
			google = new GooglePlaces(IOUtils.toString(GooglePlacesTest.class.getResourceAsStream("/api_key.txt")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testNearbyPlaces() {
		try {
			List<Place> places = google.getNearbyPlaces(EMPIRE_STATE_BUILDING_LATITUDE, EMPIRE_STATE_BUILDING_LONGITUDE,
					100, GooglePlaces.MAXIMUM_RESULTS);
			if (!placeNameInList("Empire State Building", places)) fail("Empire State Building not found!");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testTextSearch() {
		try {
			List<Place> places = google.getPlacesByQuery("Empire+State+Building", GooglePlaces.MAXIMUM_RESULTS);
			if (!placeNameInList("Empire State Building", places)) fail("Empire State Building not found!");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testPlaceDetails() {
		try {
			List<Place> places = google.getPlacesByQuery("Empire+State+Building", GooglePlaces.MAXIMUM_RESULTS);
			Place empireStateBuilding = null;
			for (Place place : places) {
				if (place.getName().equals("Empire State Building")) {
					empireStateBuilding = place;
					break;
				}
			}

			if (empireStateBuilding == null) fail("Empire State Building not found!");

			Place detailedEmpireStateBuilding = empireStateBuilding.getDetails();
			System.out.println("ID: " + detailedEmpireStateBuilding.getId());
			System.out.println("Name: " + detailedEmpireStateBuilding.getName());
			System.out.println("Phone: " + detailedEmpireStateBuilding.getPhoneNumber());
			System.out.println("International Phone: " + empireStateBuilding.getInternationalPhoneNumber());
			System.out.println("Website: " + detailedEmpireStateBuilding.getWebsite());
			System.out.println("Always Opened: " + detailedEmpireStateBuilding.isAlwaysOpened());
			System.out.println("Status: " + detailedEmpireStateBuilding.getStatus());
			System.out.println("Google Place URL: " + detailedEmpireStateBuilding.getGoogleUrl());
			System.out.println("Price: " + detailedEmpireStateBuilding.getPrice());
			System.out.println("Address: " + detailedEmpireStateBuilding.getAddress());
			System.out.println("Vicinity: " + detailedEmpireStateBuilding.getVicinity());
			System.out.println("Reviews: " + detailedEmpireStateBuilding.getReviews().size());
			System.out.println("Hours:\n" + detailedEmpireStateBuilding.getHours());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	//@Test
	public void testAddAndDeletePlace() {
		try {
			Place place = addPlace();
			google.deletePlace(place.getReferenceId());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	//@Test
	public void testAddAndDeleteEvent() {
		try {
			Place place = addPlace();
			Event event = google.addEvent(place, "Test Event, Please Ignore", 100000, "en", "http://www.example.com");
			google.deleteEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testBump() {
		try {
			Place place = addPlace().bump();
			addEvent(place).bump();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private Event addEvent(Place place) throws IOException {
		return google.addEvent(place, "Test Event, Please Ignore", 100000, "en", "http://www.example.com");
	}

	private Place addPlace() throws IOException {
		return google.addPlace("Test Location, Please Ignore", "en", 23.855917, 11.452065, 50, "spa");
	}

	private boolean placeNameInList(String name, List<Place> places) {
		for (Place place : places) {
			if (place.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
}
