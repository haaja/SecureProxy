package fi.silverskin.secureproxy.hackandslash;

import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICTextResponse;
import fi.silverskin.secureproxy.SecureProxyUtilities;
import fi.silverskin.secureproxy.redis.LinkDB;
import java.net.URI;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for hiding the resources of the protected server.
 * It contains methods for converting url paths to random strings in responses
 * and for restoring them in requests, for converting cookie values, that might
 * contain unsafe information, to random strings, and for restoring them to 
 * original values. All absolute paths in the document refering to the server and
 * all paths in attributes of tags in the document refering to the server are hided.
 * 
 * @author Hannu Kärnä
 */
public class HackAndSlash {

    private final LinkDB db = new LinkDB();
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

    /**
     * The constructor.
     * 
     * @param conf configuration file
     */
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
        URI uri = request.getUri();
        String modifiedUri;
        String path;

        if (uri.getScheme() != null) {
            String port = uri.getScheme().equals("http") ? privateHttpPort : privateHttpsPort;
            LOGGER.info("hackAndSlashIn port: " + port);
            modifiedUri = uri.getScheme() + "://"
                    + privateURI.getHost()
                    + ":" + port;
            String[] temp = uri.getPath().split("/");
            if(temp.length == 0) path = "";
            else path = db.fetchKey(temp[temp.length-1]);
            if(path == null || path.length() == 0) {
                path = uri.getPath();
            }
        } else {
            LOGGER.info("hackAndSlashIn got relative url as param. Assuming "
                    + "HTTP protocol");
            modifiedUri = "http://"
                    + privateURI.getHost()
                    + ":" + privateHttpPort;
            path = db.fetchKey(uri.getPath().substring(1));
            if(path == null || path.length() == 0) {
                path = uri.getPath();
            }
        }

