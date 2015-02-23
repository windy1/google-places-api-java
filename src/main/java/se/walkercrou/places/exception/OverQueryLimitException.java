package se.walkercrou.places.exception;

import se.walkercrou.places.Statuses;

public class OverQueryLimitException extends GooglePlacesException {
    public OverQueryLimitException(String errorMessage) {
        super(Statuses.STATUS_OVER_QUERY_LIMIT, errorMessage);
    }

    public OverQueryLimitException() {
        this(null);
    }
}
