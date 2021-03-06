package fi.silverskin.secureproxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EPICTomcat {

    private ProxyController proxy;
    private static final Logger LOGGER = 
            Logger.getLogger(EPICTomcat.class.getName());

    public EPICTomcat() throws URISyntaxException {
        proxy = new ProxyController();
    }

    /**
     * Handles requests coming from servlet.
     *
     * @param request HTTP request coming from servlet.
     * @param response HTTP response coming from servlet.
     */
    public void handleRequest(HttpServletRequest request, 
                              HttpServletResponse response) {
        LOGGER.entering(EPICTomcat.class.getName(), 
                        "handleRequest", new Object[] {request, response});
        EPICRequest convertedRequest = convertToEPICRequest(request);
        
        try {
            EPICResponse epic = proxy.handleRequest(convertedRequest);
            
            fillResponse(response, epic);
        } catch (EPICException e) {
            try {
                response.sendError(404);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        
        LOGGER.exiting(EPICTomcat.class.getName(), "handleRequest");
    }

    /**
     * Fills HttpServletResponse with data from EPICResponse.
     *
     * @param response HttpServeltResponse used by tomcat.
     * @param epic Internal response used by SecureProxy.
     */
    private void fillResponse(HttpServletResponse response, EPICResponse epic) {
        LOGGER.entering(EPICTomcat.class.getName(), 
                        "fillResponse", new Object[] {response, epic});

        response.setStatus(epic.getStatusCode());

        try {
            for (Map.Entry<String, String> header : epic.getHeaders().entrySet()) {
                response.addHeader(header.getKey(), header.getValue());
            }
        } catch (IllegalStateException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        LOGGER.log(Level.INFO, "epic class: {0}", epic.getClass().getName());

        if (epic.isText()) {
            fillText(response, (EPICTextResponse) epic);
        } else {
            fillBinary(response, (EPICBinaryResponse) epic);
        }

        LOGGER.exiting(EPICTomcat.class.getName(), "fillResponse");
    }

    /**
     * Fills HttpServletResponse with text data from EPICResponse
     *
     * @param response HttpServeltResponse used by tomcat.
     * @param epic Internal response used by SecureProxy.
     */
    private void fillText(HttpServletResponse response, EPICTextResponse epic) {
        LOGGER.entering(EPICTomcat.class.getName(), 
                        "fillText", new Object[] {response, epic});
        
        try {
            PrintWriter out = response.getWriter();
            out.print(epic.getBody());
            out.flush();
            out.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        LOGGER.entering(EPICTomcat.class.getName(), "fillText");
    }

    /**
     * Fills HttpServletResponse with binary data from EPICResponse.
     *
     * @param response HttpServeltResponse used by tomcat.
     * @param epic Internal response used by SecureProxy.
     */
    private void fillBinary(HttpServletResponse response, EPICBinaryResponse epic) {
        LOGGER.entering(EPICTomcat.class.getName(), 
                        "fillBinary", new Object[] {response, epic});
        
        try {
            ServletOutputStream out = response.getOutputStream();
            out.write(epic.getBody());
            out.flush();
            out.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        LOGGER.entering(EPICTomcat.class.getName(), "fillBinary");
    }

    /**
     * Converts HttpServletRequest to EPICRequest.
     *
     * @param request HTTP request coming from servlet.
     * @return HTTP request converted to internal EPICRequest.
     */
    private EPICRequest convertToEPICRequest(HttpServletRequest request) {
        LOGGER.entering(EPICTomcat.class.getName(), 
                        "convertToEPICRequest", request);
        HashMap<String, String> headers = new HashMap();
        String body = new String();
        
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);

            headers.put(name, value);
        }

        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();

            char[] buffer = new char[0x10000];
            int length;

            while ((length = reader.read(buffer, 0, buffer.length)) != -1) {
                sb.append(buffer, 0, length);
            }

            reader.close();
            body = sb.toString();
        } catch (java.io.UnsupportedEncodingException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (java.io.IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        EPICRequest e = new EPICRequest(request.getMethod(), headers, body);
        if (request.getQueryString() != null) {
            e.setUri(request.getRequestURL() + "?" + request.getQueryString());
        } else {
            e.setUri(request.getRequestURL().toString());
        }

        LOGGER.exiting(EPICTomcat.class.getName(), "convertToEPICRequest", e);
        return e;
    }
}
