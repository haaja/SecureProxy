
package fi.silverskin.secureproxy.hackandslash;

import fi.silverskin.secureproxy.SecureProxyUtilities;
import java.net.URI;
import java.util.Properties;


public class HackAndSlashConfig {
    private URI privateURI;
    private String privatePort;
    private URI publicURI;
    
    public HackAndSlashConfig (Properties configuration) {
        privateURI = SecureProxyUtilities.makeUriFromString(configuration.getProperty("privateURI"));
        privatePort = configuration.getProperty("privatePort");
        publicURI = SecureProxyUtilities.makeUriFromString(configuration.getProperty("publicURI"));
    }

    public HackAndSlashConfig(String privateURI, 
                              String privatePort,
                              String publicURI) {
        this.privateURI = SecureProxyUtilities.makeUriFromString(privateURI);
        this.privatePort = privatePort;
        this.publicURI = SecureProxyUtilities.makeUriFromString(publicURI);
    }
     
    public URI getPublicURI() {
        return publicURI;
    }

    public String getPrivatePort() {
        return privatePort;
    }

    public URI getPrivateURI() {
        return privateURI;
    }
    
}
