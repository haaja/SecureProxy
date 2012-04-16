package fi.silverskin.secureproxy;

import fi.silverskin.secureproxy.hackandslash.HackAndSlash;
import fi.silverskin.secureproxy.hackandslash.HackAndSlashConfig;
import fi.silverskin.secureproxy.plugins.PluginRunner;
import fi.silverskin.secureproxy.resourcefetcher.ResourceFetcher;
import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProxyController {
    private ResourceFetcher fetcher;
    private HackAndSlash hackAndSlash;
    private PluginRunner pluginRunner;
    private static final Logger LOGGER = Logger.getLogger(ProxyController.class.getName(), null);
    private ProxyConfigurer configurer;
    private Properties configuration;
 
    public ProxyController() {
        fetcher = new ResourceFetcher();
        configurer = new ProxyConfigurer();
        configuration = configurer.getConfigurationProperties();
        HackAndSlashConfig conf = new HackAndSlashConfig(configuration);
        hackAndSlash = new HackAndSlash(conf);
        
        File confFile = configurer.getConfigFile();
        pluginRunner = new PluginRunner(confFile);
        
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
        request = HeaderCleaner.cleanHeaders(request, configuration);
       
        EPICResponse response = fetcher.handleRequest(request);
        LOGGER.log(Level.INFO, response.toString());

        if (response.isText()) {
            LOGGER.info("Sending response to HackAndSlash");
            response = hackAndSlash.hackAndSlashOut((EPICTextResponse)response);
            LOGGER.info("Mutilated response:");
            LOGGER.log(Level.INFO, response.toString());
        }

        /* Change location header in case it exists */
        response = HeaderCleaner.maskLocationHeader(response, configuration);

        LOGGER.exiting(ProxyController.class.getName(), "handleRequest", response);
        return response;
    }
}
