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

    /**
     * Checks if the reponse contains text or binary data
     *
     * @param response HTTP response received from httpclient
     * @return true if content is text, otherwise false
     */
    public static boolean contentIsText(HttpResponse response) {
        LOGGER.entering(FetcherUtilities.class.getName(), "contextIsText", response);
        Header contentType = response.getFirstHeader("content-type");

        Header[] contenttypes = response.getHeaders("Content-Type");
        LOGGER.log(Level.INFO, "Content-Type: {0}", (contenttypes == null ? null : contenttypes.length));

        if (contentType == null || contentType.getValue().matches("text/.*")) {
            LOGGER.exiting(FetcherUtilities.class.getName(), "contextIsText", true);
            return true;
        }

        LOGGER.exiting(FetcherUtilities.class.getName(), "contextIsText", false);
        return false;
    }

    /**
     * Converts HttpResponse to EPICTextResponse
     *
     * @param response HTTP response received from httpclient
     * @return Supplied HTTP response in EPICTextResponse format
     */
    public static EPICTextResponse toEPICText(HttpResponse response) {
        LOGGER.entering(FetcherUtilities.class.getName(), "toEPICText", response);
        EPICTextResponse e = new EPICTextResponse();

        e.setStatusCode(response.getStatusLine().getStatusCode());

        try {
            e.setBody(EntityUtils.toString(response.getEntity()));
            e.setHeaders(getHeaders(response));
            EntityUtils.consume(response.getEntity());
        } catch (IOException ex) {
            Logger.getLogger(FetcherUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }

        LOGGER.entering(FetcherUtilities.class.getName(), "toEPICText", e);
        return e;
    }

    /**
     * Converts HttpResponse  to EPICBinaryResponse
     *
     * @param response HTTP response received from httpclient
     * @return Supplied HTTP response in EPICBinaryResponse format
     */
    public static EPICBinaryResponse toEPICBinary(HttpResponse response) {
        LOGGER.entering(FetcherUtilities.class.getName(), "toEPICBinary", response);
        EPICBinaryResponse e = new EPICBinaryResponse();

        e.setStatusCode(response.getStatusLine().getStatusCode());

        try {
            e.setBody(EntityUtils.toByteArray(response.getEntity()));
            e.setHeaders(getHeaders(response));
            EntityUtils.consume(response.getEntity());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        LOGGER.exiting(FetcherUtilities.class.getName(), "toEPICBinary", e);
        return e;
    }

    /**
     * Returns the body of the HttpEntity
     *
     * @param e
     * @return Body of the HTTP entity as String
     */
    public static String getBody(HttpEntity e) {
        LOGGER.entering(FetcherUtilities.class.getName(), "getBody", e);
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

        LOGGER.exiting(FetcherUtilities.class.getName(), "getBody", sb.toString());
        return sb.toString();
    }
    
    /**
     * Returns headers of the HttpResponse
     *
     * @param e HTTP response received from httpclient
     * @return HashMap containing response headers
     */
    public static HashMap<String, String> getHeaders(HttpResponse e) {
        LOGGER.entering(FetcherUtilities.class.getName(), "getHeaders", e);
        Header[] headers = e.getAllHeaders();
        HashMap<String, String> map = new HashMap<String, String>();
        for (Header header : headers) {
            map.put(header.getName(), header.getValue());
        }

        LOGGER.exiting(FetcherUtilities.class.getName(), "getHeaders", map);
        return map;
    }

    /**
     * Copies headers from EPICRequest to HttpRequest
     *
     * @param epic Request where headers are to be copied from
     * @param req Request where headers are to be copied to
     */
    public static void copyHeaders(EPICRequest epic, HttpRequest req) {
        LOGGER.entering(FetcherUtilities.class.getName(), "copyHeaders", new Object[] {epic, req});
        for (Map.Entry<String, String> k : epic.getHeaders().entrySet()) {
            req.addHeader(k.getKey(), k.getValue());
        }
        LOGGER.exiting(FetcherUtilities.class.getName(), "copyHeaders");
    }

    /**
     * Copies body content from epic to req. This method works only with
     * HttpPost or HttpPut instances.
     *
     * @param epic Source of body data.
     * @param req HttpPost or HttpPut to modify
     */
    public static void copyBody(EPICRequest epic, HttpEntityEnclosingRequestBase req) {
        LOGGER.entering(FetcherUtilities.class.getName(), "copyBody", new Object[] {epic, req});
        try {
            req.setEntity(new StringEntity(epic.getBody()));
        } catch (UnsupportedEncodingException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        LOGGER.exiting(FetcherUtilities.class.getName(), "copyBody");
    }
}
