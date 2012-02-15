package fi.silverskin.secureproxy;

import java.util.HashMap;


public class EPICTextResponse extends EPICResponse {
    private String body;

    
    public EPICTextResponse() {
        super();
        this.body = "";
    }

    public EPICTextResponse(HashMap<String, String> headers, String body) {
        super(headers);
        this.body = body;
    }
    
    

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
}
