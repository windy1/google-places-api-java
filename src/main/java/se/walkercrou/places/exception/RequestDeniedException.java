package se.walkercrou.places.exception;

import se.walkercrou.places.Statuses;

public class RequestDeniedException extends GooglePlacesException {
    public RequestDeniedException(String errorMessage) {
        super(Statuses.STATUS_REQUEST_DENIED, errorMessage);
    }

    public RequestDeniedException() {
        this(null);
    }
}