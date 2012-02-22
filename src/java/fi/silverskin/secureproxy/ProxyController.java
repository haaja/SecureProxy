package fi.silverskin.secureproxy;

import fi.silverskin.secureproxy.hackandslash.HackAndSlash;
import fi.silverskin.secureproxy.resourcefetcher.ResourceFetcher;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProxyController {
    private ResourceFetcher fetcher;
    private HackAndSlash hackAndSlash;
    private static final Logger LOGGER = Logger.getLogger(ProxyController.class.getName(), null);
 
    public ProxyController() {
        fetcher = new ResourceFetcher();
        hackAndSlash = new HackAndSlash();
    }

    /**
     * Control logic for handling requests within SecureProxy.
     * 
     * @param request HTTP request.
     * @return Modified HTTP response.
     */
    public EPICResponse handleRequest(EPICRequest request) {
        request = hackAndSlash.hackAndSlashIn(request);
        request = HostMutilator.mutilateRequest(request);
       
        EPICResponse response = fetcher.handleRequest(request);
        LOGGER.log(Level.INFO, response.toString());

        return response;
    }
}
