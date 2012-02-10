package fi.silverskin.secureproxy;

import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.DefaultHttpClient;

public class ResourceFetcher {

    HttpClient httpclient;

    public ResourceFetcher() {
        httpclient = new DefaultHttpClient();
    }

    public EPICResponse handleGet(EPICRequest req) {
        try {
            HttpGet get = new HttpGet(req.getUri());
            copyHeaders(req, get);
            return responseToEPICResponse(httpclient.execute(get));
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }

    public EPICResponse handlePost(EPICRequest req) {

        try {
            HttpPost post = new HttpPost(req.getUri());
            return responseToEPICResponse(httpclient.execute(post));
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }

    public EPICResponse handlePut(EPICRequest req) {

        try {
            HttpPut put = new HttpPut(req.getUri());
            return responseToEPICResponse(httpclient.execute(put));
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }

    public EPICResponse handleDelete(EPICRequest req) {

        try {
            HttpDelete delete = new HttpDelete(req.getUri());
            return responseToEPICResponse(httpclient.execute(delete));
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }

    public EPICResponse handleHead(EPICRequest req) {

        try {
            HttpHead head = new HttpHead(req.getUri());
            return responseToEPICResponse(httpclient.execute(head));
        } catch (IOException ex) {
            return new EPICResponse();
        }
    }

    private EPICResponse responseToEPICResponse(HttpResponse e) {
        HttpEntity ent = e.getEntity();

        EPICResponse response = new EPICResponse();
        response.setBody(getBody(ent));
        response.setHeaders(getHeaders(e));
        return response;
    }

    private String getBody(HttpEntity e) {
        StringBuilder sb = null;
        try {
            Reader reader = new BufferedReader(
                    new InputStreamReader(e.getContent(), e.getContentEncoding().getName()));

            sb = new StringBuilder((int) e.getContentLength());

            final char[] buffer = new char[0x10000];
            int read;
            do {
                read = reader.read(buffer, 0, buffer.length);
                if (read > 0) {
                    sb.append(buffer, 0, read);
                }
            } while (read >= 0);

        } catch (IOException ex) {
            Logger.getLogger(ResourceFetcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(ResourceFetcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sb.toString();
    }

    private HashMap<String, String> getHeaders(HttpResponse e) {
        Header[] headers = e.getAllHeaders();
        HashMap<String, String> map = new HashMap<String, String>();
        for (Header header : headers) {
            map.put(header.getName(), header.getValue());
        }
        return map;
    }

    private void copyHeaders(EPICRequest req, HttpRequest get) {
        httpclient.getParams().removeParameter("http.useragent");
        for (Entry<String, String> k : req.getHeaders().entrySet()) {
            get.addHeader(k.getKey(), k.getValue());
        }
        if (req.getHeaders().containsKey("USER_AGENT")) {
            httpclient.getParams().setParameter("http.useragent",
                    req.getHeaders().get("USER_AGENT"));
        }
    }
}
