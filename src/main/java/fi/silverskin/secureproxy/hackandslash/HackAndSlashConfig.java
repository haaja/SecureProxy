
package fi.silverskin.secureproxy.hackandslash;

import java.net.URI;
import java.net.URISyntaxException;


public class HackAndSlashConfig {
    private String privateURI;
    private String privatePort;
    private URI publicURI;

    public HackAndSlashConfig(String privateURI, String privatePort, String publicURI) throws URISyntaxException {
        this.privateURI = privateURI;
        this.privatePort = privatePort;
        this.publicURI = new URI(publicURI);
    }
     
    public URI getpublicURI() {
        return publicURI;
    }

    public String getprivatePort() {
        return privatePort;
    }

    public String getprivateURI() {
        return privateURI;
    }
    
}
