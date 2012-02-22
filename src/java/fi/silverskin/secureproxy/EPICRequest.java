package fi.silverskin.secureproxy;

import java.util.HashMap;
import java.util.Map.Entry;

public class EPICRequest extends EPICAbstraction {

    
    public enum RequestType {
        POST, PUT, GET, DELETE, HEAD
    }
    
    private RequestType type;
    
    public EPICRequest(RequestType type) {
        this.type = type;
    }

   
    public EPICRequest(String type, HashMap<String, String> headers, String body) {
        super(headers, body);
        this.type = matchType(type);
    }
    
    public EPICRequest(RequestType type, HashMap<String, String> headers, String body) {
        super(headers, body);
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
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Type: ").append(type).append('\n');
        
        sb.append("URI : ").append(getUri()).append('\n');
        
        sb.append("Headers:\n");
        for(Entry entry : getHeaders().entrySet()) {
            sb.append('\t').append(entry.getKey()).append(":").append(entry.getValue()).append('\n');
        }
        
        sb.append("Body:\n").append(getBody());
        
        return sb.toString();
    }
}
