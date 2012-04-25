package fi.silverskin.secureproxy;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SecureProxyUtilities {

    private static final Logger LOGGER = 
            Logger.getLogger(SecureProxyUtilities.class.getName(), null);

    /**
     * Creates URI object from the String parameter
     *
     * Null return value should only occur in case of non standard compliant
     * www page.
     *
     * @param url URL from which we are creating the URI
     * @return URI created from the supplied parameter or null if the method
     *         fails
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
     * Generate a random string containing charachters a..z and 0..9
     * 
     * @param length length of the string
     * @return an empty string, if length = 0, a random string otherwise
     */
    public static String getRandomString(int length){
        /*
        String rs = "";
        Random generator = new Random();
        if(length == 0) return rs;
        for(int i=0; i<length; i++){
            int rn = generator.nextInt(36);
            if(rn < 10) rs = rs + (char) (48+rn);
            else rs = rs + (char) (87+rn);
        }
        return rs;
        * */
        return UUID.randomUUID().toString();
    }

    /**
     * Checks if uri is protected uri
     *
     * @param protectedUri Url of the protected service
     * @param locationUri Url in the location header
     * @return true if location url is the one we are protecting, otherwise
     *         false
     */
    public static boolean isProtectedUrl(URI privateUri, URI locationUri) {
        LOGGER.entering(HeaderCleaner.class.getName(),
                "isProtectedUrl",
                new Object[]{privateUri, locationUri});

        if (!locationUri.isAbsolute()) {
            LOGGER.exiting(SecureProxyUtilities.class.getName(),
                    "isProtectedUrl", true);
            return true;
        }

        boolean retVal = false;
        String hostname = locationUri.getHost();
        LOGGER.log(Level.INFO, "Public host: ''{0}''", locationUri.getHost());
        LOGGER.log(Level.INFO, "Private host: ''{0}''", privateUri.getHost());

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
