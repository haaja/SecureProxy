
package fi.silverskin.secureproxy.hackandslash;

import java.net.URI;
import java.net.URISyntaxException;


public class HackAndSlashConfig {
    private String remoteUrl;
    private String remotePort;
    private URI basePseudoURI;

    public HackAndSlashConfig(String remoteUrl, String remotePort, String basePseudoURI) throws URISyntaxException {
        this.remoteUrl = remoteUrl;
        this.remotePort = remotePort;
        this.basePseudoURI = new URI(basePseudoURI);
    }
     
    public URI getBasePseudoURI() {
        return basePseudoURI;
    }

    public String getRemotePort() {
        return remotePort;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }
    
}
