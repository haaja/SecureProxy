package fi.silverskin.secureproxy.plugins;

import fi.silverskin.secureproxy.EPICBinaryResponse;
import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICTextResponse;
import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PluginRunner {

    private static final Logger LOGGER = 
            Logger.getLogger(PluginLoader.class.getName(), null);
    private SecureProxyPlugin[] plugins;



    public PluginRunner(File pluginConfig) {
        try {
            this.plugins = PluginLoader.loadPlugins(pluginConfig);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.toString());
            LOGGER.log(Level.WARNING, "PluginLoader failed, there is no plugins to run! NOT bailing out though..");
        }
    }

    public PluginRunner(Properties pluginConfig) {
        try {
            this.plugins = PluginLoader.loadPlugins(pluginConfig);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.toString());
            LOGGER.log(Level.WARNING, "PluginLoader failed, there is no plugins to run! NOT bailing out though..");
        }
    }



    public void run(EPICRequest epic) {
        if (!checkPluginsAvailable()) {
            return;
        }

        for (SecureProxyPlugin p : plugins) {
            LOGGER.entering(EPICRequest.class.getName(), "EPICRequest.run");
            p.run(epic);
            LOGGER.exiting(EPICRequest.class.getName(), "EPICRequest.run");
        }
    }

    public void run(EPICTextResponse epic) {
        if (!checkPluginsAvailable()) {
            return;
        }

        for (SecureProxyPlugin p : plugins) {
            LOGGER.entering(EPICRequest.class.getName(), "EPICTextResponse.run");
            p.run(epic);
            LOGGER.exiting(EPICRequest.class.getName(), "EPICTextResponse.run");
        }
    }

    public void run(EPICBinaryResponse epic) {
        if (!checkPluginsAvailable()) {
            return;
        }

        for (SecureProxyPlugin p : plugins) {
            LOGGER.entering(EPICRequest.class.getName(), "EPICBinaryResponse.run");
            p.run(epic);
            LOGGER.exiting(EPICRequest.class.getName(), "EPICBinaryResponse.run");
        }
    }


    private boolean checkPluginsAvailable() {
        if (plugins == null) {
            LOGGER.log(Level.INFO, "There was no plugins to run!");
            return false;
        } else {
            return true;
        }
    }
}
