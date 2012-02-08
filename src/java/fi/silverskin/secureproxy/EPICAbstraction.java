package fi.silverskin.secureproxy;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class EPICAbstraction {

    private HashMap<String, String> headers;
    private String body;
    private URI uri;

    public EPICAbstraction(HashMap<String, String> headers, String body) {
        this.headers = headers;
        this.body = body;
    }

    public EPICAbstraction() {
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public String getUrl() {
        return uri.toString();
    }

    public void setUrl(String uri) {
        try {
            this.uri = new URI(uri);
        } catch (URISyntaxException ex) {
            //TODO: proper error handling
            Logger.getLogger(EPICAbstraction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}