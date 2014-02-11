package me.windwaker.places;

/**
 * Represents a referenced photo.
 */
public class Photo {
	private final String reference;
	private final int width, height;

	/**
	 * Creates a new photo with the specified reference token, width and height.
	 *
	 * @param reference token to photo
	 * @param width     of photo
	 * @param height    of photo
	 */
	public Photo(String reference, int width, int height) {
		this.reference = reference;
		this.width = width;
		this.height = height;
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
