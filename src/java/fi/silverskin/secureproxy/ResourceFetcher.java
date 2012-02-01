package fi.silverskin.secureproxy;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class ResourceFetcher {

    HttpClient httpclient;

    public ResourceFetcher() {
        httpclient = new DefaultHttpClient();
    }

    public EPICResponse handleGet(EPICRequest req) {

        return new EPICResponse();
    }

    public EPICResponse handlePost(EPICRequest req) {

        return new EPICResponse();
    }

    public EPICResponse handlePut(EPICRequest req) {

        return new EPICResponse();
    }

    public EPICResponse handleDelete(EPICRequest req) {

        return new EPICResponse();
    }

    public EPICResponse handleHead(EPICRequest req) {

        return new EPICResponse();
    }
}
