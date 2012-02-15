package fi.silverskin.secureproxy.resourcefetcher;

import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICResponse;
import java.io.IOException;
import org.apache.http.HttpResponse;
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
        try {
            HttpGet get = new HttpGet(req.getUri());
            FetcherUtilities.copyHeaders(req, get);
            HttpResponse res = httpclient.execute(get);
            if (FetcherUtilities.contentIsText(res)) {
                return (EPICResponse) FetcherUtilities.toEPICText(res);
            } else {
                return (EPICResponse) FetcherUtilities.toEPICBinary(res);
            }
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }
    
    private EPICResponse handlePost(EPICRequest req) {

        try {
            HttpPost post = new HttpPost(req.getUri());
            FetcherUtilities.copyHeaders(req, post);
            HttpResponse res = httpclient.execute(post);
            if (FetcherUtilities.contentIsText(res)) {
                return (EPICResponse) FetcherUtilities.toEPICText(res);
            } else {
                return (EPICResponse) FetcherUtilities.toEPICBinary(res);
            }            
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }

    private EPICResponse handlePut(EPICRequest req) {

        try {
            HttpPut put = new HttpPut(req.getUri());
            FetcherUtilities.copyHeaders(req, put);
            HttpResponse res = httpclient.execute(put);
            if (FetcherUtilities.contentIsText(res)) {
                return (EPICResponse) FetcherUtilities.toEPICText(res);
            } else {
                return (EPICResponse) FetcherUtilities.toEPICBinary(res);
            }
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }

    private EPICResponse handleDelete(EPICRequest req) {
        try {
            HttpDelete delete = new HttpDelete(req.getUri());
            FetcherUtilities.copyHeaders(req, delete);
            HttpResponse res = httpclient.execute(delete);
            if (FetcherUtilities.contentIsText(res)) {
                return (EPICResponse) FetcherUtilities.toEPICText(res);
            } else {
                return (EPICResponse) FetcherUtilities.toEPICBinary(res);
            }            
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }

    private EPICResponse handleHead(EPICRequest req) {
        try {
            HttpHead head = new HttpHead(req.getUri());
            FetcherUtilities.copyHeaders(req, head);
            HttpResponse res = httpclient.execute(head);
            if (FetcherUtilities.contentIsText(res)) {
                return (EPICResponse) FetcherUtilities.toEPICText(res);
            } else {
                return (EPICResponse) FetcherUtilities.toEPICBinary(res);
            } 
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }
}
