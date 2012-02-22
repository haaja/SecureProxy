package fi.silverskin.secureproxy;

import java.util.HashMap;
import java.util.Map;

public class EPICResponse extends EPICAbstraction {

    public EPICResponse() {
    }

    public EPICResponse(HashMap<String, String> headers, String body) {
        super(headers, body);
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
