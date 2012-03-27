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
     * @return
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

            if (isProtectedUrl(privateUri, locationUri)) {
                String mutilatedUrl = configuration.getProperty("publicURI") + locationUri.getPath();
                HashMap<String, String> mutilatedHeaders = new HashMap(originalHeaders);
                mutilatedHeaders.put("Location", mutilatedUrl);
                response.setHeaders(mutilatedHeaders);
            }
        }

        LOGGER.exiting(HeaderCleaner.class.getName(), "maskLocationHeader", response);
        return response;
    }

    /**
     * Checks if uri is protected uri
     *
     * @param protectedUri Url of the protected service
     * @param locationUri  Url in the location header
     * @return true if location url is the one we are protecting, otherwise
     *         false
     */
    private static boolean isProtectedUrl(URI privateUri, URI locationUri) {
        LOGGER.entering(HeaderCleaner.class.getName(),
                        "isProtectedUrl",
                        new Object[] { privateUri, locationUri});

        LOGGER.info("PROTECTEDURI: " +privateUri.getHost());
        LOGGER.info("LOCATIONURI: "+ locationUri.getHost());

        boolean retVal = false;
        String hostname = locationUri.getHost();

        if (hostname.equals(privateUri.getHost())) {
            retVal = true;
        } else {
            retVal = false;
        }

        LOGGER.exiting(HeaderCleaner.class.getName(), "isProtectedUrl", retVal);
        return retVal;
    }
}
