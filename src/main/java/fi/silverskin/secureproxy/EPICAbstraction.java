package fi.silverskin.secureproxy;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
    public URI getUri() {
        LOGGER.entering(EPICAbstraction.class.getName(), "getUri");
        LOGGER.exiting(EPICAbstraction.class.getName(), "getUri", uri);
        return uri;
    }

    /**
     * Sets the URI of the request or response.
     *
     * @param uri A string representation of the URI.
     */
    public void setUri(String uri) {
        LOGGER.entering(EPICAbstraction.class.getName(), "setUri", uri);
        this.uri = SecureProxyUtilities.makeUriFromString(uri);
        LOGGER.exiting(EPICAbstraction.class.getName(), "setUri");
    }   
}