
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

    /**
     * Returns the publicURI configuration value
     * 
     * @return publicURI configuration value
     */
    public URI getPublicURI() {
        return publicURI;
    }

    /**
     * Returns the publicHttpPort configuration value
     *
     * @return publicHttpPort configuration value
     */
    public String getPublicHttpPort() {
        return publicHttpPort;
    }

    /**
     * Returns the publicHttpsPort configuration value
     *
     * @return publicHttpsPort configuration value
     */
    public String getPublicHttpsPort() {
        return publicHttpsPort;
    }

    /**
     * Returns the privateURI configuration value
     *
     * @return privateURI configuration value
     */
    public URI getPrivateURI() {
        return privateURI;
    }

    /**
     * Returns the privateHttpPort configuration value
     *
     * @return privateHttpPort configuration value
     */
    public String getPrivateHttpPort() {
        return privateHttpPort;
    }

    /**
     * Returns the privateHttpsPort configuration value
     *
     * @return privateHttpsPort configuration value
     */
    public String getPrivateHttpsPort() {
        return this.privateHttpsPort;
    }
    
}
