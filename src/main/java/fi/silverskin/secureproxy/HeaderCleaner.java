package fi.silverskin.secureproxy;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeaderCleaner {

    private static final Logger LOGGER = Logger.getLogger(HeaderCleaner.class.getName(), null);
    private static String[] headersToBePreserved = { "cookie", "content-type" };

    /**
     * Cleans request headers of everything else except the required ones.
     * @param request Original HTTP request
     * @return HTTP request with cleaned headers
     */
    public static EPICRequest cleanHeaders(EPICRequest request, Properties configuration) {
        LOGGER.entering(HeaderCleaner.class.getName(),
                        "cleanHeaders",
                        request);

        HashMap<String, String> cleanedHeaders = new HashMap<String, String>();
        Map<String, String> originalHeaders = request.getHeaders();

        for (int i=0; i<headersToBePreserved.length; i++) {
            if (originalHeaders.containsKey(headersToBePreserved[i])) {
                cleanedHeaders.put(headersToBePreserved[i],
                originalHeaders.get(headersToBePreserved[i]));
            }
        }

        cleanedHeaders.put("host",
                           configuration.getProperty("protectedHost"));
        request.setHeaders(cleanedHeaders);
        
        LOGGER.exiting(HeaderCleaner.class.getName(), "cleanHeaders", request);
        return request;
    }

    /**
     * Masks Location header url if it contains our protected url
     * @param response HTTP response received from the protected service
     * @param configuration Configuration of our proxy
     * @return Response with location header masked if needed or original
     *         response
     */
    public static EPICResponse maskLocationHeader(EPICResponse response,
                                               Properties configuration) {
        LOGGER.entering(HeaderCleaner.class.getName(),
                "maskLocationHeader",
                new Object[] {response, configuration});

        Map<String, String> originalHeaders = response.getHeaders();
        URI locationUri = null;
        URI privateUri = null;

        if (originalHeaders.containsKey("Location")) {
            String location = originalHeaders.get("Location");

            try {
                privateUri = new URI(configuration.getProperty("privateURI"));
                locationUri = new URI(location);
            } catch (NullPointerException e) {
                LOGGER.log(Level.SEVERE,
                           "Received NullPointerException",
                           e);
            } catch (URISyntaxException e) {
                LOGGER.log(Level.SEVERE,
                           "Received URISyntaxException",
                           e);
            }

            if (SecureProxyUtilities.isProtectedUrl(privateUri, locationUri)) {
                String mutilatedUrl = configuration.getProperty("publicURI") + locationUri.getRawPath();
                HashMap<String, String> mutilatedHeaders = new HashMap(originalHeaders);
                mutilatedHeaders.put("Location", mutilatedUrl);
                response.setHeaders(mutilatedHeaders);
            }
        }

        LOGGER.exiting(HeaderCleaner.class.getName(), "maskLocationHeader", response);
        return response;
    }
}
