package fi.silverskin.secureproxy.resourcefetcher;

import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.cache.CachingHttpClient;

public class ResourceFetcher {

    HttpClient httpclient;
    private static final Logger LOGGER = Logger.getLogger(ResourceFetcher.class.getName(), null);

    public ResourceFetcher() {
        httpclient = new CachingHttpClient();
        httpclient.getParams().removeParameter("http.useragent");
    }

    /**
     * Handle methods coming from ProxyController.
     *
     * @param req EPICRequest to be handled.
     * @return Response for the request or empty response.
     */
    public EPICResponse handleRequest(EPICRequest req) {
        LOGGER.log(Level.INFO, req.toString());
        switch (req.getType()) {
            case POST:
                return handlePost(req);
            case PUT:
                return handlePut(req);
            case GET:
                return handleGet(req);
            case DELETE:
                return handleDelete(req);
            case HEAD:
                return handleHead(req);
            default:
                return new EPICResponse();
        }
    }

    /**
     * Handle HTTP GET requests.
     *
     * @param req EPICRequest containing HTTP GET request.
     * @return Response for the handled request or empty response.
     */
    private EPICResponse handleGet(EPICRequest req) {
        LOGGER.entering(ResourceFetcher.class.getName(), "handleGet", req);
        EPICResponse retVal = null;
        try {
            LOGGER.log(Level.INFO, "GET Request \"{0}\"", req.getUri());
            LOGGER.log(Level.INFO, "Request Headers {0}", req.getHeaders());

            HttpGet get = new HttpGet(req.getUri());
            FetcherUtilities.copyHeaders(req, get);

            HttpResponse res = httpclient.execute(get);
            LOGGER.info("HTTP response status line: "+res.getStatusLine());
            LOGGER.log(Level.INFO, "HTTP response Headers Before Anything: {0}", res.getAllHeaders().length);

            for (Header h : res.getAllHeaders()) {
                System.err.println("\t" + h);
            }

            if (FetcherUtilities.contentIsText(res)) {
                retVal = FetcherUtilities.toEPICText(res);
            } else {
                retVal = FetcherUtilities.toEPICBinary(res);
            }
        }  catch (Throwable ex) {
            LOGGER.log(Level.SEVERE, "Exception in GET:{0}", ex);
        }

        LOGGER.exiting(ResourceFetcher.class.getName(), "handleGet", retVal);
        return retVal;
    }

    /**
     * Handle HTTP POST request.
     *
     * @param req EPICRequest containing HTTP POST request.
     * @return Response for the handled request or empty response.
     */
    private EPICResponse handlePost(EPICRequest req) {

        try {
            LOGGER.log(Level.INFO, "POST Request \"" + req.getUri() +"\"");
            HttpPost post = new HttpPost(req.getUri());
            FetcherUtilities.copyHeaders(req, post);
            FetcherUtilities.copyBody(req, post);
            HttpResponse res = httpclient.execute(post);
            if (FetcherUtilities.contentIsText(res)) {
                return (EPICResponse) FetcherUtilities.toEPICText(res);
            } else {
                return (EPICResponse) FetcherUtilities.toEPICBinary(res);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Exception in POST:{0}", ex);
            return new EPICResponse();
        }
    }

    /**
     * Handle HTTP PUT request.
     *
     * @param req EPICRequest containing HTTP PUT request.
     * @return Response for the handled request or empty response.
     */
    private EPICResponse handlePut(EPICRequest req) {

        try {
            LOGGER.log(Level.INFO, "PUT Request \"" + req.getUri() +"\"");
            HttpPut put = new HttpPut(req.getUri());
            FetcherUtilities.copyHeaders(req, put);
            FetcherUtilities.copyBody(req, put);
            HttpResponse res = httpclient.execute(put);
            if (FetcherUtilities.contentIsText(res)) {
                return (EPICResponse) FetcherUtilities.toEPICText(res);
            } else {
                return (EPICResponse) FetcherUtilities.toEPICBinary(res);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Exception in PUT:{0}", ex);
            return new EPICResponse();
        }
    }

    /**
     * Handle HTTP DELETE request.
     *
     * @param req EPICRequest containing HTTP DELETE request.
     * @return Response for the handled request or empty response.
     */
    private EPICResponse handleDelete(EPICRequest req) {
        try {
            LOGGER.log(Level.INFO, "DELETE Request \"" + req.getUri() +"\"");
            HttpDelete delete = new HttpDelete(req.getUri());
            FetcherUtilities.copyHeaders(req, delete);
            HttpResponse res = httpclient.execute(delete);
            if (FetcherUtilities.contentIsText(res)) {
                return (EPICResponse) FetcherUtilities.toEPICText(res);
            } else {
                return (EPICResponse) FetcherUtilities.toEPICBinary(res);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Exception in DELETE:{0}", ex);
            return new EPICResponse();
        }
    }

    /**
     * Handle HTTP HEAD request.
     *
     * @param req EPICRequest containing HTTP HEAD request.
     * @return Response for the handled request or empty response.
     */
    private EPICResponse handleHead(EPICRequest req) {
        try {
            LOGGER.log(Level.INFO, "HEAD Request \"" + req.getUri() +"\"");
            HttpHead head = new HttpHead(req.getUri());
            FetcherUtilities.copyHeaders(req, head);
            HttpResponse res = httpclient.execute(head);
            if (FetcherUtilities.contentIsText(res)) {
                return (EPICResponse) FetcherUtilities.toEPICText(res);
            } else {
                return (EPICResponse) FetcherUtilities.toEPICBinary(res);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Exception in HEAD:{0}", ex);
            return new EPICResponse();
        }
    }
}
