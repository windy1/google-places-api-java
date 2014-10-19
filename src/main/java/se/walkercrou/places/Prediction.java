package se.walkercrou.places;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static se.walkercrou.places.GooglePlaces.*;

/**
 * Represents a autocomplete prediction based on a query.
 */
public class Prediction {
    private final List<DescriptionTerm> terms = new ArrayList<>();
    private final List<String> types = new ArrayList<>();
    private GooglePlaces client;
    private String placeId, placeReference;
    private String description;
    private int substrOffset, substrLength;

    protected Prediction() {
    }

    /**
     * Returns a list of predictions from JSON.
     *
     * @param client  of request
     * @param rawJson to parse
     * @return list of predictions
     */
    public static List<Prediction> parse(GooglePlaces client, String rawJson) {
        JSONObject json = new JSONObject(rawJson);
        checkStatus(json.getString(STRING_STATUS));

        List<Prediction> predictions = new ArrayList<>();
        JSONArray jsonPredictions = json.getJSONArray(ARRAY_PREDICTIONS);
        for (int i = 0; i < jsonPredictions.length(); i++) {
            JSONObject jsonPrediction = jsonPredictions.getJSONObject(i);
            String description = jsonPrediction.getString(STRING_DESCRIPTION);
            String id = jsonPrediction.optString(STRING_ID, null);
            String reference = jsonPrediction.optString(STRING_REFERENCE, null);

            JSONArray jsonTerms = jsonPrediction.getJSONArray(ARRAY_TERMS);
            List<DescriptionTerm> terms = new ArrayList<>();
            for (int a = 0; a < jsonTerms.length(); a++) {
                JSONObject jsonTerm = jsonTerms.getJSONObject(a);
                String value = jsonTerm.getString(STRING_VALUE);
                int offset = jsonTerm.getInt(INTEGER_OFFSET);
                terms.add(new DescriptionTerm(value, offset));
            }

            JSONArray jsonTypes = jsonPrediction.optJSONArray(ARRAY_TYPES);
            List<String> types = new ArrayList<>();
            if (jsonTypes != null) {
                for (int b = 0; b < jsonTypes.length(); b++) {
                    types.add(jsonTypes.getString(b));
                }
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

    /**
     * Returns the client for this prediction.
     *
     * @return client
     */
    public GooglePlaces getClient() {
        return client;
    }

    protected Prediction setClient(GooglePlaces client) {
        this.client = client;
        return this;
    }

    /**
     * Returns the place this prediction is suggesting.
     *
     * @param extraParams to append to request URL
     * @return place
     */
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

    /**
     * Returns the components of the description.
     *
     * @return description components
     */
    public List<DescriptionTerm> getTerms() {
        return Collections.unmodifiableList(terms);
    }

    /**
     * Returns the id of the place this prediction is suggesting.
     *
     * @return place id
     */
    public String getPlaceId() {
        return placeId;
    }

    protected Prediction setPlaceId(String placeId) {
        this.placeId = placeId;
        return this;
    }

    /**
     * Returns the reference to the place being suggested.
     *
     * @return place reference
     */
    public String getPlaceReference() {
        return placeReference;
    }

    protected Prediction setPlaceReference(String placeReference) {
        this.placeReference = placeReference;
        return this;
    }

    /**
     * Returns a description of the prediction in human-readable form.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    protected Prediction setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Returns the offset from the query where the matched string starts.
     *
     * @return offset
     */
    public int getSubstringOffset() {
        return substrOffset;
    }

    protected Prediction setSubstringOffset(int substrOffset) {
        this.substrOffset = substrOffset;
        return this;
    }

    /**
     * Returns the length from the substring offset of the matched string.
     *
     * @return length of substring
     */
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

    /**
     * Represents an element within a description.
     */
    public static class DescriptionTerm {
        private final String value;
        private final int offset;

        protected DescriptionTerm(String value, int offset) {
            this.value = value;
            this.offset = offset;
        }

        /**
         * Returns the value of the term.
         *
         * @return value
         */
        public String getValue() {
            return value;
        }

        /**
         * Returns the terms position in the description.
         *
         * @return description position
         */
        public int getOffset() {
            return offset;
        }
    }
}
