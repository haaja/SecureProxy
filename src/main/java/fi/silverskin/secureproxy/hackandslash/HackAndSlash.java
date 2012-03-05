package fi.silverskin.secureproxy.hackandslash;

import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICResponse;
import fi.silverskin.secureproxy.EPICTextResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HackAndSlash {

    private final String[][] tagsAndAttributes = {
        {"a", "href"}, {"applet", "codebase", "archive"}, {"area", "href"},
        {"base", "href"}, {"blockquate", "cite"}, {"body", "background"},
        {"del", "cite"}, {"form", "action"}, {"frame", "src", "longdesc"},
        {"head", "profile"}, {"iframe", "src", "longdesc"},
        {"img", "src", "longdesc", "usemap"}, {"input", "src"}, {"ins", "cite"},
        {"link", "href"},
        {"object", "data", "classid", "archive", "codebase", "usemap"},
        {"q", "cite"}, {"script", "src"}
    };
    private static final Logger LOGGER = Logger.getLogger(HackAndSlash.class.getName(), null);
    //TODO: To be replaced with proper settings
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
        try {
            URI uri = new URI(request.getUri());
            request.setUri(privateURI + ":" + privatePort + uri.getPath());
            LOGGER.log(Level.INFO, request.getUri().toString());

        } catch (URISyntaxException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return request;
    }

    /**
     * Modifies HTTP response to hide traces of the real service.
     *
     * @param response HTTP response with real data.
     * @return Modified HTTP response.
     */
    public EPICTextResponse hackAndSlashOut(EPICTextResponse response) {

        String oldResponse = response.getBody(), newResponse = "";
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
        response.setBody(newResponse);

        /*
         * Update Content-Lenght with new size
         */
        HashMap<String, String> headers = new HashMap<String, String>(response.getHeaders());
        headers.put("Content-Length", Integer.toString(response.getBody().getBytes().length));
        response.setHeaders(headers);

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

        System.out.println("getMaskedUrl's param: " + url);
        URI parsedUri = null;
        String maskedUri = null;

        try {
            parsedUri = new URI(url);
        } catch (URISyntaxException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        if (!isProtectedUrl(parsedUri)) {
            System.out.println("Wasn't protected.");
            return url;
        }

        if (parsedUri.isAbsolute()) {
            maskedUri = publicURI + parsedUri.getPath();
        } else {
            maskedUri = publicURI + "/" + url;
        }

        LOGGER.log(Level.INFO, "Returning masked url: {0}", maskedUri);
        System.out.println("ready Masked url: " + maskedUri);
        return maskedUri;
    }

    /**
     * Checks if url is own by protected service
     *
     * @param url Original URI
     * @return
     */
    private boolean isProtectedUrl(URI url) {
        String hostname = url.getHost();
        System.out.println("Hostname: " + hostname);
        System.out.println("privateURI hostname: " + privateURI.getHost());

        if (hostname.equals(privateURI.getHost())) {
            return true;
        } else {
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
                    return tag.substring(0, attributeStart + urlMatcher.start() + 1)
                            + newUrl + tag.substring(attributeEnd - 1);
                }
            }
        }
        return tag;
    }
}
