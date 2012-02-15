package fi.silverskin.secureproxy.resourcefetcher;

import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.DefaultHttpClient;

public class ResourceFetcher {

    HttpClient httpclient;

    public ResourceFetcher() {
        httpclient = new DefaultHttpClient();
        httpclient.getParams().removeParameter("http.useragent");
    }

    
    
    public EPICResponse handleRequest(EPICRequest req) {
        Logger.getLogger(ResourceFetcher.class.getName()).log(Level.INFO, req.toString());
        switch (req.getType()) {
            case POST: return handlePost(req);
            case PUT: return handlePut(req);
            case GET: return handleGet(req);
            case DELETE: return handleDelete(req);
            case HEAD: return handleHead(req);
            default: return new EPICResponse();
        }
    }
    
    
    private EPICResponse handleGet(EPICRequest req) {
        //return new EPICResponse(new HashMap<String,String>(req.getHeaders()), "ASDFASDF\n");
        try {
            Logger.getLogger("log").log(Level.INFO, "GET Request \"" + req.getUri() +"\"");           
            HttpGet get = new HttpGet(req.getUri());
            FetcherUtilities.copyHeaders(req, get);
            return FetcherUtilities.responseToEPICResponse(httpclient.execute(get));
        } catch (Throwable ex) {
            Logger.getLogger("log").log(Level.SEVERE, "Exception in GET:{0}", ex);
            System.err.println("Exception in GET: " + ex);
            return new EPICResponse();
        }
    }
    
    private EPICResponse handlePost(EPICRequest req) {

        try {
            HttpPost post = new HttpPost(req.getUri());
            FetcherUtilities.copyHeaders(req, post);
            FetcherUtilities.copyBody(req, post);
            return FetcherUtilities.responseToEPICResponse(httpclient.execute(post));
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }

    private EPICResponse handlePut(EPICRequest req) {

        try {
            HttpPut put = new HttpPut(req.getUri());
            FetcherUtilities.copyHeaders(req, put);
            FetcherUtilities.copyBody(req, put);
            return FetcherUtilities.responseToEPICResponse(httpclient.execute(put));
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }

    private EPICResponse handleDelete(EPICRequest req) {
        try {
            HttpDelete delete = new HttpDelete(req.getUri());
            FetcherUtilities.copyHeaders(req, delete);
            return FetcherUtilities.responseToEPICResponse(httpclient.execute(delete));
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }

    private EPICResponse handleHead(EPICRequest req) {
        try {
            HttpHead head = new HttpHead(req.getUri());
            FetcherUtilities.copyHeaders(req, head);
            return FetcherUtilities.responseToEPICResponse(httpclient.execute(head));
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }
}
