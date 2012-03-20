package fi.silverskin.secureproxy;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class HeaderCleaner {

    private static final Logger LOGGER = Logger.getLogger(HeaderCleaner.class.getName(), null);
    private static String[] headersToBePreserved = { "cookie" };

    /**
     * Cleans request headers of everything else except the required ones.
     * @param request Original HTTP request
     * @return HTTP request with cleaned headers
     */
    public static EPICRequest cleanHeaders(EPICRequest request, Properties configuration) {
        LOGGER.entering(HeaderCleaner.class.getCanonicalName(),
                        "cleanHeaders",
                        request);

        HashMap<String, String> cleanedHeaders = new HashMap<String, String>();
        Map<String, String> originalHeaders = request.getHeaders();

        for (int i=0; i<headersToBePreserved.length; i++) {
            cleanedHeaders.put(headersToBePreserved[i],
            originalHeaders.get(headersToBePreserved[i]));
        }

        cleanedHeaders.put("host",
                           configuration.getProperty("protectedHost"));
        request.setHeaders(cleanedHeaders);
        
        LOGGER.exiting(HeaderCleaner.class.getName(), "cleanHeaders", request);
        return request;
    }
    
}
