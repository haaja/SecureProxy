package fi.silverskin.secureproxy.plugins;

import fi.silverskin.secureproxy.EPICBinaryResponse;
import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICTextResponse;


public interface SecureProxyPlugin {
    public String getName();
    public void run(EPICRequest epic);
    public void run(EPICTextResponse epic);
    public void run(EPICBinaryResponse epic);
}
