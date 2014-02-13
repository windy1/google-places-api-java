package me.windwaker.places;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static me.windwaker.places.GooglePlaces.*;

public class Prediction {
	private GooglePlaces client;
	private String placeId, placeReference;
	private String description;
	private int substrOffset, substrLength;
	private final List<DescriptionTerm> terms = new ArrayList<DescriptionTerm>();
	private final List<String> types = new ArrayList<String>();

	protected Prediction() {
	}

	protected Prediction setClient(GooglePlaces client) {
		this.client = client;
		return this;
	}

	public GooglePlaces getClient() {
		return client;
	}

	public Place getPlace(Param... extraParams) {
		return client.getPlace(placeReference, extraParams);
	}

	/**
	 * Adds a collection of string "types".
	 *
	 * @param types to add
	 * @return this
	 */
	protected Prediction addTypes(Collection<String> types) {
		this.types.addAll(types);
		return this;
	}

	/**
	 * Adds a new type to this place.
	 *
	 * @param type to add
	 * @return this
	 */
	protected Prediction addType(String type) {
		types.add(type);
		return this;
	}

	/**
	 * Returns all of this place's types in an unmodifiable list.
	 *
	 * @return types
	 */
	public List<String> getTypes() {
		return Collections.unmodifiableList(types);
	}

	/**
	 * Removes a type from this place.
	 *
	 * @param type to remove
	 * @return this
	 */
	protected Prediction removeType(String type) {
		types.remove(type);
		return this;
	}

	/**
	 * Clears all types from this place.
	 *
	 * @return this
	 */
	protected Prediction clearTypes() {
		types.clear();
		return this;
	}

	protected Prediction addTerms(Collection<DescriptionTerm> terms) {
		this.terms.addAll(terms);
		return this;
	}

	protected Prediction addTerm(DescriptionTerm term) {
		terms.add(term);
		return this;
	}

	protected Prediction removeTerm(DescriptionTerm term) {
		terms.remove(term);
		return this;
	}

	protected Prediction clearTerms() {
		terms.clear();
		return this;
	}

	public List<DescriptionTerm> getTerms() {
		return Collections.unmodifiableList(terms);
	}

	public String getPlaceId() {
		return placeId;
	}

	protected Prediction setPlaceId(String placeId) {
		this.placeId = placeId;
		return this;
	}

	public String getPlaceReference() {
		return placeReference;
	}

	protected Prediction setPlaceReference(String placeReference) {
		this.placeReference = placeReference;
		return this;
	}

	public String getDescription() {
		return description;
	}

	protected Prediction setDescription(String description) {
		this.description = description;
		return this;
	}

	public int getSubstringOffset() {
		return substrOffset;
	}

	protected Prediction setSubstringOffset(int substrOffset) {
		this.substrOffset = substrOffset;
		return this;
	}

	public int getSubstringLength() {
		return substrLength;
	}

	protected Prediction setSubstringLength(int substrLength) {
		this.substrLength = substrLength;
		return this;
	}

	@Override
	public String toString() {
		return "[" + description + "]";
	}

	public static List<Prediction> parse(GooglePlaces client, String rawJson) {
		JSONObject json = new JSONObject(rawJson);
		checkStatus(json.getString(STRING_STATUS));

		List<Prediction> predictions = new ArrayList<Prediction>();
		JSONArray jsonPredictions = json.getJSONArray(ARRAY_PREDICTIONS);
		for (int i = 0; i < jsonPredictions.length(); i++) {
			JSONObject jsonPrediction = jsonPredictions.getJSONObject(i);
			String description = jsonPrediction.getString(STRING_DESCRIPTION);
			String id = jsonPrediction.getString(STRING_ID);
			String reference = jsonPrediction.getString(STRING_REFERENCE);

			JSONArray jsonTerms = jsonPrediction.getJSONArray(ARRAY_TERMS);
			List<DescriptionTerm> terms = new ArrayList<DescriptionTerm>();
			for (int a = 0; a < jsonTerms.length(); a++) {
				JSONObject jsonTerm = jsonTerms.getJSONObject(a);
				String value = jsonTerm.getString(STRING_VALUE);
				int offset = jsonTerm.getInt(INTEGER_OFFSET);
				terms.add(new DescriptionTerm(value, offset));
			}

			JSONArray jsonTypes = jsonPrediction.getJSONArray(ARRAY_TYPES);
			List<String> types = new ArrayList<String>();
			for (int b = 0; b < jsonTypes.length(); b++) {
				types.add(jsonTypes.getString(b));
			}

			JSONArray substrArray = jsonPrediction.getJSONArray(ARRAY_MATCHED_SUBSTRINGS);
			JSONObject substr = substrArray.getJSONObject(0);
			int substrOffset = substr.getInt(INTEGER_OFFSET);
			int substrLength = substr.getInt(INTEGER_LENGTH);

			predictions.add(new Prediction().setDescription(description).setPlaceId(id)
					.setPlaceReference(reference).addTerms(terms).addTypes(types).setSubstringLength(substrLength)
					.setSubstringOffset(substrOffset).setClient(client));
		}

		return predictions;
	}

	public static class DescriptionTerm {
		private final String value;
		private final int offset;

		protected DescriptionTerm(String value, int offset) {
			this.value = value;
			this.offset = offset;
		}

		public String getValue() {
			return value;
		}

		public int getOffset() {
			return offset;
		}
	}
}
