package fi.silverskin.secureproxy;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


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