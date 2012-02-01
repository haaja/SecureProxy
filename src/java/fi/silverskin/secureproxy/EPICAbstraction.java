package fi.silverskin.secureproxy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class EPICAbstraction {

    private HashMap<String, String> headers;
    private String body;

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
}