package me.windwaker.places;

import java.util.Date;

/**
 * Represents an Event at a {@link me.windwaker.places.Place}.
 */
public class Event {
	private final String id;
	private String summary, url;
	private long startTime;

	/**
	 * Creates a new Event with the specified unique id.
	 *
	 * @param id of event
	 */
	public Event(String id) {
		this.id = id;
	}

	/**
	 * Sets the summary of the event.
	 *
	 * @param summary of event
	 * @return this
	 */
	public Event setSummary(String summary) {
		this.summary = summary;
		return this;
	}

	/**
	 * Returns the summary of the event.
	 *
	 * @return summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * Sets the url associated with this event.
	 *
	 * @param url of event
	 * @return this
	 */
	public Event setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * Returns the url associated with this event.
	 *
	 * @return url of event
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the Unix timestamp of the event's date.
	 *
	 * @param startTime unix timestamp
	 * @return this
	 */
	public Event setStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	/**
	 * Returns the Unix timestamp of the event's date.
	 *
	 * @return start time unix timestamp
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * Returns the Date that this event will start.
	 *
	 * @return date the event will start
	 */
	public Date getStartDate() {
		return new Date(startTime * 1000);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Event && ((Event) obj).id.equals(id);
	}
}
