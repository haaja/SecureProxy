/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.paramcheck;

import fi.silverskin.secureproxy.EPICBinaryResponse;
import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICRequest.RequestType;
import fi.silverskin.secureproxy.EPICTextResponse;
import fi.silverskin.secureproxy.plugins.SecureProxyPlugin;
import java.net.URI;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author peltoel
 */
public class ParamCheck implements SecureProxyPlugin {
    
    private static final Logger LOGGER = Logger.getLogger(ParamCheck.class.getName(), null);
    
    // TODO: instead of boolean status, throw a EPICException
    public boolean status;
    
    // the rule of valid parameter query, readed from config file
    public String rule; 
    
    public ParamCheck() {
        this.status = false;
        ParamCheckConfig config = new ParamCheckConfig();
        rule = config.getRule();
    }
    
    public ParamCheck(String rule) {
        this.status = false;
        this.rule = rule;
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
        LOGGER.entering(ParamCheck.class.getName(), "isValidQuery", query);

        Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(query);
    
        if (matcher.matches()) {
            LOGGER.exiting(ParamCheck.class.getName(), "isValidQuery", true);
            return true;
        }
        
        LOGGER.exiting(ParamCheck.class.getName(), "isValidQuery", false);
        return false;
    }

}