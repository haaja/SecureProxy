
package fi.silverskin.secureproxy.hackandslash;

import java.net.URI;
import java.net.URISyntaxException;


public class HackAndSlashConfig {
    private URI privateURI;
    private String privatePort;
    private URI publicURI;

    public HackAndSlashConfig(String privateURI, String privatePort, String publicURI) throws URISyntaxException {
        this.privateURI = new URI(privateURI);
        this.privatePort = privatePort;
        this.publicURI = new URI(publicURI);
    }
     
    public URI getpublicURI() {
        return publicURI;
    }

    public String getprivatePort() {
        return privatePort;
    }

    public URI getprivateURI() {
        return privateURI;
    }
    
}
