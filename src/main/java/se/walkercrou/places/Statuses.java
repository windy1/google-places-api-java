package se.walkercrou.places;

public interface Statuses {
    /**
     * Indicates the request was successful.
     */
    static final String STATUS_OK = "OK";
    /**
     * Indicates that nothing went wrong during the request, but no places were found
     */
    static final String STATUS_ZERO_RESULTS = "ZERO_RESULTS";
    /**
     * Indicates that you are over the quota for queries to Google Places API
     */
    static final String STATUS_OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
    /**
     * Indicates that the request was denied
     */
    static final String STATUS_REQUEST_DENIED = "REQUEST_DENIED";
    /**
     * Indicates the request was invalid, generally indicating a missing parameter
     */
    static final String STATUS_INVALID_REQUEST = "INVALID_REQUEST";
    /**
     * Indicates an internal server-side error
     */
    static final String STATUS_UNKNOWN_ERROR = "UNKNOWN_ERROR";
    /**
     * Indicates that a resource was could not be resolved
     */
    static final String STATUS_NOT_FOUND = "NOT_FOUND";
}
