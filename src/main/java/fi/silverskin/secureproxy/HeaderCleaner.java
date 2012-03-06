package fi.silverskin.secureproxy;

import java.util.HashMap;
import java.util.Map;

public class HeaderCleaner {

    private static ProxyConfigurer configurer = new ProxyConfigurer();
    public static final String protectedHost = configurer.getConfigure("protectedHost")[0];
    //public static final String protectedHost = "tkt_palo.users.cs.helsinki.fi";

    public static EPICRequest cleanHeaders(EPICRequest request) {
        HashMap<String, String> cleanedHeaders = new HashMap<String, String>();
        Map<String, String> originalHeaders = request.getHeaders();

        // headers to be preserved
        String cookieHeader = originalHeaders.get("cookie");
        
        cleanedHeaders.put("cookie", cookieHeader);
        cleanedHeaders.put("host", protectedHost);
        
        request.setHeaders(cleanedHeaders);
        
        return request;
    }
}
