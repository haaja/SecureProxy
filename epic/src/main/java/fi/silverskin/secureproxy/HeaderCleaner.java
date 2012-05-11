package fi.silverskin.secureproxy;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class HeaderCleaner {

    private static final Logger LOGGER = 
            Logger.getLogger(HeaderCleaner.class.getName(), null);
    private static String[] headersToBePreserved = 
            { "cookie", "content-type", "referer" };

    /**
     * Removes all headers of the HTTP request, except the once specified in
     * headersToBePreserved variable.
     * 
     * @param request Original HTTP request
     * @return HTTP request with cleaned headers
     */
    public static EPICRequest cleanHeaders(EPICRequest request, 
                                           Properties configuration) {
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

        request = changeHostHeader(request, configuration, cleanedHeaders);
        
        LOGGER.exiting(HeaderCleaner.class.getName(), "cleanHeaders", request);
        return request;
    }
    
    /**
     * Changes the host header of the HTTP request to the real service.
     * 
     * @param request HTTP request
     * @param configuration Our proxy configuration
     * @param cleanHeaders Headers cleaned with cleanHeaders method
     * @return HTTP request with changed host header
     */
    public static EPICRequest changeHostHeader(EPICRequest request, 
                                               Properties configuration, 
                                               HashMap<String, String> cleanHeaders) {
        
        String protectedHost = configuration.getProperty("protectedHost");
        cleanHeaders.put("host", protectedHost);
        request.setHeaders(cleanHeaders);
        
        return request;
      
    }
    
    
    /**
     * Masks Location header url if it contains our protected url
     * 
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
        if (originalHeaders.containsKey("Location")) {
            String locationUrl = originalHeaders.get("Location");
            URI privateUri = 
                    SecureProxyUtilities.makeUriFromString(configuration.getProperty("privateURI"));
            URI locationUri = SecureProxyUtilities.makeUriFromString(locationUrl);

            if (SecureProxyUtilities.isProtectedUrl(privateUri, locationUri)) {
                String mutilatedUrl = 
                        buildMaskedLocationUrl(locationUri, configuration);
                HashMap<String, String> mutilatedHeaders = 
                        new HashMap(originalHeaders);
                
                mutilatedHeaders.put("Location", mutilatedUrl);
                response.setHeaders(mutilatedHeaders);
            }
                    

        }
        
        LOGGER.exiting(HeaderCleaner.class.getName(), 
                       "maskLocationHeader", 
                       response);
        
        return response;
    }

    /**
     * Builds masked location url
     * 
     * @param locationUri Real location url found in response headers
     * @param conf Configuration properties
     * @return Masked location url
     */
    private static String buildMaskedLocationUrl(URI locationUri,
                                                 Properties conf) {
        LOGGER.entering(HeaderCleaner.class.getName(), 
                        "buildMaskedUrl",
                        new Object[] {locationUri, conf});
        URI publicUri = null;
        String maskedUrl = null;

        if (locationUri.isAbsolute()) {
            publicUri = 
                    SecureProxyUtilities.makeUriFromString(conf.getProperty("publicURI"));
            String scheme = locationUri.getScheme();
            String port;
            if (scheme != null) {
                port = scheme.equals("http") ? 
                       conf.getProperty("publicHttpPort") :
                       conf.getProperty("publicHttpsPort");
                maskedUrl = scheme + "://" +
                            publicUri.getHost() +
                            ":" + port +
                            locationUri.getRawPath();
            }
        } else {
            /* 
             * Should never go here as location header is required to be 
             * absolute. Nevertheless assuming HTTP protocol
             */
            maskedUrl = "http://" + publicUri.getHost() + locationUri.getRawPath();
        }

        LOGGER.info("MaskedUrlBuilt: " + maskedUrl);
        LOGGER.exiting(HeaderCleaner.class.getName(), 
                       "buildMaskedUrl", 
                       maskedUrl);
        return maskedUrl;
    }
}
