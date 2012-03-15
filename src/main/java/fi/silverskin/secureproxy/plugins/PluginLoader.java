package fi.silverskin.secureproxy.plugins;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
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
     * @return SecureProxyPlugin[] with
     * @throws PluginLoadException if something goes wrong while loading plugins.
     */
    public static SecureProxyPlugin[] loadPlugins(File pluginConfig) 
            throws PluginLoadException {
        Properties conf = loadConfig(pluginConfig);

        try {
            PluginClassLoader loader = new PluginClassLoader(getPluginURLs(conf));
            ArrayList<SecureProxyPlugin> plugins = new ArrayList<SecureProxyPlugin>();

            for (String plugin : getPluginNames(conf)) {
                Class<SecureProxyPlugin> clas = loader.findClass(plugin);
                plugins.add(clas.newInstance());
            }

            return plugins.toArray(new SecureProxyPlugin[plugins.size()]);
        } catch (MalformedURLException ex) {
            LOGGER.log(Level.SEVERE, "MalformedURLException during plugin load.");
            throw new PluginLoadException("MalformedURLException during plugin load.");
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "ClassNotFoundException during plugin load");
            throw new PluginLoadException("ClassNotFoundException during plugin load");
        } catch (InstantiationException ex) {
            LOGGER.log(Level.SEVERE, "InstantationException during plugin load.");
            throw new PluginLoadException("InstantationException during plugin load.");
        } catch (IllegalAccessException ex) {
            LOGGER.log(Level.SEVERE, "IllegalAccessException during plugin load.");
            throw new PluginLoadException("IllegalAccessException during plugin load.");
        }
    }

    private static Properties loadConfig(File pluginConfig) throws PluginLoadException {
        Properties conf;
        try {
            conf = new Properties();
            conf.load(new FileReader(pluginConfig));
            if (!validateConfig(conf)) {
                LOGGER.log(Level.SEVERE, "Plugin loader config was not valid!");
                throw new PluginLoadException("Given config was not valid!");
            }
        } catch (IOException ex) {
            Logger.getLogger(PluginLoader.class.getName()).log(Level.SEVERE, null, ex);
            throw new PluginLoadException("IOException while loading config.");
        }
        return conf;
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
        File pluginDir = getPluginDirFile(pluginConfig);
        File[] allPlugins = pluginDir.listFiles(new JarFilter());

        if (allPlugins != null) {
            ArrayList<URL> urlList = new ArrayList<URL>();
            for (File f : allPlugins) {
                urlList.add(f.toURI().toURL());
            }
            return urlList.toArray(new URL[urlList.size()]);
        } else {
            LOGGER.log(Level.SEVERE, "Properties given to getPluginURLs was NULL!");
            return new URL[0];
        }
    }

    /**
     * Validates that config has all needed keys.
     *
     * @param pluginConfig file to validate
     * @return true if file can be used to try load
     */
    public static boolean validateConfig(Properties pluginConfig) {
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
        if (pluginConfig == null) {
            LOGGER.log(Level.SEVERE, "Properties given to getPluginNames was NULL!");
            return new String[0];
        }
        
        String names = pluginConfig.getProperty("load_order");
        String tmp[] = names.split(", ");
        return tmp;
    }

    public static File getPluginDirFile(Properties pluginConfig) {
        if (pluginConfig == null) {
            LOGGER.log(Level.SEVERE, "Properties given to getPluginDir was NULL!");
            return null;
        }

        File retval = new File(pluginConfig.getProperty("plugin_dir"));
        LOGGER.log(Level.INFO, "Loaded {0} as plugin directory.", retval.getAbsolutePath());
        return retval;
    }
}

class JarFilter implements FileFilter {

    private static final Logger LOGGER = Logger.getLogger(PluginLoader.class.getName(), null);

    @Override
    public boolean accept(File file) {
        System.err.println("Checking file '" + file.getAbsolutePath() + "' is plugin.");
        if (file.isFile() && file.getAbsolutePath().endsWith(".jar")) {
            LOGGER.log(Level.INFO, "Dtermied that {0} is plugin.", file.getAbsolutePath());
            return true;
        } else {
            return false;
        }
    }
}


class PluginLoadException extends RuntimeException {
    public PluginLoadException(String message) {
        super(message);
    }

    public PluginLoadException() {
    }

}