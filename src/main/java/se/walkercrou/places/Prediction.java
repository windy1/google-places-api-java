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
    private String placeId;
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
        checkStatus(json.getString(STRING_STATUS), json.optString(STRING_ERROR_MESSAGE));

        List<Prediction> predictions = new ArrayList<>();
        JSONArray jsonPredictions = json.getJSONArray(ARRAY_PREDICTIONS);
        for (int i = 0; i < jsonPredictions.length(); i++) {
            JSONObject jsonPrediction = jsonPredictions.getJSONObject(i);
            String placeId = jsonPrediction.getString(STRING_PLACE_ID);
            String description = jsonPrediction.getString(STRING_DESCRIPTION);

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

            predictions.add(new Prediction().setPlaceId(placeId).setDescription(description).addTerms(terms).addTypes(types)
                    .setSubstringLength(substrLength).setSubstringOffset(substrOffset).setClient(client));
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
        return client.getPlaceById(placeId, extraParams);
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
     * Returns all of this place's types in an unmodifiable list.
     *
     * @return types
     */
    public List<String> getTypes() {
        return Collections.unmodifiableList(types);
    }

    protected Prediction addTerms(Collection<DescriptionTerm> terms) {
        this.terms.addAll(terms);
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
