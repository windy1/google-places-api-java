package se.walkercrou.places;

import java.util.ArrayList;
import java.util.List;

public class PlaceResponse {
	String nextPage;
	List<Place> places = new ArrayList<>();

	public PlaceResponse(String nextPage, List<Place> places) {
		this.nextPage = nextPage;
		this.places = places;
	}

	public String getNextPage() {
		return nextPage;
	}

	public List<Place> getPlaces() {
		return places;
	}
}
