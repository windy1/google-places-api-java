package se.walkercrou.places;

import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.io.InputStream;

/**
 * Represents an interface to handle HTTP traffic between the client and Google Places API.
 */
public interface RequestHandler {
    /**
     * Returns an InputStream from the specified URI.
     *
     * @param uri to get input stream for
     * @return input stream at uri
     * @throws IOException
     */
    public InputStream getInputStream(String uri) throws IOException;

    /**
     * Returns the returned data at the specified URI.
     *
     * @param uri to get string data at
     * @return string data at uri
     * @throws IOException
     */
    public String get(String uri) throws IOException;

    /**
     * Posts new data to the server and returns the response as a string.
     *
     * @param data to post
     * @return string returned
     * @throws IOException
     */
    public String post(HttpPost data) throws IOException;
}
