package me.windwaker.places;

import java.awt.image.BufferedImage;

/**
 * Represents a referenced photo.
 */
public class Photo {
	private final Place place;
	private final String reference;
	private final int width, height;

	protected Photo(Place place, String reference, int width, int height) {
		this.place = place;
		this.reference = reference;
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns an Image from the specified photo reference.
	 *
	 * @param maxWidth of image
	 * @param maxHeight of image
	 * @param extraParams to append to request url
	 * @return image
	 */
	public BufferedImage getImage(int maxWidth, int maxHeight, GooglePlaces.Param... extraParams) {
		return place.getClient().getImage(this, maxWidth, maxHeight, extraParams);
	}

	/**
	 * Returns an Image from the specified photo reference in it's original form.
	 *
	 * @param extraParams to append to request url
	 * @return image
	 */
	public BufferedImage getImage(GooglePlaces.Param... extraParams) {
		return getImage(-1, -1, extraParams);
	}

	/**
	 * Returns the reference token to the photo.
	 *
	 * @return reference token
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * Returns the width of the photo.
	 *
	 * @return photo width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the photo.
	 *
	 * @return photo height
	 */
	public int getHeight() {
		return height;
	}
}
