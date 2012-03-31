
package fi.silverskin.secureproxy.hackandslash;

import fi.silverskin.secureproxy.SecureProxyUtilities;
import java.net.URI;
import java.util.Properties;


public class HackAndSlashConfig {
    private URI privateURI;
    private String privateHttpPort;
    private String privateHttpsPort;
    private URI publicURI;
    private String publicHttpPort;
    private String publicHttpsPort;
    
    public HackAndSlashConfig (Properties configuration) {
        String tmpPrivateUri = configuration.getProperty("privateURI").trim();
        String tmpPrivateHttpPort = configuration.getProperty("privateHttpPort").trim();
        String tmpPrivateHttpsPort = configuration.getProperty("privateHttpsPort").trim();
        String tmpPublicUri = configuration.getProperty("publicURI").trim();
        String tmpPublicHttpPort = configuration.getProperty("publicHttpPort").trim();
        String tmpPublicHttpsPort = configuration.getProperty("publicHttpsPort").trim();

        privateURI = SecureProxyUtilities.makeUriFromString(tmpPrivateUri);
        privateHttpPort = tmpPrivateHttpPort;
        privateHttpsPort = tmpPrivateHttpsPort;
        publicURI = SecureProxyUtilities.makeUriFromString(tmpPublicUri);
        publicHttpPort = tmpPublicHttpPort;
        publicHttpsPort = tmpPublicHttpsPort;
    }

    public HackAndSlashConfig(String privateURI, 
                              String privateHttpPort,
                              String privateHttpsPort,
                              String publicURI,
                              String publicHttpPort,
                              String publicHttpsPort) {
        this.privateURI = SecureProxyUtilities.makeUriFromString(privateURI);
        this.privateHttpPort = privateHttpPort;
        this.privateHttpsPort = privateHttpsPort;
        this.publicURI = SecureProxyUtilities.makeUriFromString(publicURI);
        this.publicHttpPort = publicHttpPort;
        this.publicHttpsPort = publicHttpsPort;
    }
     
    public URI getPublicURI() {
        return publicURI;
    }

    public String getPublicHttpPort() {
        return publicHttpPort;
    }

    public String getPublicHttpsPort() {
        return publicHttpsPort;
    }

    public URI getPrivateURI() {
        return privateURI;
    }

    public String getPrivateHttpPort() {
        return privateHttpPort;
    }

    public String getPrivateHttpsPort() {
        return this.privateHttpsPort;
    }
    
}
