package fi.silverskin.secureproxy.hackandslash;

import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICTextResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HackAndSlash {

    private static final Logger LOGGER = Logger.getLogger(HackAndSlash.class.getName(), null);
    private URI privateURI;
    private String privatePort;
    private URI publicURI;

    public HackAndSlash(HackAndSlashConfig conf) {
        privateURI = conf.getprivateURI();
        privatePort = conf.getprivatePort();
        publicURI = conf.getpublicURI();
    }

    /**
     * Modifies requests to contain the real URI of the service.
     *
     * @param request HTTP request.
     * @return HTTP request containing the real URI of the service.
     */
    public EPICRequest hackAndSlashIn(EPICRequest request) {
        LOGGER.entering(HackAndSlash.class.getName(), "hackAndSlashIn", request);
        String modifiedUri;
        try {
            URI uri = new URI(request.getUri());
            modifiedUri = privateURI + ":" + privatePort + uri.getPath();
            if (uri.getQuery() != null) {
                modifiedUri = modifiedUri + "?" + uri.getQuery();
            }
            if (uri.getFragment() != null) {
                modifiedUri = modifiedUri + "#" + uri.getFragment();
            }
            
            request.setUri(modifiedUri);
            LOGGER.log(Level.INFO, request.getUri().toString());
        } catch (URISyntaxException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        LOGGER.exiting(HackAndSlash.class.getName(), "hackAndSlashIn", request);

        return request;
    }

    /**
     * Modifies HTTP response to hide traces of the real service.
     *
     * @param response HTTP response with real data.
     * @return Modified HTTP response.
     */
    public EPICTextResponse hackAndSlashOut(EPICTextResponse response) {
        LOGGER.entering(HackAndSlash.class.getName(), "hackAndSlashOut", response);
        String oldResponse = response.getBody();
        String newResponse = oldResponse.replaceAll(privateURI.toString(), publicURI.toString());
        
        response.setBody(newResponse);
        response = updateContentLength(response);
        
        LOGGER.exiting(HackAndSlash.class.getName(), "hackAndSlashOut", response);
        return response;
    }

    /**
     * Generates the pseudo URI from original URI
     *
     * TODO: Actual masking of the url and filesystem.
     *
     * @param url Original URI
     * @return Masked URI
     */
    public String getMaskedUrl(String url) {

        LOGGER.info("getMaskedUrl's param: " + url);
        LOGGER.entering(HackAndSlash.class.getName(), "getMaskedUrl", url);
        /* fixing common mistakes in page sources */
        url = url.trim().replace(" ", "%20");
        URI parsedUri = null;
        String maskedUri;

        try {
            parsedUri = new URI(url);
        } catch (NullPointerException ex) {
          LOGGER.log(Level.SEVERE, "Received NullPointerException", ex);
        } catch (URISyntaxException ex) {
            LOGGER.log(Level.SEVERE, "Received URISyntaxException", ex);
        }

        if (!isProtectedUrl(parsedUri)) {
            return url;
        }

        if (parsedUri.isAbsolute()) {
            maskedUri = publicURI + parsedUri.getPath();

            /* if url has query part */
            if (parsedUri.getQuery() != null) {
                maskedUri = maskedUri + "?" + parsedUri.getQuery();
            }
            /* if url is using fragment (#service) */
            if (parsedUri.getFragment() != null) {
                maskedUri = maskedUri + "#" + parsedUri.getFragment();
            }
        } else {
            maskedUri = publicURI + url;
        }

        LOGGER.log(Level.INFO, "Returning masked url: {0}", maskedUri);
        LOGGER.exiting(HackAndSlash.class.getName(), "getMaskedUrl", maskedUri);

        return maskedUri;
    }

    /**
     * Checks if url is own by protected service
     *
     * @param url Original URI
     * @return
     */
    private boolean isProtectedUrl(URI url) {
        LOGGER.entering(HackAndSlash.class.getName(), "isProtectedUrl", url);

        if (!url.isAbsolute()) {
            return true;
        }
        /* this came up on cs.helsinki.fi: <a href="mailto:it-web[at-remove]cs.helsinki.fi">Webmaster</a>
         * there might be more special cases and we need to take them into acount
         */
        else if (url.toString().startsWith("mailto:")) {
            return false;
        }
        /* Quick fix for <img src="file:///C:/Users/TVIKBE~1.003/AppData/Local/Temp/moz-screenshot.png" alt="" />
         * in page: http://www.cs.helsinki.fi/alumni
         */
        else if (url.toString().startsWith("file:")) {
            return false;
        }
        else if (url.toString().startsWith("news:")) {
            return false;
        }

        String hostname = url.getHost();
        LOGGER.info("Hostname: " + hostname);
        LOGGER.info("privateURI hostname: " + privateURI.getHost());

        if (hostname.equals(privateURI.getHost())) {
            LOGGER.exiting(HackAndSlash.class.getName(), "isProtectedUrl", true);
            return true;
        } else {
            LOGGER.exiting(HackAndSlash.class.getName(), "isProtectedUrl", false);
            return false;
        }
    }

    /**
     * Changes original URI in HTML tag to a masked one.
     *
     * @param tag HTML tag
     * @param attributeName HTML tags attribute
     * @return HTML tag with masked URI
     */
    public String convertUrlInTag(String tag, String attributeName) {


        // what about if quotation marks are missing

        LOGGER.entering(HackAndSlash.class.getName(), "convertUrlInTag", new Object[] {tag, attributeName});
        // what about if quotation marks missing
        // Extract given attribute and its value(s) from tag
        Pattern sourcePattern = Pattern.compile(attributeName + "(\\s)*=(\\s)*\"[^\"]*\"");
        Matcher sourceMatcher = sourcePattern.matcher(tag.toLowerCase());

        if (sourceMatcher.find()) {
            int attributeStart = sourceMatcher.start();
            int attributeEnd = sourceMatcher.end();
            String temp = tag.substring(attributeStart, attributeEnd);
            // Extract attribute value(s) including quotation marks
            Pattern urlPattern = Pattern.compile("\"[^\"]+\"");
            Matcher urlMatcher = urlPattern.matcher(temp);

            if ((urlMatcher.find())) {
                String url = urlMatcher.group();
                // Cut off quotation marks
                url = url.substring(1, url.length() - 1);
                if (!url.equals("")) {
                    String newUrl = "";
                    // attribute archive may contain several values separated by spaces
                    if (attributeName.equals("archive")) {
                        String[] urls = url.split(" ");
                        for (String s : urls) {
                            newUrl += getMaskedUrl(s) + " ";
                        }
                        newUrl = newUrl.trim();
                    } else {
                        newUrl = getMaskedUrl(url);
                    }
                    
                    String convertedTag = tag.substring(0, attributeStart + urlMatcher.start() + 1)
                            + newUrl + tag.substring(attributeEnd - 1);
                    LOGGER.exiting(HackAndSlash.class.getName(), "convertUrlInTag", convertedTag);
                    
                    return convertedTag;
                }
            }
        }
        LOGGER.exiting(HackAndSlash.class.getName(), "convertUrlInTag", tag);
        return tag;
    }

    /**
     * Updates Content-Length header with a new value after masking the urls.
     * 
     * See: https://en.wikipedia.org/wiki/Chunked_transfer_encoding
     * @param response Response with modified Content-Length value or unmodified
     *        response in case chunked encoding is used
     *
     */
    private EPICTextResponse updateContentLength(EPICTextResponse response) {

        //if the http server uses chunked encoding
        if (!response.getHeaders().containsKey("Transfer-Encoding")) {
            /* Update Content-Lenght with new size */
            HashMap<String, String> headers = new HashMap<String, String>(response.getHeaders());
            headers.put("Content-Length", Integer.toString(response.getBody().getBytes().length));
            response.setHeaders(headers);
        }

        return response;
    }
}
