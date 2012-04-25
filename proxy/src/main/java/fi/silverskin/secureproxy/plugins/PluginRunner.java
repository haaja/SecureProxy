package fi.silverskin.secureproxy.plugins;

import fi.silverskin.secureproxy.EPICBinaryResponse;
import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICTextResponse;
import java.io.File;


public class PluginRunner {

    private SecureProxyPlugin[] plugins;

    public PluginRunner(File pluginConfig) {
        this.plugins = PluginLoader.loadPlugins(pluginConfig);
    }



    public void run(EPICRequest epic) {
        for (SecureProxyPlugin p : plugins)
            p.run(epic);
    }

    public void run(EPICTextResponse epic) {
        for (SecureProxyPlugin p : plugins)
            p.run(epic);
    }

    public void run(EPICBinaryResponse epic) {
        for (SecureProxyPlugin p : plugins)
            p.run(epic);
    }
}
