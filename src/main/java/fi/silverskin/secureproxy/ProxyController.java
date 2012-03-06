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
    private ProxyConfigurer configurer;
    private static final Logger LOGGER = Logger.getLogger(ProxyController.class.getName(), null);
 
    public ProxyController() throws URISyntaxException {
        fetcher = new ResourceFetcher();
        configurer = new ProxyConfigurer();
        
        // Takes only one valye (first) from properties.configure
        String privateURI = configurer.getConfigure("privateURI")[0];
        String privatePort = configurer.getConfigure("privatePort")[0];
        String publicURI = configurer.getConfigure("publicURI")[0];

        HackAndSlashConfig conf = new HackAndSlashConfig(privateURI, privatePort, publicURI);
        hackAndSlash = new HackAndSlash(conf);
    }

    /**
     * Control logic for handling requests within SecureProxy.
     * 
     * @param request HTTP request.
     * @return Modified HTTP response.
     */
    public EPICResponse handleRequest(EPICRequest request) {
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

        return response;
    }
}
