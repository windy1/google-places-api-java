package se.walkercrou.places.exception;

public class GooglePlacesException extends RuntimeException {
    private final String statusCode;

    public GooglePlacesException(Throwable cause) {
        super(cause);
        statusCode = null;
    }

    public GooglePlacesException(String statusCode, String msg) {
        super(msg);
        this.statusCode = statusCode;
    }

    public GooglePlacesException(String msg) {
        this(null, msg);
    }

    public String getStatusCode() {
        return statusCode;
    }
}
