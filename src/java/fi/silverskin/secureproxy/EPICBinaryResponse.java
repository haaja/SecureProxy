package fi.silverskin.secureproxy;

import java.io.InputStream;
import java.util.HashMap;


public class EPICBinaryResponse extends EPICResponse {
    private InputStream body;

    public EPICBinaryResponse() {
        super();
    }
    
    public EPICBinaryResponse(InputStream body) {
        super();
        this.body = body;
    }
    
    public EPICBinaryResponse(HashMap<String, String> headers, InputStream body) {
        super(headers);
        this.body = body;
    }

    public InputStream getBody() {
        return body;
    }

    public void setBody(InputStream body) {
        this.body = body;
    }
}
