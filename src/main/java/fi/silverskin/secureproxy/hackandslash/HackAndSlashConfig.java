
package fi.silverskin.secureproxy.hackandslash;

import fi.silverskin.secureproxy.SecureProxyUtilities;
import java.net.URI;
import java.util.Properties;


public class HackAndSlashConfig {
    private URI privateURI;
    private String privatePort;
    private URI publicURI;
    
    public HackAndSlashConfig (Properties configuration) {
        String tmpPrivateUri = configuration.getProperty("privateURI").trim();
        String tmpPrivatePort = configuration.getProperty("privatePort").trim();
        String tmpPublicUri = configuration.getProperty("publicURI").trim();
        
        privateURI = SecureProxyUtilities.makeUriFromString(tmpPrivateUri);
        privatePort = tmpPrivatePort;
        publicURI = SecureProxyUtilities.makeUriFromString(tmpPublicUri);
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
