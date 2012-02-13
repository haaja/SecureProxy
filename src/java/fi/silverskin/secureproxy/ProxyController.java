package fi.silverskin.secureproxy;

import fi.silverskin.secureproxy.resourcefetcher.ResourceFetcher;


public class ProxyController {
    private ResourceFetcher fetcher;
    
    public ProxyController() {
        fetcher = new ResourceFetcher();
    }
    
    public EPICResponse handleRequest(EPICRequest request) {
        return fetcher.handleRequest(request);
    }
}
