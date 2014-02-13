package me.windwaker.places;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * HTTP utility class.
 */
public final class HttpUtil {
	/**
	 * The default HttpClient
	 */
	public static final HttpClient DEFAULT_CLIENT = new DefaultHttpClient();

	private HttpUtil() {
	}


	/**
	 * Returns the raw GET response from the specified uri.
	 *
	 * @param client to use
	 * @param get    to send
	 * @return string response
	 * @throws IOException
	 */
	protected static String get(HttpClient client, HttpGet get) throws IOException {
		try {
			return readString(client.execute(get));
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * Returns the raw GET response from the specified uri.
	 *
	 * @param client to use
	 * @param uri    to send
	 * @return string response
	 * @throws IOException
	 */
	protected static String get(HttpClient client, String uri) throws IOException {
		try {
			return get(client, new HttpGet(uri));
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * Returns the raw POST response from the specified uri.
	 *
	 * @param client to use
	 * @param post to send
	 * @return string response
	 * @throws IOException
	 */
	protected static String post(HttpClient client, HttpPost post) throws IOException {
		try {
			return readString(client.execute(post));
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * Returns the raw POST response from the specified uri.
	 *
	 * @param client to use
	 * @param uri to send
	 * @return string response
	 * @throws IOException
	 */
	protected static String post(HttpClient client, String uri) throws IOException {
		try {
			return post(client, new HttpPost(uri));
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	private static final String CHARACTER_ENCODING = "UTF-8";

	/**
	 * Converts an HttpResponse to a string.
	 *
	 * @param response returned
	 * @return string response
	 * @throws IOException
	 */
	protected static String readString(HttpResponse response) throws IOException {
		String str = IOUtils.toString(response.getEntity().getContent(), CHARACTER_ENCODING);
		if (str == null || str.trim().length() == 0) {
			return null;
		}
		return str.trim();
	}
}
