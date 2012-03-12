package fi.silverskin.secureproxy.plugins;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginLoader {

    private static final Logger LOGGER = Logger.getLogger(PluginLoader.class.getName(), null);

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
     * Get URLs to plugins in given plugin_dir mentioned in pluginConfig.
     *
     * @param pluginConfig
     * @return URL[] containing all jars in plugin directory
     * @throws MalformedURLException if creating URL fails
     */
    public static URL[] getPluginURLs(Properties pluginConfig)
            throws MalformedURLException {
        LOGGER.entering(PluginLoader.class.getName(), "getPluginURLs", pluginConfig);
        File pluginDir = getPluginDirFile(pluginConfig);
        File[] allPlugins = pluginDir.listFiles(new JarFilter());
        URL[] pluginURLs;

        if (allPlugins != null) {
            ArrayList<URL> urlList = new ArrayList<URL>();
            for (File f : allPlugins) {
                urlList.add(f.toURI().toURL());
            }
            pluginURLs = urlList.toArray(new URL[urlList.size()]);
        } else {
            pluginURLs = new URL[0];
        }
        LOGGER.exiting(PluginLoader.class.getName(), "getPluginURLs", pluginURLs);

        return pluginURLs;
    }

    /**
     * Validates that config has all needed keys.
     *
     * @param pluginConfig file to validate
     * @return true if file can be used to try load
     */
    public static boolean validateConfig(Properties pluginConfig) {
        LOGGER.entering(PluginLoader.class.getName(), "validateConfig", pluginConfig);
        boolean isValid = true;

        if (pluginConfig == null) {
            return false;
        }
        
        if (!pluginConfig.containsKey("plugin_dir")) {
            isValid = false;
        }
        if (!getPluginDirFile(pluginConfig).isDirectory()) {
            isValid = false;
        }
        if (!pluginConfig.containsKey("load_order")) {
            isValid = false;
        }

        LOGGER.exiting(PluginLoader.class.getName(), "validateConfig", isValid);
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
        LOGGER.entering(PluginLoader.class.getName(), "getPluginNames", pluginConfig);
        if (pluginConfig == null) {
            LOGGER.log(Level.SEVERE, "Properties given to getPluginNames was NULL!");
            return new String[0];
        }
        
        String names = pluginConfig.getProperty("load_order");
        String tmp[] = names.split(", ");

        LOGGER.exiting(PluginLoader.class.getName(), "getPluginNames", tmp);
        return tmp;
    }

    public static File getPluginDirFile(Properties pluginConfig) {
        LOGGER.entering(PluginLoader.class.getName(), "getPluginDirFile", pluginConfig);
        if (pluginConfig == null) {
            LOGGER.log(Level.SEVERE, "Properties given to getPluginDir was NULL!");
            return null;
        }

        File retval = new File(pluginConfig.getProperty("plugin_dir"));
        LOGGER.log(Level.INFO, "Loaded {0} as plugin directory.", retval.getAbsolutePath());

        LOGGER.entering(PluginLoader.class.getName(), "getPluginDirFile", retval);
        return retval;
    }
}

class JarFilter implements FileFilter {

    private static final Logger LOGGER = Logger.getLogger(PluginLoader.class.getName(), null);

    @Override
    public boolean accept(File file) {
        LOGGER.entering(PluginLoader.class.getName(), "accept", file);
        System.err.println("Checking file '" + file.getAbsolutePath() + "' is plugin.");
        if (file.isFile() && file.getAbsolutePath().endsWith(".jar")) {
            LOGGER.log(Level.INFO, "Dtermied that {0} is plugin.", file.getAbsolutePath());
            LOGGER.exiting(PluginLoader.class.getName(), "accept", true);
            return true;
        } else {
            LOGGER.exiting(PluginLoader.class.getName(), "accept", false);
            return false;
        }
    }
}