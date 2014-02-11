package me.windwaker.places;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Review {
	private final List<Aspect> aspects = new ArrayList<Aspect>();
	private String author, authorUrl, lang, text;
	private int rating;
	private long time;

	public String getAuthor() {
		return author;
	}

	public Review setAuthor(String author) {
		this.author = author;
		return this;
	}

	public String getAuthorUrl() {
		return authorUrl;
	}

	public Review setAuthorUrl(String authorUrl) {
		this.authorUrl = authorUrl;
		return this;
	}

	public String getLanguage() {
		return lang;
	}

	public Review setLanguage(String lang) {
		this.lang = lang;
		return this;
	}

	public String getText() {
		return text;
	}

	public Review setText(String text) {
		this.text = text;
		return this;
	}

	public int getRating() {
		return rating;
	}

	public Review setRating(int rating) {
		this.rating = rating;
		return this;
	}

	public long getTime() {
		return time;
	}

	public Review setTime(long time) {
		this.time = time;
		return this;
	}

	public Review addAspects(Collection<Aspect> aspects) {
		this.aspects.addAll(aspects);
		return this;
	}

	public Review addAspect(Aspect aspect) {
		aspects.add(aspect);
		return this;
	}

	public Review removeAspect(Aspect aspect) {
		aspects.remove(aspect);
		return this;
	}

	public List<Aspect> getAspects() {
		return Collections.unmodifiableList(aspects);
	}

	public static class Aspect {
		private final int rating;
		private final String type;

		public Aspect(int rating, String type) {
			this.rating = rating;
			this.type = type;
		}

		public int getRating() {
			return rating;
		}

		public String getType() {
			return type;
		}
	}
}
