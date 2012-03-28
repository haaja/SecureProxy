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

/**
 *
 * @author peltoel
 */
public class ParamCheck implements SecureProxyPlugin {
    
    private static final Logger LOGGER = Logger.getLogger(ParamCheck.class.getName(), null);
    
    @Override
    public String getName() {
        return "ParamCheck";
    }
    
    @Override
    public void run(EPICRequest epic) {
        LOGGER.entering(ParamCheck.class.getName(), "run", epic);
        
        if (epic.getType() == RequestType.GET) 
            handleGet(epic);
        
        if (epic.getType() == RequestType.POST)
            handlePost(epic);
        
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
        
        URI uri = null;
        try {
            uri = new URI(epic.getUri());
        } catch (URISyntaxException ex) {
            LOGGER.log(Level.SEVERE, "Received URISyntaxException with: " + epic.getUri(), ex);
        }
        
        String query = uri.getQuery();
        if (query.isEmpty())
            return false;
        
        // separate parameters to "name=value" pars
        String[] parsed = query.split("&");
        checkParams(parsed);
        
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

        String body = epic.getBody();
        if (body.isEmpty())
            return false;
        
        // bodyn parserointi
        String[] parsed = null;
        checkParams(parsed);
        
        LOGGER.exiting(ParamCheck.class.getName(), "handlePost", true);
        return true;
    }
   
    /**
     * Checks "bad charecters".
     * 
     * @param query 
     */
    private void checkParams(String[] query) {
        LOGGER.entering(ParamCheck.class.getName(), "parseQuery", query);
        
        for (int i = 0; i < query.length; i++) {
            String[] tmp = query[i].split("=");
            String value = tmp[1];
            // regex tähän, ettei value sisällä pahoja parametreja
        }

        LOGGER.exiting(ParamCheck.class.getName(), "parseQuery");
    }

}
