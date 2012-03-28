package fi.silverskin.secureproxy;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SecureProxyUtilities {

    private static final Logger LOGGER = Logger.getLogger(SecureProxyUtilities.class.getName(), null);

    /*
     * Tries to make URI object from String
     * @param url URL in string
     * @return URI object formed from the parameter or null
     */
    public static URI makeUriFromString(String url) {
        LOGGER.entering(SecureProxyUtilities.class.getName(), 
                        "makeUriFromString",
                        url);
        URI uri = null;

        try {
            URL tempURL = new URL(url);
            uri = new URI(tempURL.getProtocol(),
                          tempURL.getAuthority(),
                          tempURL.getPath(),
                          tempURL.getQuery(),
                          tempURL.getRef());
        } catch (MalformedURLException ex) {
            LOGGER.log(Level.SEVERE, 
                       "Received MalformedURLException with: " + url,
                       ex);
            try {
                uri = new URI(url);
            } catch (URISyntaxException e) {
                LOGGER.log(Level.SEVERE, 
                           "Received URISyntaxException with: " + url,
                           e);
            } catch (NullPointerException e) {
                LOGGER.log(Level.SEVERE, 
                           "Received NullPointerException with: " + url,
                           e);
            }
        } catch (URISyntaxException ex) {
                LOGGER.log(Level.SEVERE, 
                           "Received URISyntaxException with: " + url,
                           ex);
        }

        LOGGER.exiting(SecureProxyUtilities.class.getName(), 
                       "makeUriFromString",
                       uri);
        return uri;
    }

    /**
     * Checks if uri is protected uri
     *
     * @param protectedUri Url of the protected service
     * @param locationUri  Url in the location header
     * @return true if location url is the one we are protecting, otherwise
     *         false
     */
    public static boolean isProtectedUrl(URI privateUri, URI locationUri) {
        LOGGER.entering(HeaderCleaner.class.getName(),
                        "isProtectedUrl",
                        new Object[] {privateUri, locationUri});

        if (!locationUri.isAbsolute()) {
            LOGGER.exiting(SecureProxyUtilities.class.getName(),
                           "isProtectedUrl", true);
            return true;
        }

        boolean retVal = false;
        String hostname = locationUri.getHost();

        if (hostname.equals(privateUri.getHost())) {
            retVal = true;
        } else {
            retVal = false;
        }

        LOGGER.exiting(SecureProxyUtilities.class.getName(), 
                       "isProtectedUrl",
                       retVal);
        return retVal;
        
    }
}
