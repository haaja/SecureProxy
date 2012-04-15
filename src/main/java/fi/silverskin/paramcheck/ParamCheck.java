/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.paramcheck;

import fi.silverskin.secureproxy.plugins.*;
import fi.silverskin.secureproxy.*;
import fi.silverskin.secureproxy.EPICRequest.RequestType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author peltoel
 */
public class ParamCheck implements SecureProxyPlugin {
    
    private static final Logger LOGGER = Logger.getLogger(ParamCheck.class.getName(), null);
    
    public boolean status;
    
    public ParamCheck() {
        this.status = false;
    }
    
    /**
     * 
     * @return status of plugin
     */
    public boolean getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "ParamCheck";
    }
    
    @Override
    public void run(EPICRequest epic) {
        LOGGER.entering(ParamCheck.class.getName(), "run", epic);
        
        if (epic.getType() == RequestType.GET) 
            this.status = handleGet(epic);
        
        if (epic.getType() == RequestType.POST)
            this.status = handlePost(epic);
        
        LOGGER.exiting(ParamCheck.class.getName(), "run");
    }
    
    @Override
    public void run(EPICTextResponse epic) {
    }
    
    @Override
    public void run(EPICBinaryResponse epic) {
    }

    /**
     * Handles params of HTTP GET.
     * 
     * @param epic request
     */
    private boolean handleGet(EPICRequest epic) {
        LOGGER.entering(ParamCheck.class.getName(), "handleGet", epic);
        
        URI uri = epic.getUri();
        
        String query = uri.getQuery();
        if (query.isEmpty())
            return false;
        
        if (!isValidQuery(query)) {
            LOGGER.exiting(ParamCheck.class.getName(), "handleGet", false);
            return false;
        }
        
        // everything went well
        LOGGER.exiting(ParamCheck.class.getName(), "handleGet", true);
        return true;
    }

     /**
     * Handles params of HTTP POST.
     * 
     * @param epic request
     */
    private boolean handlePost(EPICRequest epic) {
        LOGGER.entering(ParamCheck.class.getName(), "handlePost", epic);

        String query = epic.getBody();
        if (query.isEmpty())
            return false;
        
        if (!isValidQuery(query)) {
            LOGGER.exiting(ParamCheck.class.getName(), "handlePost", false);
            return false;
        }
        
        LOGGER.exiting(ParamCheck.class.getName(), "handlePost", true);
        return true;
    }
    
    /**
     * Parsers the query and calls validate checking isValidParam
     * 
     * @param query
     * @return boolean
     */
    private boolean isValidQuery(String query) {
    
        // separate parameters to "name=value" pars
        String[] parsed = query.split("&");
        
        // read pars and send values for checking
        for (int i = 0; i < parsed.length; i++) {
            String[] tmp = parsed[i].split("=");
            String param = tmp[1];
            boolean isValid = isValidParam(param);
            if (isValid == false) {
                // something was badly
                LOGGER.exiting(ParamCheck.class.getName(), "parseQuery", false);
                return false;
            }
        }
        
        LOGGER.exiting(ParamCheck.class.getName(), "parseQuery", true);
        return true;
    }
   
    /**
     * Checks "bad charecters".
     * 
     * @param param
     * @return boolean
     */
    private boolean isValidParam(String param) {
        LOGGER.entering(ParamCheck.class.getName(), "isValidParam", param);
        
        // regex: bad charecters are >, <, " ja '
        Pattern pattern = Pattern.compile("<|>|\"|\'");
        Matcher matcher = pattern.matcher(param);
        
        if (matcher.find()) {
            // some bad character
            LOGGER.exiting(ParamCheck.class.getName(), "isValidParam", false);
            return false;
        }

        // everything went well
        LOGGER.exiting(ParamCheck.class.getName(), "isValidParam", true);
        return true;
    }

}
