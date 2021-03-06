package fi.silverskin.secureproxy;

import java.net.URI;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EPICRequest extends EPICAbstraction {
    public enum RequestType {
        POST, PUT, GET, DELETE, HEAD
    }
    
    private String body;
    private RequestType type;
    private URI uri;
    private static final Logger LOGGER = 
            Logger.getLogger(EPICRequest.class.getName(), null);

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
        this.type = type;
    }

    /**
     * Returns the body of the request.
     *
     * @return A string representation of the body.
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets the body of the request.
     *
     * @param body A string representation of the body.
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Returns the type of the request.
     *
     * @return Type of the request.
     */
    public RequestType getType() {
        return type;
    }

    /**
     * Matches the HTTP request type with right RequestType.
     *
     * @param type A string representation of the HTTP request type.
     * @return Matching RequestType.
     */
    private RequestType matchType(String type) {
        LOGGER.entering(EPICRequest.class.getName(), "matchType");
        if (type.equalsIgnoreCase("post"))
            return RequestType.POST;
        else if (type.equalsIgnoreCase("put"))
            return RequestType.PUT;
        else if (type.equalsIgnoreCase("get"))
            return RequestType.GET;
        else if (type.equalsIgnoreCase("delete"))
            return RequestType.DELETE;
        else if (type.equalsIgnoreCase("head"))
            return RequestType.HEAD;
        else
            LOGGER.log(Level.SEVERE, "Invalid request type: " +type);
            throw new RuntimeException("Invalid request type '"+type+"'");
                    
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

    /**
     * Returns a string representation of the request.
     *
     * @return A string representation of the request.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Type: ").append(type).append('\n');
        sb.append("URI : ").append(getUri().toString()).append('\n');
        sb.append("Headers:\n");
        for(Entry entry : getHeaders().entrySet()) {
            sb.append('\t').append(entry.getKey()).append(":").append(entry.getValue()).append('\n');
        }
        
        sb.append("Body:\n").append(getBody());
        
        return sb.toString();
    }
}
