
package fi.silverskin.secureproxy.hackandslash;


public class HackAndSlashConfig {
    private String remoteUrl;
    private String remotePort;
    private String basePseudoURI;

    public HackAndSlashConfig(String remoteUrl, String remotePort, String basePseudoURI) {
        this.remoteUrl = remoteUrl;
        this.remotePort = remotePort;
        this.basePseudoURI = basePseudoURI;
    }

    public String getBasePseudoURI() {
        return basePseudoURI;
    }

    public String getRemotePort() {
        return remotePort;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }
    
}
