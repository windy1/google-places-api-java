package se.walkercrou.places;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents a user submitted review.
 */
public class Review {
    private final List<Aspect> aspects = new ArrayList<>();
    private String author, authorUrl, lang, text;
    private int rating;
    private long time;

    protected Review() {
    }

    /**
     * Returns the author of the review.
     *
     * @return the author name
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the review.
     *
     * @param author of review
     * @return this
     */
    protected Review setAuthor(String author) {
        this.author = author;
        return this;
    }

    /**
     * Returns the url associated with the author.
     *
     * @return url
     */
    public String getAuthorUrl() {
        return authorUrl;
    }

    /**
     * Sets the author's url.
     *
     * @param authorUrl to set
     * @return this
     */
    protected Review setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
        return this;
    }

    /**
     * Returns the language the review is in.
     *
     * @return review language
     */
    public String getLanguage() {
        return lang;
    }

    /**
     * Sets the language the review is in.
     *
     * @param lang language of review
     * @return this
     */
    protected Review setLanguage(String lang) {
        this.lang = lang;
        return this;
    }

    /**
     * Returns the content of this review.
     *
     * @return content of review
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the content of this review.
     *
     * @param text content of review
     * @return this
     */
    protected Review setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Returns the rating of this review between 0 and 5.
     *
     * @return rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the rating of this review.
     *
     * @param rating of review
     * @return this
     */
    protected Review setRating(int rating) {
        this.rating = rating;
        return this;
    }

    /**
     * Returns the unix-time stamp that the review was posted on.
     *
     * @return unix-timestamp
     */
    public long getTime() {
        return time;
    }

    /**
     * Sets the unix-time stamp the review was posted on.
     *
     * @param time unix timestamp
     * @return this
     */
    protected Review setTime(long time) {
        this.time = time;
        return this;
    }

    /**
     * Adds a collection of aspects to the review.
     *
     * @param aspects to add to review
     * @return this
     */
    protected Review addAspects(Collection<Aspect> aspects) {
        this.aspects.addAll(aspects);
        return this;
    }

    /**
     * Adds an {@link se.walkercrou.places.Review.Aspect} to the review.
     *
     * @param aspect to add
     * @return this
     */
    protected Review addAspect(Aspect aspect) {
        aspects.add(aspect);
        return this;
    }

    /**
     * Removes the specified aspect from the review.
     *
     * @param aspect to remove
     * @return this
     */
    protected Review removeAspect(Aspect aspect) {
        aspects.remove(aspect);
        return this;
    }

    /**
     * Returns all aspects of the review.
     *
     * @return this
     */
    protected List<Aspect> getAspects() {
        return Collections.unmodifiableList(aspects);
    }

    /**
     * Represents an certain aspect of a review such as "quality" or "service".
     */
    public static class Aspect {
        private final int rating;
        private final String type;

        /**
         * Creates a new Aspect with the specified rating an aspect type.
         *
         * @param rating of aspect
         * @param type   of aspect
         */
        protected Aspect(int rating, String type) {
            this.rating = rating;
            this.type = type;
        }

        /**
         * Returns the rating between 1 and 3
         *
         * @return rating
         */
        public int getRating() {
            return rating;
        }

        /**
         * Returns the type of the aspect.
         *
         * @return aspect type
         */
        public String getType() {
            return type;
        }
    }
}
