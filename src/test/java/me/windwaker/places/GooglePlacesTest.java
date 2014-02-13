package me.windwaker.places;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

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
		List<Place> places = google.getNearbyPlaces(EMPIRE_STATE_BUILDING_LATITUDE, EMPIRE_STATE_BUILDING_LONGITUDE,
				100, GooglePlaces.MAXIMUM_RESULTS);
		if (!placeNameInList("Empire State Building", places)) fail("Empire State Building not found!");
	}

	@Test
	public void testTextSearch() {
		List<Place> places = google.getPlacesByQuery("Empire+State+Building", GooglePlaces.MAXIMUM_RESULTS);
		if (!placeNameInList("Empire State Building", places)) fail("Empire State Building not found!");
	}

	@Test
	public void testPlaceDetails() {
		Place empireStateBuilding = getPlace();
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
		System.out.println("Photos: " + detailedEmpireStateBuilding.getPhotos().size());
		System.out.println("Hours:\n" + detailedEmpireStateBuilding.getHours());
	}

	@Test
	public void testGetImage() {
		try {
			Place place = getPlace().getDetails();
			List<Photo> photos = place.getPhotos();
			Photo photo = photos.get(new Random().nextInt(photos.size()));
			BufferedImage image = photo.getImage();

			File file = new File("target/test.jpg");
			System.out.println(System.getProperty("user.dir"));
			file.delete();
			file.createNewFile();
			ImageIO.write(image, "jpg", new File("target/test.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testPredictions() {
		System.out.println("Predictions: " + google.getPredictions("Empire"));
	}

	// KEEP THE FOLLOWING COMMENTED TO AVOID FILLING THE QUOTA QUICKLY

	//@Test
	public void testAddAndDeletePlace() {
		Place place = addPlace();
		google.deletePlace(place.getReferenceId());
	}

	//@Test
	public void testAddAndDeleteEvent() {
		Place place = addPlace();
		Event event = google.addEvent(place, "Test Event, Please Ignore", 100000, "en", "http://www.example.com");
		google.deleteEvent(event);
	}

	//@Test
	public void testBump() {
		Place place = addPlace().bump();
		addEvent(place).bump();
	}

	private Place getPlace() {
		List<Place> places = google.getPlacesByQuery("Empire+State+Building", GooglePlaces.MAXIMUM_RESULTS);
		Place empireStateBuilding = null;
		for (Place place : places) {
			if (place.getName().equals("Empire State Building")) {
				empireStateBuilding = place;
				break;
			}
		}
		if (empireStateBuilding == null) fail("Empire State Building not found!");
		return empireStateBuilding;
	}

	private Event addEvent(Place place) {
		return google.addEvent(place, "Test Event, Please Ignore", 100000, "en", "http://www.example.com");
	}

	private Place addPlace() {
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
