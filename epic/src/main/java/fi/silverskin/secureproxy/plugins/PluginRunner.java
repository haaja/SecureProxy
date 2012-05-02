package fi.silverskin.secureproxy.plugins;

import fi.silverskin.secureproxy.EPICBinaryResponse;
import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICTextResponse;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PluginRunner {

    private static final Logger LOGGER = 
            Logger.getLogger(PluginLoader.class.getName(), null);
    private SecureProxyPlugin[] plugins;

    public PluginRunner(File pluginConfig) {
		try {
			this.plugins = PluginLoader.loadPlugins(pluginConfig);
		} catch (PluginLoadException ex) {
			LOGGER.log(Level.SEVERE, ex.toString());
		} catch (InvocationTargetException ex) {
			LOGGER.log(Level.SEVERE, ex.toString());
		} catch (SecurityException ex) {
			LOGGER.log(Level.SEVERE, ex.toString());
			throw ex;
		} catch (NoSuchMethodException ex) {
			LOGGER.log(Level.SEVERE, ex.toString());
		}
    }



    public void run(EPICRequest epic) {
        for (SecureProxyPlugin p : plugins) {
			LOGGER.entering(EPICRequest.class.getName(), "EPICRequest.run");
            p.run(epic);
			LOGGER.exiting(EPICRequest.class.getName(), "EPICRequest.run");
		}
    }

    public void run(EPICTextResponse epic) {
        for (SecureProxyPlugin p : plugins) {
			LOGGER.entering(EPICRequest.class.getName(), "EPICTextResponse.run");
            p.run(epic);
			LOGGER.exiting(EPICRequest.class.getName(), "EPICTextResponse.run");
		}
    }

    public void run(EPICBinaryResponse epic) {
        for (SecureProxyPlugin p : plugins) {
			LOGGER.entering(EPICRequest.class.getName(), "EPICBinaryResponse.run");
            p.run(epic);
			LOGGER.exiting(EPICRequest.class.getName(), "EPICBinaryResponse.run");
		}
    }
}
