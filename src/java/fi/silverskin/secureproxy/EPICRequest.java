package fi.silverskin.secureproxy;

import java.util.HashMap;

public class EPICRequest extends EPICAbstraction {
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    
    public enum RequestType {
        POST, PUT, GET, DELETE, HEAD
    }
    
    private RequestType type;
    
    public EPICRequest(RequestType type) {
        this.type = type;
    }

   
    public EPICRequest(String type, HashMap<String, String> headers, String body) {
        super(headers);
        this.body = body;
        this.type = matchType(type);
    }
    
    public EPICRequest(RequestType type, HashMap<String, String> headers, String body) {
        super(headers);
        this.body = body;
    }

    public RequestType getType() {
        return type;
    }
    
    
    private RequestType matchType(String type) {
        if (type.equalsIgnoreCase("post"))
            return RequestType.POST;
        else if (type.equalsIgnoreCase("put"))
            return RequestType.PUT;
        else if (type.equalsIgnoreCase("get"))
            return RequestType.GET;
        else if (type.equalsIgnoreCase("delete"))
            return RequestType.DELETE;
        else if (type.equalsIgnoreCase("put"))
            return RequestType.HEAD;
        else
            throw new RuntimeException("Invalid request type '"+type+"'");
                    
    }    
}
