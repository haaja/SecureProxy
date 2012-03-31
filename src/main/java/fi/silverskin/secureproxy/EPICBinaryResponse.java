package fi.silverskin.secureproxy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EPICBinaryResponse extends EPICResponse {
    private byte[] body;
    private static final Logger LOGGER = Logger.getLogger(EPICBinaryResponse.class.getName());

    public EPICBinaryResponse() {
        super();
    }
    
    public EPICBinaryResponse(InputStream body) {
        super();
        this.body = fromInputStream(body);
    }
    
    public EPICBinaryResponse(HashMap<String, String> headers, InputStream body) {
        super(headers);
        this.body = fromInputStream(body);
    }

    /**
     * Returns the body of the response.
     *
     * @return Body of the response.
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * Returns the body of the response.
     *
     * @param body Body of the response.
     */
    public void setBody(InputStream body) {
        this.body = fromInputStream(body);
    }
    
    public void setBody(byte[] body) {
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

        sb.append("Headers:\n");
        for (Map.Entry entry : getHeaders().entrySet()) {
            sb.append('\t').append(entry.getKey()).append(":").append(entry.getValue()).append('\n');
        }
        sb.append("Body:\n").append(getBody());
        return sb.toString();
    }    
    
    
    
    private byte[] fromInputStream(InputStream in) {
        LOGGER.entering(EPICBinaryResponse.class.getName(), "fromInputStream", in);
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        
        try {            
            byte[] b = new byte[0x1000];
            int read = in.read(b);
            while(read > -1) {
                buff.write(b, 0, read);
                read = in.read(b);
            }
            
            buff.flush();
            in.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return new byte[0];
        }

        LOGGER.exiting(EPICBinaryResponse.class.getName(), "fromInputStream", in);

        return buff.toByteArray();
    }
}
