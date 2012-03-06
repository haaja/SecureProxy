
package fi.silverskin.secureproxy.hackandslash;

import fi.silverskin.secureproxy.ProxyConfigurer;
import java.net.URI;
import java.net.URISyntaxException;


public class HackAndSlashConfig {
    private URI privateURI;
    private String privatePort;
    private URI publicURI;
    
    public HackAndSlashConfig () throws URISyntaxException {
        ProxyConfigurer configurer = new ProxyConfigurer();
        // Takes only one valye (first) from properties.configure
        privateURI = new URI(configurer.getConfigure("privateURI")[0]);
        privatePort = configurer.getConfigure("privatePort")[0];
        publicURI = new URI(configurer.getConfigure("publicURI")[0]);
    
    }

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
