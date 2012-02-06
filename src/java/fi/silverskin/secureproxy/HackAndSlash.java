package fi.silverskin.secureproxy;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class HackAndSlash {

    private EPICRequest request;
    private EPICResponse response;

    //To be replaced with proper settings
    private String remoteUrl = "localhost";
    private String remotePort = "8084";

    public HackAndSlash() {
        this.request = null;
        this.response = null;
    }

    public EPICRequest hackAndSlashIn(EPICRequest request) {

        throw new NotImplementedException();
    }

    public EPICResponse hackAndSlashIn(EPICResponse response) {

        throw new NotImplementedException();
    }

    public EPICRequest hackAndSlashOut(EPICRequest request) {

        throw new NotImplementedException();
    }

    public EPICResponse hackAndSlashOut(EPICResponse response) {

        throw new NotImplementedException();
    }
}
