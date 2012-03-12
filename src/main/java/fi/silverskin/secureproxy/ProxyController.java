package fi.silverskin.secureproxy;

import fi.silverskin.secureproxy.hackandslash.HackAndSlash;
import fi.silverskin.secureproxy.hackandslash.HackAndSlashConfig;
import fi.silverskin.secureproxy.resourcefetcher.ResourceFetcher;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProxyController {
    private ResourceFetcher fetcher;
    private HackAndSlash hackAndSlash;
    private static final Logger LOGGER = Logger.getLogger(ProxyController.class.getName(), null);
 
    public ProxyController() throws URISyntaxException {
        fetcher = new ResourceFetcher();
        HackAndSlashConfig conf = new HackAndSlashConfig();
        hackAndSlash = new HackAndSlash(conf);
    }

    /**
     * Control logic for handling requests within SecureProxy.
     * 
     * @param request HTTP request.
     * @return Modified HTTP response.
     */
    public EPICResponse handleRequest(EPICRequest request) {
        LOGGER.entering(ProxyController.class.getName(), "handleRequest", request);
        request = hackAndSlash.hackAndSlashIn(request);
        request = HeaderCleaner.cleanHeaders(request);
       
        EPICResponse response = fetcher.handleRequest(request);
        LOGGER.log(Level.INFO, response.toString());

        if (response.isText()) {
            LOGGER.info("Sending response to HackAndSlash");
            response = hackAndSlash.hackAndSlashOut((EPICTextResponse)response);
            LOGGER.info("Mutilated response:");
            LOGGER.log(Level.INFO, response.toString());
        }

        LOGGER.exiting(ProxyController.class.getName(), "handleRequest", response);
        
        return response;
    }
}
