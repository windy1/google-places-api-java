package me.windwaker.places;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import static me.windwaker.places.GooglePlaces.*;

/**
 * Represents an Event at a {@link me.windwaker.places.Place}.
 */
public class Event {
	private Place place;
	private String id;
	private String summary, url, lang;
	private long startTime, duration;

	protected Event() {
	}

	/**
	 * Sets the unique id of this event
	 *
	 * @param id to set
	 * @return this
	 */
	public Event setId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Returns the unique ID of the event.
	 *
	 * @return unique id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the place this event is taking place at.
	 *
	 * @return event place
	 */
	public Place getPlace() {
		return place;
	}

	/**
	 * Sets the place of this event.
	 *
	 * @param place of event
	 * @return this
	 */
	public Event setPlace(Place place) {
		this.place = place;
		return this;
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

	/**
	 * Returns the language of the event.
	 *
	 * @return language of event
	 */
	public String getLanguage() {
		return lang;
	}

	/**
	 * Sets the language of the event.
	 *
	 * @param lang language of event
	 * @return this
	 */
	public Event setLanguage(String lang) {
		this.lang = lang;
		return this;
	}

	/**
	 * Returns the duration of the event, in seconds.
	 *
	 * @return duration of event
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * Sets the duration of the event, in seconds.
	 *
	 * @param duration in seconds
	 * @return this
	 */
	public Event setDuration(long duration) {
		this.duration = duration;
		return this;
	}

	/**
	 * Bumps an event within the application. Bumps are reflected in your place searches for your application only.
	 * Bumping an event makes it appear higher in the result set.
	 *
	 * @param extraParams to append to request url
	 */
	public Event bump(Param... extraParams) {
		place.getClient().bumpEvent(this, extraParams);
		return this;
	}

	/**
	 * Returns a more detailed version of this event.
	 *
	 * @return event details
	 * @throws IOException
	 */
	public Event getDetails() throws IOException {
		return place.getClient().getEvent(place, id);
	}

	/**
	 * Builds a JSON representation of this event.
	 *
	 * @return json representation of event
	 */
	public static JSONObject buildInput(long duration, String lang, String reference, String summary, String url) {
		return new JSONObject().put("duration", duration).putOpt("language", lang)
				.put("reference", reference).put("summary", summary).putOpt("url", url);
	}

	/**
	 * Parses an event from a event/details request.
	 *
	 * @param rawJson to parse
	 * @return this
	 */
	public static Event parseDetails(String rawJson) {
		JSONObject json = new JSONObject(rawJson);
		checkStatus(json.getString(STRING_STATUS));

		JSONObject result = json.getJSONObject(OBJECT_RESULT);

		long duration = result.getLong(LONG_DURATION);
		String eventId = result.getString(STRING_EVENT_ID);
		long startTime = result.getLong(LONG_START_TIME);
		String summary = result.getString(STRING_SUMMARY);
		String url = result.getString(STRING_URL);

		return new Event().setId(eventId).setDuration(duration).setStartTime(startTime).setSummary(summary).setUrl(url);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Event && ((Event) obj).id.equals(id);
	}
}
