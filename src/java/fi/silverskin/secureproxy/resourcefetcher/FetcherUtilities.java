package fi.silverskin.secureproxy.resourcefetcher;

import fi.silverskin.secureproxy.EPICBinaryResponse;
import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICTextResponse;
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
import org.apache.http.util.EntityUtils;

public class FetcherUtilities {

    private static final Logger LOGGER = Logger.getLogger(FetcherUtilities.class.getName(), null);
  
    public static boolean contentIsText(HttpResponse response) {
        Header contentType = response.getFirstHeader("content-type");

        Header[] contenttypes = response.getHeaders("Content-Type");
        LOGGER.log(Level.INFO, "Content-Type: {0}", (contenttypes == null ? null : contenttypes.length));

        if (contentType == null || contentType.getValue().matches("text/.*")) {
            return true;
        }
        
        return false;
    }

    public static EPICTextResponse toEPICText(HttpResponse response) {
        EPICTextResponse e = new EPICTextResponse();
        try {
            //        e.setBody(getBody(response.getEntity()));
            e.setBody(EntityUtils.toString(response.getEntity()));
            e.setHeaders(getHeaders(response));
            EntityUtils.consume(response.getEntity());
        } catch (IOException ex) {
            Logger.getLogger(FetcherUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return e;
    }

    
    public static EPICBinaryResponse toEPICBinary(HttpResponse response) {
        EPICBinaryResponse e = new EPICBinaryResponse();

        try {
            e.setBody(EntityUtils.toByteArray(response.getEntity()));
            e.setHeaders(getHeaders(response));
            EntityUtils.consume(response.getEntity());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
        return e;
    }

    public static String getBody(HttpEntity e) {
        StringBuilder sb = null;
        try {
            Reader reader = new BufferedReader(new InputStreamReader(e.getContent()));
            sb = new StringBuilder();

            final char[] buffer = new char[0x10000];
            int read = reader.read(buffer, 0, buffer.length);

            while (read >= 0) {
                if (read > 0) {
                    sb.append(buffer, 0, read);
                }
                read = reader.read(buffer, 0, buffer.length);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
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
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}
