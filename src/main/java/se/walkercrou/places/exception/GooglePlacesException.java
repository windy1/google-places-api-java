package se.walkercrou.places.exception;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import static se.walkercrou.places.Statuses.*;

/**
 * Represents an exception or error thrown by Google Places API
 */
public class GooglePlacesException extends RuntimeException {
    private static final Map<String, Class<?>> statusClassMap = new HashMap<>();

    static {
        statusClassMap.put(STATUS_OK, null);
        statusClassMap.put(STATUS_ZERO_RESULTS, NoResultsFoundException.class);
        statusClassMap.put(STATUS_OVER_QUERY_LIMIT, OverQueryLimitException.class);
        statusClassMap.put(STATUS_REQUEST_DENIED, RequestDeniedException.class);
        statusClassMap.put(STATUS_INVALID_REQUEST, InvalidRequestException.class);
    }

    private String statusCode;
    private String errorMessage;

    /**
     * Constructs a new exception from a server-given status code and error message.
     *
     * @param statusCode to use
     * @param errorMessage to use
     */
    public GooglePlacesException(String statusCode, String errorMessage) {
        super(statusCode + (errorMessage == null ? "" : ": \"" + errorMessage + "\""));
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Constructs a new exception from a server-given status code.
     *
     * @param statusCode to use
     */
    public GooglePlacesException(String statusCode) {
        this(statusCode, null);
    }

    /**
     * Constructs a new exception from a given {@link java.lang.Throwable}.
     *
     * @param t throwable cause
     */
    public GooglePlacesException(Throwable t) {
        super(t);
    }

    /**
     * Returns this exception's status code.
     *
     * @return status code
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * Returns this exception's error message.
     *
     * @return error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Returns the correct exception from a server-given status code and error message.
     *
     * @param statusCode to find exception for
     * @param errorMessage error message from server
     * @return exception that matches the specified status code
     */
    public static GooglePlacesException parse(String statusCode, String errorMessage) {
        Class<?> clazz = statusClassMap.get(statusCode);
        if (clazz == null)
            return null;
        try {
            if (errorMessage == null || errorMessage.isEmpty())
                return (GooglePlacesException) clazz.newInstance();
            else {
                Constructor<?> constructor = clazz.getConstructor(String.class);
                return (GooglePlacesException) constructor.newInstance(errorMessage);
            }
        } catch (Exception e) {
            // Should never happen!
            throw new GooglePlacesException(e);
        }
    }
}
