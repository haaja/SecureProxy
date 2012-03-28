package fi.silverskin.secureproxy;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class EPICAbstraction {

    protected HashMap<String, String> headers;
    private URI uri;
    private static final Logger LOGGER = Logger.getLogger(EPICAbstraction.class.getName(), null);

    public EPICAbstraction(HashMap<String, String> header) {
        this.headers = header;
    }

    public EPICAbstraction() {
        this.headers = new HashMap<String, String>();
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    /**
     * Sets the headers of the request or response.
     *
     * @param headers Headers of the request or response.
     */
    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    /**
     * Returns the URI of the request or response.
     *
     * @return A string representation of the URI.
     */
    public String getUri() {
        LOGGER.entering(EPICAbstraction.class.getName(), "getUri");
        if (uri == null) {
            return "";
        }
        LOGGER.exiting(EPICAbstraction.class.getName(), "getUri", uri.toString());
        return uri.toString();
    }

    /**
     * Sets the URI of the request or response.
     *
     * @param uri A string representation of the URI.
     */
    public void setUri(String uri) {
        LOGGER.entering(EPICAbstraction.class.getName(), "setUri", uri);
        this.uri = makeUriFromString(uri);

        LOGGER.exiting(EPICAbstraction.class.getName(), "setUri");
    }

    /**
     * Tries to make URI object from String
     * @param url URL in string
     * @return URI object formed from the parameter or null
     */
    private URI makeUriFromString(String url) {
        LOGGER.entering(EPICAbstraction.class.getName(), "makeUriFromString", url);
        URI uri = null;

        try {
            URL tempURL = new URL(url);
            uri = new URI(tempURL.getProtocol(),
                          tempURL.getAuthority(),
                          tempURL.getPath(),
                          tempURL.getQuery(),
                          tempURL.getRef());
        } catch (MalformedURLException ex) {
            LOGGER.log(Level.SEVERE, "Received MalformedURLException with: " + url, ex);
            try {
                uri = new URI(url);
            } catch (URISyntaxException e) {
                LOGGER.log(Level.SEVERE, "Received URISyntaxException with: " + url, e);
            } catch (NullPointerException e) {
                LOGGER.log(Level.SEVERE, "Received NullPointerException with: " + url, e);
            }
        } catch (URISyntaxException ex) {
                LOGGER.log(Level.SEVERE, "Received URISyntaxException with: " + url, ex);
        }

        LOGGER.exiting(EPICAbstraction.class.getName(), "makeUriFromString", uri);
        return uri;
    }
}