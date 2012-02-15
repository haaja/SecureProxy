package fi.silverskin.secureproxy;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class EPICAbstraction {

    protected HashMap<String, String> headers;
    private URI uri;

    public EPICAbstraction(HashMap<String, String> header) {
        this.headers = header;
    }

    public EPICAbstraction() {
        this.headers = new HashMap<String, String>();
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public String getUri() {
        if (uri == null) {
            return "";
        }
        return uri.toString();
    }

    public void setUri(String uri) {
        try {
            this.uri = new URI(uri);
        } catch (URISyntaxException ex) {
            //TODO: proper error handling
            Logger.getLogger(EPICAbstraction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}