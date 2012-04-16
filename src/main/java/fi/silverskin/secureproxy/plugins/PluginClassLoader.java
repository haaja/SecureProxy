package fi.silverskin.secureproxy.plugins;

import java.net.URL;
import java.net.URLClassLoader;


public class PluginClassLoader extends URLClassLoader {

    public PluginClassLoader(URL[] urls) {
        super(urls);
    }

    @Override
    public Class<SecureProxyPlugin> findClass(String name) throws ClassNotFoundException {
        return (Class<SecureProxyPlugin>) super.findClass(name);
    }
}
