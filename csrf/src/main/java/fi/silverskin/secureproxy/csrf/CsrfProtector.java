package fi.silverskin.secureproxy.csrf;

import fi.silverskin.secureproxy.EPICRequest.RequestType;
import fi.silverskin.secureproxy.*;
import fi.silverskin.secureproxy.plugins.SecureProxyPlugin;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsrfProtector implements SecureProxyPlugin {

    private static final String PLUGINNAME = "CsrfProtector";
    private static final Logger LOGGER = 
            Logger.getLogger(CsrfProtector.class.getName(), null);
    private static final String CSRFFIELD = "csrfKey";

    public String getName() {
        return PLUGINNAME;
    }

    public void run(EPICRequest request) {
        LOGGER.entering(CsrfProtector.class.getName(), "run", request);
        
        if (request.getType() == RequestType.POST) {
            if (!validateReferer(request)) {
                //Referer header invalid! Throw exception
                LOGGER.info("Referer header valid!");
            } else {
                LOGGER.info("Referer header invalid!");
            }
                
            if (!validateCsrfKey(request)) {
                LOGGER.info("Csrfkey invalid!");
                //CSRF key invalid! Throw exception
            } else {
                LOGGER.info("Csrfkey valid!");
            }
        }
        
        LOGGER.exiting(CsrfProtector.class.getName(), "run");
    }

    public void run(EPICTextResponse response) {
        LOGGER.entering(CsrfProtector.class.getName(), "run", response);
        String csrfKey = generateCsrfKey();
        injectCsrfKeyField(response, csrfKey);
        LOGGER.exiting(CsrfProtector.class.getName(), "run");
    }

    //no need to modify binary responses
    public void run(EPICBinaryResponse response) {
        LOGGER.log(Level.WARNING, 
                   "CsrfProtector.run() was called with EPICBinaryResponse.");
    }

    /**
     * Validates request header
     * 
     * POST requests should always have our public url as a referer value
     * 
     * @param request HTTP request
     * @return true if referer is valid, false otherwise.
     */
    public boolean validateReferer(EPICRequest request) {
        LOGGER.entering(CsrfProtector.class.getName(), 
                        "validateReferer", 
                        request);
        
        Map<String, String> originalHeaders = request.getHeaders();
        String referer = originalHeaders.get("referer");
        URI refererUri = SecureProxyUtilities.makeUriFromString(referer);
        boolean retVal;

        ProxyConfigurer conf = new ProxyConfigurer();
        Properties configuration = conf.getConfigurationProperties();
        String protectedUrl = configuration.getProperty("publicURI");
        URI protectedUri = SecureProxyUtilities.makeUriFromString(protectedUrl);

        if (SecureProxyUtilities.isProtectedUrl(protectedUri, refererUri)) {
            retVal = true;
        } else {
            retVal = false;
        }
        
        LOGGER.entering(CsrfProtector.class.getName(), 
                        "validateReferer", 
                        retVal);
        return retVal;
    }

    /**
     * Validates CSRF key value
     * 
     * @param request Incoming HTTP request
     * @return true if CSRF key is valid, false otherwise.
     */
    public boolean validateCsrfKey(EPICRequest request) {
        LOGGER.entering(CsrfProtector.class.getName(), 
                        "validateCsrfKey", 
                        request);
        
        String requestBody = request.getBody();
        String[] postParameters = requestBody.split("&");
        String csrfKey = getCsrfKeyFromHeaders(request);
        boolean retVal = false;
        
        for(String parameter: postParameters) {
            String[] valuePair = parameter.split("=");
            valuePair[0] = valuePair[0].trim();
            
            if (valuePair[0].equals(CSRFFIELD)) {
                String paramCsrfKey = valuePair[1].trim();
                if (csrfKey.equals(paramCsrfKey)) {
                    retVal = true;
                } else {
                    retVal = false;
                }
            }
        }
        
        LOGGER.exiting(CsrfProtector.class.getName(), "validateCsrfKey", retVal);
        return retVal;
    }
    
    /**
     * Parses CSRF key from request headers
     * 
     * @param request HTTP request
     * @return Parsed CSRF key
     */
    public String getCsrfKeyFromHeaders(EPICRequest request) {
        LOGGER.entering(CsrfProtector.class.getName(), 
                        "getCsrfKeyFromHeaders", 
                        request);
        
        String csrfKey = "";
        HashMap<String, String> headers = 
                new HashMap<String, String>(request.getHeaders());
        
        String cookieHeader = headers.get("cookie");
        String[] cookies = cookieHeader.split(";");
        for (String cookie : cookies) {
            String[] valuePair = cookie.split("=");
            valuePair[0] = valuePair[0].trim();
            
            if (valuePair[0].equals(CSRFFIELD)) {
                csrfKey = valuePair[1].trim();
                break;
            }
        }
        
        LOGGER.exiting(CsrfProtector.class.getName(), 
                       "getCsrfKeyFromHeaders", 
                       csrfKey);
        return csrfKey;
    }
    
    /**
     * Generates random CSRF key
     * 
     * @return CSRF key
     */
    private String generateCsrfKey() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Injects hidden input field to containing csrfKey to all forms.
     * 
     * NOTE:
     * This is a lazy implementation which uses the same csrfKey for all the
     * forms in HTTP response.
     * 
     * 
     * @param response HTTP response
     * @param csrfKey CSRF key to be injected into all forms
     */
    public void injectCsrfKeyField(EPICTextResponse response, String csrfKey) {
        LOGGER.entering(CsrfProtector.class.getName(), 
                        "injectCsrfKeyField", 
                        response);
                
        String csrfField = 
                "<input type=\"hidden\" name=\"csrfKey\" value=\""+ csrfKey +"\">";
        String responseBody = response.getBody();
        StringBuilder injectedBody = new StringBuilder();
        boolean isInjected = false;
        
        //match <form tags
        Pattern formPattern = Pattern.compile("<[\\s]*form[^>]*>");
        Matcher formMatcher = formPattern.matcher(responseBody);
        int index = 0;
        
        while (formMatcher.find()) {
            if (formMatcher.start() > index) {
                injectedBody.append(responseBody.substring(index, formMatcher.start()));
            }

            String form = formMatcher.group();
            form += csrfField;
            injectedBody.append(form);
            index = formMatcher.end();
            isInjected = true;
        }
        
        //update cookie header only if new csrfKey was injected
        if (isInjected) {
            injectedBody.append(responseBody.substring(index));
            updateCookieWithCsrfKey(response, csrfKey);
            response.setBody(injectedBody.toString());
        }
        
        formMatcher.reset();
        LOGGER.exiting(CsrfProtector.class.getName(), "injectCsrfKeyField");
    }
    
    /**
     * Updates the cookie with the injected CSRF key or inserts new one if no
     * previous key exists.
     * 
     * @param response HTTP response
     * @param csrfKey generated CSRF key to be inserted into cookie header
     */
    public void updateCookieWithCsrfKey(EPICTextResponse response, String csrfKey) {
        LOGGER.entering(CsrfProtector.class.getName(), 
                        "updateCookieWithCsrfKey", 
                        new Object[] { response, csrfKey });
        
        HashMap<String, String> headers = 
                new HashMap<String, String>(response.getHeaders());
        String newCookie = "";
        String cookie = headers.get("cookie");
        if (cookie == null) {
            newCookie = CSRFFIELD + "=" + csrfKey;
        } else {
            String[] cookieParams = cookie.split(";");
            
            for (String c : cookieParams) {
                String[] valuePair = c.split("=");
                
                if (valuePair.length > 1) {
                    if (valuePair[0].trim().equals(CSRFFIELD)) {
                        newCookie += valuePair[0] + "=" + csrfKey + "; ";
                    } else {
                        newCookie += valuePair[0].trim() + "=" 
                                     + valuePair[1].trim() + "; ";
                    }
                }
            }
        }
        
        headers.put("Set-Cookie", newCookie);
        
        response.setHeaders(headers);
        LOGGER.exiting(CsrfProtector.class.getName(), "updateCookieWithCsrfKey");
    }
}
