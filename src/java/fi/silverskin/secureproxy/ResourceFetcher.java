package fi.silverskin.secureproxy;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.DefaultHttpClient;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ResourceFetcher {

    HttpClient httpclient;

    public ResourceFetcher() {
        httpclient = new DefaultHttpClient();
    }

    public EPICResponse handleGet(EPICRequest req) {
        try {
            HttpGet get = new HttpGet(req.getUrl());
            return responseToEPICResponse(httpclient.execute(get));
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }

    public EPICResponse handlePost(EPICRequest req) {

        try {
            HttpPost post = new HttpPost(req.getUrl());
            return responseToEPICResponse(httpclient.execute(post));
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }

    public EPICResponse handlePut(EPICRequest req) {

        try {
            HttpPut put = new HttpPut(req.getUrl());
            return responseToEPICResponse(httpclient.execute(put));
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }

    public EPICResponse handleDelete(EPICRequest req) {

        try {
            HttpDelete delete = new HttpDelete(req.getUrl());
            return responseToEPICResponse(httpclient.execute(delete));
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }

    public EPICResponse handleHead(EPICRequest req) {

        try {
            HttpHead head = new HttpHead(req.getUrl());
            return responseToEPICResponse(httpclient.execute(head));
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }

    private EPICResponse responseToEPICResponse(HttpResponse e) {
        // Helpful stuff in; 
        // http://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html
        // http://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/index.html
        throw new NotImplementedException();
    }
}
