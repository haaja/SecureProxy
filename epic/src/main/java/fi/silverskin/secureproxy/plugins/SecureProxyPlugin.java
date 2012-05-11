package fi.silverskin.secureproxy.plugins;

import fi.silverskin.secureproxy.EPICBinaryResponse;
import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICTextResponse;


/**
 * This is interface that plugin needs to implement in order to be loadadle into
 * plugin system.
 *
 * @author Iivari Äikäs
 */
public interface SecureProxyPlugin {

    /**
     * Returns name of the plugin. 
     * 
     * This name needs to match to class which is loaded into plugin system. In
     * other words, plugin name in pluginloader configuration file needs to be
     * same as this value.
     *
     * @return Name of the plugin.
     */
    public String getName();

    /**
     * Call this plugin for EPICRequest
     *
     * @param epic to be checked/handled by this plugin.
     */
    public void run(EPICRequest epic);

    /**
     * Call this plugin for EPICResponse
     *
     * @param epic to be checked/handled by this plugin.
     */
    public void run(EPICTextResponse epic);

    /**
     * Call this plugin for EPICBinaryResponse
     *
     * @param epic to be checked/handled by this plugin.
     */
    public void run(EPICBinaryResponse epic);
}