        LOGGER.info("PATH: "+ path);
        if (path.startsWith("/")) {
            modifiedUri += path;
        } else {
            modifiedUri += "/" + path;
        }
        if (uri.getQuery() != null) {
            modifiedUri = modifiedUri + "?" + uri.getQuery();
        }
        if (uri.getFragment() != null) {
            modifiedUri = modifiedUri + "#" + uri.getFragment();
        }
        request.setUri(modifiedUri);
        HashMap<String, String> headers =
                new HashMap<String, String>(request.getHeaders());
        //mutilateCookiesIn(headers);
        request.setHeaders(headers);
        LOGGER.info("HackAndSlashIn modified URI: " + request.getUri().toString());
        LOGGER.exiting(HackAndSlash.class.getName(), "hackAndSlashIn", request);
        return request;
    }

    /**
     * Restore mutilated cookies back to original cookies
     * 
     * @param headers incoming headers
     */
    public void mutilateCookiesIn(HashMap<String, String> headers) {
        String cookieTag = headers.get("cookie");
        LOGGER.info("testing in1:" + cookieTag);
        if (cookieTag == null) {
            return;
        }
        String[] cookies = cookieTag.split(";");
        cookieTag = "";
        for (String cookie : cookies) {
            String[] cookieParts = cookie.split("=");
            cookieTag += cookieParts[0] + "=";
            if (cookieParts.length > 1) {
                cookieParts[1] = cookieParts[1].trim();
                String cookieValue = db.fetchKey(cookieParts[1]);
                if (cookieValue.length() > 0) {
                    cookieParts[1] = cookieValue;
                }
                cookieTag += cookieParts[1];
            }
            cookieTag += "; ";
            LOGGER.info("testing in2:" + cookieTag);
        }
        cookieTag = cookieTag.substring(0, cookieTag.length() - 2);
        headers.put("cookie", cookieTag);
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
        oldResponse = convertAbsoluteUrlsInText(oldResponse);
        response.setBody(oldResponse);
        HashMap<String, String> headers =
                new HashMap<String, String>(response.getHeaders());
        //mutilateCookiesOut(headers);
        response.setHeaders(headers);
        LOGGER.exiting(HackAndSlash.class.getName(), "hackAndSlashOut", response);
        return response;
    }

    /**
     * Converts cookie values to random strings
     * 
     * @param headers outgoing headers
     */
    public void mutilateCookiesOut(HashMap<String, String> headers) {
        String cookieValue = "", newCookie, attributes, domain;
        String cookie = headers.get("Set-Cookie");
        if (cookie == null) {
            return;
        }
        Pattern cookiePattern = Pattern.compile("[^\\s;]*");
        Matcher cookieMatcher = cookiePattern.matcher(cookie);
        if (!(cookieMatcher.find())) {
            return;
        }
        String[] cookies = (cookieMatcher.group()).split("=");
        newCookie = cookies[0];
        if (cookies.length > 1) {
            cookieValue = UUID.randomUUID().toString();
            newCookie += "=" + cookieValue;
        }
        if (cookie.length() > cookieMatcher.end()) {
            attributes = cookie.substring(cookieMatcher.end());
            cookiePattern = Pattern.compile("domain[^\\s;]*[\\s;]?");
            cookieMatcher = cookiePattern.matcher(attributes);
            if (!(cookieMatcher.find())) {
                newCookie += attributes;
            } else {
                domain = cookieMatcher.group();
                if (cookieMatcher.start() > 0) {
                    newCookie += attributes.substring(0, cookieMatcher.start());
                }
                if (cookieMatcher.end() < attributes.length()) {
                    newCookie +=
                            attributes.substring(cookieMatcher.end());
                }
            }
        }
        headers.put("Set-Cookie", newCookie);
        db.addLink(cookies[1].trim(), cookieValue);
    }

    /**
     * Changes protected URIs outside of tags and css
     *
     * @param oldresponse HTML page
     * @return HTML page with absolute URIs in text masked
     */
    public String convertAbsoluteUrlsInText(String oldResponse) {
        LOGGER.entering(HackAndSlash.class.getName(),
                "convertAbsoluteUrlsInText", oldResponse);

        Pattern urlPattern = Pattern.compile("((" + privateURI.getScheme() + "|http|https)://)?"
                + privateURI.getHost() + "[^<\\s]*");
        Matcher urlMatcher = urlPattern.matcher(oldResponse.toLowerCase());
        String newResponse = "";
        int index = 0;
        while (urlMatcher.find()) {
            if (urlMatcher.start() > index) {
                newResponse += oldResponse.substring(index, urlMatcher.start());
            }
            index = urlMatcher.end();
            String url = urlMatcher.group();
            if (!url.contains("://")) {
                url = "http://" + url;
            }
            // Convert URL if needed
            url = getMaskedUrl(url);
            newResponse += url;
        }
        if (index == 0) {
            newResponse = oldResponse;
        } else if (index < oldResponse.length()) {
            newResponse += oldResponse.substring(index);
        }

        LOGGER.exiting(HackAndSlash.class.getName(), "convertAbsoluteUrlsInText", newResponse);
        return newResponse;
    }

    /**
     * Changes protected URIs in HTML tags
     *
     * @param oldResponse HTML page
     * @return HTML page with URIs in tags masked
     */
    public String convertTags(String oldResponse) {
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
     * @param url Original URI
     * @return Masked URI
     */
    public String getMaskedUrl(String url) {
        LOGGER.entering(HackAndSlash.class.getName(), "getMaskedUrl", url);
        LOGGER.info("getMaskedUrl's param: " + url);

        String realPath, maskedPath = "", maskedUri, port;
        //fixing common mistakes in page sources
        url = url.trim().replace(" ", "%20");
        if (hasInvalidProtocol(url)) {
            return url;
        }

        URI parsedUri = SecureProxyUtilities.makeUriFromString(url);
        if (!SecureProxyUtilities.isProtectedUrl(privateURI, parsedUri)) {
            return url;
        }

        if (parsedUri.isAbsolute()) {

            port = parsedUri.getScheme().equals("http")
                    ? publicHttpPort : publicHttpsPort;
            LOGGER.info("PORT: " + port);

            maskedUri = parsedUri.getScheme() + "://"
                    + publicURI.getHost() + ":" + port+"/";
            realPath = parsedUri.getPath();
            if (db.fetchValue(realPath) != null) {
                maskedUri += db.fetchValue(realPath);
            } else {
                    maskedPath = UUID.randomUUID().toString();
                maskedUri += maskedPath;
                db.addLink(realPath, maskedPath);
            }
            //if url has query part
            if (parsedUri.getQuery() != null) {
                maskedUri = maskedUri + "?" + parsedUri.getQuery();
            }
            //if url is using fragment (#service)
            if (parsedUri.getFragment() != null) {
                maskedUri = maskedUri + "#" + parsedUri.getFragment();
            }
        } else {
            maskedUri = "";//http://"+
                    //publicURI.getHost() + ":" + publicHttpPort+"/";
            if (db.fetchValue(url) != null) {
                maskedUri += db.fetchValue(url);
            } else {
                maskedPath = UUID.randomUUID().toString();
                maskedUri += maskedPath;
                db.addLink(url, maskedPath);
            }
        }
        LOGGER.log(Level.INFO, "Returning masked url: {0}", maskedUri);
        LOGGER.exiting(HackAndSlash.class.getName(), "getMaskedUrl", maskedUri);
        return maskedUri;
    }

    /**
     * Searches supplied parameter for common protocols and mistakes that will
     * break our beloved proxy.
     *
     * @param url String representation of the url
     * @return true in case invalidies are found, otherwise false
     */
    private boolean hasInvalidProtocol(String url) {
        LOGGER.entering(HackAndSlash.class.getName(), "hasInvalidProtocol", url);
        boolean retVal = false;

        /*
         * this came up on cs.helsinki.fi: <a
         * href="mailto:it-web[at-remove]cs.helsinki.fi">Webmaster</a> there
         * might be more special cases and we need to take them into acount
         */
        if (url.startsWith("mailto:")) {
            retVal = true;
        } 
        /*
         * Quick fix for <img
         * src="file:///C:/Users/TVIKBE~1.003/AppData/Local/Temp/moz-screenshot.png"
         * alt="" /> in page: http://www.cs.helsinki.fi/alumni
         */ 
        else if (url.startsWith("file:")) {
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
        /*
         * Thank you CS department for your awesome www pages
         * http://www.cs.helsinki.fi/story/63467/windows-phone-7-tutuksi-koodausleirill
         */ 
        else if (url.startsWith("http:///")) {
            retVal = true;
        }
        LOGGER.exiting(HackAndSlash.class.getName(), "hasInvalidProtocol", retVal);
        return retVal;
    }

    /**
     * Changes protected URIs in CSS code
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
                urlAttribute = urlAttribute.substring(0, sourceMatcher.start() + 1) + url + urlAttribute.substring(sourceMatcher.end() - 1);
            }
        }
        LOGGER.exiting(HackAndSlash.class.getName(), "convertUrlInCss", urlAttribute);
        return urlAttribute;
    }

    /**
     * Changes protected URIs in HTML tag to masked ones.
     *
     * @param tag HTML tag
     * @param attributeName HTML tags attribute
     * @return HTML tag with masked URI
     */
    public String convertUrlInTag(String tag, String attributeName) {
        LOGGER.entering(HackAndSlash.class.getName(),
                "convertUrlInTag",
                new Object[]{tag, attributeName});

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
}
