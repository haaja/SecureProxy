package fi.silverskin.secureproxy.hackandslash;

import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICTextResponse;
import fi.silverskin.secureproxy.SecureProxyUtilities;
import java.net.URI;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HackAndSlash {

    private final String[][] tagsAndAttributes = {
        {"a", "href"}, {"applet", "codebase", "archive"}, {"area", "href"},
        {"audio", "src"},
        {"base", "href"}, {"blockquate", "cite"}, {"body", "background"},
        {"button", "formaction"}, {"command", "icon"},
        {"del", "cite"}, {"embed", "src"}, {"form", "action"}, {"frame", "src", "longdesc"},
        {"head", "profile"}, {"html", "manifest"}, {"iframe", "src", "longdesc"},
        {"img", "src", "longdesc", "usemap"}, {"input", "src", "formaction"}, {"ins", "cite"},
        {"link", "href"}, {"meta", "scheme"},
        {"object", "data", "classid", "archive", "codebase", "usemap"},
        {"q", "cite"}, {"script", "src"}, {"source", "src"}, {"track", "src"},
        {"video", "poster", "src"}
    };
    private static final Logger LOGGER = Logger.getLogger(HackAndSlash.class.getName(), null);
    private URI privateURI;
    private String privateHttpPort;
    private String privateHttpsPort;
    private URI publicURI;
    private String publicHttpPort;
    private String publicHttpsPort;

    public HackAndSlash(HackAndSlashConfig conf) {
        privateURI = conf.getPrivateURI();
        privateHttpPort = conf.getPrivateHttpPort();
        privateHttpsPort = conf.getPrivateHttpsPort();
        publicURI = conf.getPublicURI();
        publicHttpPort = conf.getPublicHttpPort();
        publicHttpsPort = conf.getPublicHttpsPort();
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

        URI uri = SecureProxyUtilities.makeUriFromString(request.getUri().toString());
        modifiedUri = privateURI + ":" + privateHttpPort + uri.getPath();

        if (uri.getQuery() != null) {
            modifiedUri = modifiedUri + "?" + uri.getQuery();
        }
        if (uri.getFragment() != null) {
            modifiedUri = modifiedUri + "#" + uri.getFragment();
        }

        request.setUri(modifiedUri);
        LOGGER.info("HackAndSlashIn modified URI: " + request.getUri().toString());

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
        oldResponse = convertCss(oldResponse);
        oldResponse = convertTags(oldResponse);
        response.setBody(oldResponse);
        response = updateContentLength(response);
        LOGGER.exiting(HackAndSlash.class.getName(), "hackAndSlashOut", response);
        return response;
    }
    
    /**
     * Changes URIs in HTML tags
     * 
     * @param oldResponse HTML page
     * @return HTML page with masked URIs
     */
    public String convertTags(String oldResponse){
        String newResponse = "";
        for (int i = 0; i < tagsAndAttributes.length; i++) {
            newResponse = "";
            // Find all strings beginning with '<', containing a tag keyword and ending with '>'
            Pattern tagPattern = Pattern.compile("<(\\s)*" + tagsAndAttributes[i][0] + "[^>]*>");
            Matcher tagMatcher = tagPattern.matcher(oldResponse);
            int index = 0;
            while (tagMatcher.find()) {
                if (tagMatcher.start() > index) {
                    newResponse += oldResponse.substring(index, tagMatcher.start());
                }
                index = tagMatcher.end();
                String tag = tagMatcher.group();
                // Find attributes, that may contain a URL, and convert URLs if needed
                for (int j = 1; j < tagsAndAttributes[i].length; j++) {
                    tag = convertUrlInTag(tag, tagsAndAttributes[i][j]);
                }
                newResponse += tag;
            }
            if (index == 0) {
                newResponse = oldResponse;
            } else if (index < oldResponse.length()) {
                newResponse += oldResponse.substring(index);
            }
            oldResponse = newResponse;
        }
        return newResponse;
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
        LOGGER.entering(HackAndSlash.class.getName(), "getMaskedUrl", url);
        LOGGER.info("getMaskedUrl's param: " + url);

        /* fixing common mistakes in page sources */
        url = url.trim().replace(" ", "%20");
        String maskedUri;

        if (hasInvalidProtocol(url)) {
            return url;
        }

        URI parsedUri = SecureProxyUtilities.makeUriFromString(url);
        if (!SecureProxyUtilities.isProtectedUrl(privateURI, parsedUri)) {
            return url;
        }

        if (parsedUri.isAbsolute()) {

            String port = parsedUri.getScheme().equals("http") ?
                          publicHttpPort : publicHttpsPort;
            LOGGER.info("PORT: "+ port);
            
            maskedUri = parsedUri.getScheme() + "://" +
                        publicURI.getHost() + ":" + port +
                        parsedUri.getPath();

            //if url has query part
            if (parsedUri.getQuery() != null) {
                maskedUri = maskedUri + "?" + parsedUri.getQuery();
            }
            //if url is using fragment (#service)
            if (parsedUri.getFragment() != null) {
                maskedUri = maskedUri + "#" + parsedUri.getFragment();
            }
        } else {
            maskedUri = url;
        }

        LOGGER.log(Level.INFO, "Returning masked url: {0}", maskedUri);
        LOGGER.exiting(HackAndSlash.class.getName(), "getMaskedUrl", maskedUri);

        return maskedUri;
    }

    private boolean hasInvalidProtocol(String url) {
        LOGGER.entering(HackAndSlash.class.getName(), "hasInvalidProtocol", url);
        boolean retVal = false;

        if (url.startsWith("mailto:")) {
            retVal = true;
        }
        /*
         * this came up on cs.helsinki.fi: <a
         * href="mailto:it-web[at-remove]cs.helsinki.fi">Webmaster</a> there
         * might be more special cases and we need to take them into acount
         */ else if (url.startsWith("mailto:")) {
            retVal = true;
        }
        /*
         * Quick fix for <img
         * src="file:///C:/Users/TVIKBE~1.003/AppData/Local/Temp/moz-screenshot.png"
         * alt="" /> in page: http://www.cs.helsinki.fi/alumni
         */ else if (url.startsWith("file:")) {
            retVal = true;
        } else if (url.startsWith("news:")) {
            retVal = true;
        } else if (url.startsWith("#")) {
            retVal = true;
        } else if (url.startsWith("\"")) {
            retVal = true;
        } else if (url.startsWith("javascript")) {
            retVal = true;
        }

        LOGGER.exiting(HackAndSlash.class.getName(), "hasInvalidProtocol", retVal);
        return retVal;
    }

    /**
     * Changes URIs in CSS code
     *
     * @param oldResponse HTML page or CSS style sheet
     * @return HTML page or CSS style sheet with masked URIs
     */
    public String convertCss(String oldResponse) {
        LOGGER.entering(HackAndSlash.class.getName(), "convertCss", oldResponse);

        Pattern cssPattern = Pattern.compile("url\\([^\\)]*\\)");
        Matcher cssMatcher = cssPattern.matcher(oldResponse.toLowerCase());
        String newResponse = "";
        int index = 0;
        while (cssMatcher.find()) {
            if (cssMatcher.start() > index) {
                newResponse += oldResponse.substring(index, cssMatcher.start());
            }
            index = cssMatcher.end();
            String cssUrl = cssMatcher.group();
            // Convert URL if needed
            cssUrl = convertUrlInCss(cssUrl);
            newResponse += cssUrl;
        } 
        if (index == 0) {
            newResponse = oldResponse;
        } else if (index < oldResponse.length()) {
            newResponse += oldResponse.substring(index);
        }

        LOGGER.exiting(HackAndSlash.class.getName(), "convertCss", newResponse);
        return newResponse;
    }
    
    /**
     * Changes original URI in CSS url attribute to a masked one.
     * 
     * @param urlAttribute CSS url attribute
     * @return CSS url attribute with masked URI
     */
    public String convertUrlInCss(String urlAttribute) {
        LOGGER.entering(HackAndSlash.class.getName(), "convertUrlInCss", urlAttribute);

        Pattern sourcePattern = Pattern.compile("('[^']*')|(\"[^\"]*)\"|"
                + "(\\([^'\"\\)][^\\)]*[^'\"\\)]\\))");
        Matcher sourceMatcher = sourcePattern.matcher(urlAttribute);
        if ((sourceMatcher.find())) {
            String url = sourceMatcher.group();
            // Cut off first and last character
            url = url.substring(1, url.length() - 1);
            if (!url.equals("")) { 
                url = getMaskedUrl(url.trim());
                urlAttribute = urlAttribute.substring(0, sourceMatcher.start() + 1)+ url + urlAttribute.substring(sourceMatcher.end() - 1);
            }
        }

        LOGGER.exiting(HackAndSlash.class.getName(), "convertUrlInCss", urlAttribute);
        return urlAttribute;
    }

    /**
     * Changes original URI in HTML tag to a masked one.
     *
     * @param tag HTML tag
     * @param attributeName HTML tags attribute
     * @return HTML tag with masked URI
     */
    public String convertUrlInTag(String tag, String attributeName) {
        LOGGER.entering(HackAndSlash.class.getName(), 
                        "convertUrlInTag",
                        new Object[] {tag, attributeName});
        
        // Extract given attribute and its value(s) from tag
        Pattern sourcePattern = Pattern.compile(attributeName + "(\\s)*=((\\s)*(\"[^\"]*\")|([^\\s]*))");
        Matcher sourceMatcher = sourcePattern.matcher(tag.toLowerCase());

        if (sourceMatcher.find()) {
            int attributeStart = sourceMatcher.start();
            int attributeEnd = sourceMatcher.end();
            String temp = tag.substring(attributeStart, attributeEnd);
            // Extract attribute value(s) including possible quotation marks
            Pattern urlPattern = Pattern.compile("(\"[^\"]+\")|(=(\\s)*[^\"][^\\s\"]*[^\"])");
            Matcher urlMatcher = urlPattern.matcher(temp);
            
            if ((urlMatcher.find())) { 
                String url = urlMatcher.group();
                // Cut off first and last character
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
                        newUrl = getMaskedUrl(url.trim());
                    }

                    String convertedTag = tag.substring(0, attributeStart + urlMatcher.start() + 1)
                            + newUrl + tag.substring(attributeEnd - 1);

                    LOGGER.exiting(HackAndSlash.class.getName(),
                                   "convertUrlInTag",
                                   convertedTag);
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
     *
     * @param response Response with modified Content-Length value or unmodified
     * response in case chunked encoding is used
     *
     */
    private EPICTextResponse updateContentLength(EPICTextResponse response) {
        LOGGER.entering(HackAndSlash.class.getName(), "updateContentLength", response);

        //if the http server uses chunked encoding
        if (!response.getHeaders().containsKey("Transfer-Encoding")) {
            /*
             * Update Content-Lenght with new size
             */
            HashMap<String, String> headers = new HashMap<String, String>(response.getHeaders());
            headers.put("Content-Length", Integer.toString(response.getBody().getBytes().length));
            response.setHeaders(headers);
        }

        LOGGER.exiting(HackAndSlash.class.getName(), "updateContentLength", response);
        return response;
    }
}
