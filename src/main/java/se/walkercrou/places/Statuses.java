package se.walkercrou.places;

/**
 * Google Places API official "status" replies.
 */
public interface Statuses {
    /**
     * Indicates the request was successful.
     */
    String STATUS_OK = "OK";
    /**
     * Indicates that nothing went wrong during the request, but no places were found
     */
    String STATUS_ZERO_RESULTS = "ZERO_RESULTS";
    /**
     * Indicates that you are over the quota for queries to Google Places API
     */
    String STATUS_OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
    /**
     * Indicates that the request was denied
     */
    String STATUS_REQUEST_DENIED = "REQUEST_DENIED";
    /**
     * Indicates the request was invalid, generally indicating a missing parameter
     */
    String STATUS_INVALID_REQUEST = "INVALID_REQUEST";
    /**
     * Indicates an internal server-side error
     */
    String STATUS_UNKNOWN_ERROR = "UNKNOWN_ERROR";
    /**
     * Indicates that a resource was could not be resolved
     */
    String STATUS_NOT_FOUND = "NOT_FOUND";
}
