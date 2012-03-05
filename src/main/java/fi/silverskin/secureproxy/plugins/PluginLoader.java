package fi.silverskin.secureproxy.plugins;

import java.io.File;
import java.util.Properties;


public class PluginLoader {

    /**
     * Load plugins given in config sorted by load order.
     *
     * @param pluginConfig
     * @return
     */
    public static SecureProxyPlugin[] loadPlugins(File pluginConfig) {
        return new SecureProxyPlugin[0];
    }


    /**
     * Validates that config has all needed keys.
     *
     * @param pluginConfig file to validate
     * @return true if file can be used to try load
     */
    public static boolean validateConfig(Properties pluginConfig) {
        boolean isValid = true;

        if (!pluginConfig.containsKey("plugin_dir"))
            isValid = false;
        if (!pluginConfig.containsKey("load_order"))
            isValid = false;

        return isValid;
    }

    /**
     * Get plugin names mentioned in config. Note, this namelist is NOT queried
     * from actual plugins!
     *
     * @param pluginConfig Properties made from configuration file.
     * @return List of plugin names, in loading order.
     */
    public static String[] getPluginNames(Properties pluginConfig) {
        String names = pluginConfig.getProperty("load_order");
        String tmp[] = names.split(", ");
        return tmp;
    }


    public static File getPluginDirFile(Properties pluginProperties) {
        return new File(pluginProperties.getProperty("plugin_dir"));
    }
}