package fi.silverskin.secureproxy;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class EPICTextResponse extends EPICResponse {
    private String body;
    private static final Logger LOGGER = 
            Logger.getLogger(EPICTextResponse.class.getName(), null);
    
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
        updateContentLength();
    }
  
    /**
     * Returns a string representation of the response.
     * 
     * @return A string representation of the response.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Headers:\n");
        for (Map.Entry entry : getHeaders().entrySet()) {
            sb.append('\t').append(entry.getKey()).append(":").append(entry.getValue()).append('\n');
        }
        sb.append("Body:\n").append(getBody());
        return sb.toString();
    }
    
    /**
     * Updates Content-Length header with a new value after masking the urls.
     *
     * See: https://en.wikipedia.org/wiki/Chunked_transfer_encoding
     */
    private void updateContentLength() {
        LOGGER.entering(EPICTextResponse.class.getName(), "updateContentLength");

        //if the http server uses chunked encoding
        if (!this.getHeaders().containsKey("Transfer-Encoding")) {
            //Update Content-Lenght with new size
            HashMap<String, String> headers = 
                    new HashMap<String, String>(this.getHeaders());
            headers.put("Content-Length", 
                        Integer.toString(this.getBody().getBytes().length));
            this.setHeaders(headers);
        }

        LOGGER.exiting(EPICTextResponse.class.getName(), "updateContentLength");
    }
}
