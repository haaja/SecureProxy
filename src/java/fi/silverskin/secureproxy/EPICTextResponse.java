package fi.silverskin.secureproxy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


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

    /**
     * Returns the body of the response.
     *
     * @return Body of the response.
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets the body of the response.
     *
     * @param body Body of the response.
     */
    public void setBody(String body) {
        this.body = body;
    }
  
    /**
     * Returns a string representation of the response.
     * 
     * @return A string representation of the response.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("URI : ").append(getUri()).append('\n');
        sb.append("Headers:\n");
        for (Map.Entry entry : getHeaders().entrySet()) {
            sb.append('\t').append(entry.getKey()).append(":").append(entry.getValue()).append('\n');
        }
        sb.append("Body:\n").append(getBody());
        return sb.toString();
    }
}
