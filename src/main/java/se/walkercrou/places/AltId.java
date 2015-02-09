package se.walkercrou.places;

/**
 * Represents an alternate entry for a specified {@link Place}
 */
public class AltId {
    private final GooglePlaces client;
    private final String placeId;
    private final Scope scope;

    protected AltId(GooglePlaces client, String placeId, Scope scope) {
        this.client = client;
        this.placeId = placeId;
        this.scope = scope;
    }

    /**
     * Returns the placeId of the alternate entry.
     *
     * @return id of alternate entry
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * Returns the scope of the alternate entry.
     *
     * @return scope of alternate entry
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * Returns the actual alternate place.
     *
     * @return alternate place object
     */
    public Place getPlace() {
        return client.getPlaceById(placeId);
    }
}
