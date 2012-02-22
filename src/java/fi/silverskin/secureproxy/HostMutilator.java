package fi.silverskin.secureproxy;

import java.util.HashMap;

public class HostMutilator {

    public static final String remoteHost = "tkt_palo.users.cs.helsinki.fi";

    public static EPICRequest mutilateRequest(EPICRequest request) {
        HashMap<String, String> headers = new HashMap<String, String>(request.getHeaders());

        headers.put("host", remoteHost);
        headers.remove("accept-encoding");
        headers.remove("connection");
        headers.remove("keep-alive");
        
        request.setHeaders(headers);
        return request;
    }
}
