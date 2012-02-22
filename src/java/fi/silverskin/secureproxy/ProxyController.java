package fi.silverskin.secureproxy;

import fi.silverskin.secureproxy.hackandslash.HackAndSlash;
import fi.silverskin.secureproxy.resourcefetcher.ResourceFetcher;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProxyController {
    private ResourceFetcher fetcher;
    private HackAndSlash hackAndSlash;
    
    public ProxyController() {
        fetcher = new ResourceFetcher();
        hackAndSlash = new HackAndSlash();
    }
    
    public EPICResponse handleRequest(EPICRequest request) {
        request = hackAndSlash.hackAndSlashIn(request);

        
        EPICResponse response = fetcher.handleRequest(request);
        Logger.getLogger(ProxyController.class.getName()).log(Level.INFO, response.toString());
        return response;
    }
}
