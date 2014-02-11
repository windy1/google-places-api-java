package me.windwaker.places.exception;

public class GooglePlacesException extends RuntimeException {
	private final String statusCode;

	public GooglePlacesException(String statusCode, String msg) {
		super(msg);
		this.statusCode = statusCode;
	}

	public String getStatusCode() {
		return statusCode;
	}
}
