package fi.silverskin.secureproxy;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HeaderCleaner {

    private static ProxyConfigurer configurer = new ProxyConfigurer();
    public static final String PROTECTEDHOST = configurer.getConfigure("protectedHost")[0];

    private static final Logger LOGGER = Logger.getLogger(HeaderCleaner.class.getName(), null);


    public static EPICRequest cleanHeaders(EPICRequest request) {
        LOGGER.entering(HeaderCleaner.class.getCanonicalName(), "cleanHeaders", request);
        HashMap<String, String> cleanedHeaders = new HashMap<String, String>();
        Map<String, String> originalHeaders = request.getHeaders();

        // headers to be preserved
        String cookieHeader = originalHeaders.get("cookie");
        
        cleanedHeaders.put("cookie", cookieHeader);
        cleanedHeaders.put("host", PROTECTEDHOST);
        
        request.setHeaders(cleanedHeaders);
        LOGGER.exiting(HeaderCleaner.class.getName(), "cleanHeaders", request);
        
        return request;
    }
}
