package se.walkercrou.places;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Simon on 2014/12/20.
 */
public class GAERequestHandler implements RequestHandler {

    public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
    private String characterEncoding;

    public GAERequestHandler(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }
    public GAERequestHandler() {
        this(DEFAULT_CHARACTER_ENCODING);
    }

    private String readString(InputStream inputStream) throws IOException {
        String str = IOUtils.toString(inputStream, characterEncoding);
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        return str.trim();
    }

    @Override
    public InputStream getInputStream(String uri) throws IOException {

        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        //I have used the application/json constant here as the Places and this library uses the JSON format
        conn.setRequestProperty("Content-Type", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }
        return conn.getInputStream();
    }

    @Override
    public String get(String uri) throws IOException {

        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        //I have used the application/json constant here as the Places and this library uses the JSON format
        conn.setRequestProperty("Content-Type", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }
        StringBuilder str = new StringBuilder();
        String line = null;

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        while ((line = br.readLine()) != null) {
            str.append(line);
        }
        return str.toString();
    }


    public String post(String data, String uri) throws IOException {

        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //this must be true as we are sending out a request body with our POST request
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        //we are sending out a json
        conn.setRequestProperty("Content-Type", "application/json");

        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        out.write(data);
        out.flush();
        out.close();

        //check to see whether the http has failed.
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());

        } else {

            //getting the response and writing it out to a reader.
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
            in.close();
            return builder.toString();
        }

    }

}
