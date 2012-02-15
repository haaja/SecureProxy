/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy.resourcefetcher;

import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;

/**
 *
 * @author orva
 */
public class FetcherUtilities {

   

    public static EPICResponse responseToEPICResponse(HttpResponse e) {
        EPICResponse response = new EPICResponse();
        response.setBody(getBody(e.getEntity()));
        response.setHeaders(getHeaders(e));
        return response;
    }

    public static String getBody(HttpEntity e) {
        StringBuilder sb = null;
        try {
            Reader reader = new BufferedReader(new InputStreamReader(e.getContent()));
            sb = new StringBuilder();

            final char[] buffer = new char[0x10000];
            int read = reader.read(buffer, 0, buffer.length);
            
            while(read >= 0) {
                if (read > 0)
                    sb.append(buffer, 0, read);
                read = reader.read(buffer, 0, buffer.length);
            }
        } catch (IOException ex) {
            Logger.getLogger(ResourceFetcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(ResourceFetcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sb.toString();
    }

    public static HashMap<String, String> getHeaders(HttpResponse e) {
        Header[] headers = e.getAllHeaders();
        HashMap<String, String> map = new HashMap<String, String>();
        for (Header header : headers) {
            map.put(header.getName(), header.getValue());
        }
        return map;
    }

    public static void copyHeaders(EPICRequest epic, HttpRequest req) {
        for (Map.Entry<String, String> k : epic.getHeaders().entrySet()) {
            req.addHeader(k.getKey(), k.getValue());
        }
    }
    
    /**
     * Copies body content from epic to req. This method works only with
     * HttpPost or HttpPut instances. 
     * 
     * @param epic Source of body data.
     * @param req HttpPost or HttpPut to modify
     */
    public static void copyBody(EPICRequest epic, HttpEntityEnclosingRequestBase req) {
        try {
            req.setEntity(new StringEntity(epic.getBody()));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ResourceFetcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
