package fi.silverskin.secureproxy.plugins;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * Collection of static methods to perform plugin loading.
 * 
 * @author Iivari Äikäs
 */
public class PluginLoader {

    private static final Logger LOGGER = 
            Logger.getLogger(PluginLoader.class.getName(), null);
	private ClassLoader loader;


	/**
	 * These are needed for access to existing ClassLoader.
	 */
	private PluginLoader() {
		loader = PluginLoader.class.getClassLoader();
	}

	protected ClassLoader getLoader() {
		return loader;
	}



    /**
     * Load plugins given in config, sorted by load order.
     * 
     * Throws PluginLoadException if something fails during plugin loading, note
     * that no plugins to load is NOT error condition.
     *
     * @param pluginConfig
     * @return SecureProxyPlugin[] with
     * @throws PluginLoadException if something goes wrong while loading plugins.
     */
    public static SecureProxyPlugin[] loadPlugins(File pluginConfig) 
            throws PluginLoadException, InvocationTargetException, SecurityException, NoSuchMethodException {
        Properties conf = loadConfig(pluginConfig);

        try {
            ArrayList<SecureProxyPlugin> plugins = new ArrayList<SecureProxyPlugin>();
			PluginLoader plugLoad = new PluginLoader();
			ClassLoader clsLoader = new URLClassLoader(getPluginURLs(conf), 
														plugLoad.getLoader());

			String[] pluginNames = getPluginNames(conf);

			if (pluginNames != null) {
				LOGGER.log(Level.INFO, "Loading plugins..");
				for (String plugin : pluginNames) {
					Class<?> clas = Class.forName(plugin, true, clsLoader);
					
					Class<? extends SecureProxyPlugin> duh = clas.asSubclass(SecureProxyPlugin.class);
					Constructor<? extends SecureProxyPlugin> ctor = duh.getConstructor();
					SecureProxyPlugin plug = ctor.newInstance();
					plugins.add(plug);

					LOGGER.log(Level.INFO, "\tSuccesfully loaded plugin '{0}'", plugin);
				}
			} else {
				LOGGER.log(Level.INFO, "There was no plugins to load!");
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


    private static Properties loadConfig(File pluginConfig) throws 
	    PluginLoadException {
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
     * Validates that config has all needed keys. Doesn't validate values!
     *
     * @param pluginConfig file to validate.
     * @return true if file can be used to try loading.
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
     * Get plugin names mentioned in config. 
     * 
     * @param pluginConfig Properties made from configuration file.
     * @return List of plugin names, in loading order or null if list didn't exist or was empty.
     */
    public static String[] getPluginNames(Properties pluginConfig) {
        LOGGER.entering(PluginLoader.class.getName(), "getPluginNames", pluginConfig);
        if (pluginConfig == null) {
            LOGGER.log(Level.SEVERE, "Properties given to getPluginNames was NULL!");
            return new String[0];
        }
        
        String names = pluginConfig.getProperty("load_order");
		if (names != null && !names.equals("")) {
			String tmp[] = names.split(", ");
			
			LOGGER.exiting(PluginLoader.class.getName(), "getPluginNames", tmp);
			return tmp;
		} else {
			LOGGER.exiting(PluginLoader.class.getName(), "getPluginNames", null);
			return null;
		}
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


class PluginLoadException extends RuntimeException {
    public PluginLoadException(String message) {
        super(message);
    }

    public PluginLoadException() {
    }
}