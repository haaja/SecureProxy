package fi.silverskin.secureproxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
    private static final Logger LOGGER = Logger.getLogger(EPICTomcat.class.getName());

    public EPICTomcat() {
        proxy = new ProxyController();
    }

    /**
     * Handles requests coming from servlet.
     *
     * @param request HTTP request coming from servlet.
     * @param response HTTP response coming from servlet.
     */
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        EPICRequest convertedRequest = convertToEPICRequest(request);
        EPICResponse epic = proxy.handleRequest(convertedRequest);

        fillResponse(response, epic);
    }

    /**
     * Fills HttpServletResponse with data from EPICResponse.
     * 
     * @param response HttpServeltResponse used by tomcat.
     * @param epic Internal response used by SecureProxy.
     */
    private void fillResponse(HttpServletResponse response, EPICResponse epic) {

        //logger.log(Level.INFO, "Headers before: {0}", response.getHeaderNames());        
        
        try {
            //response.reset();
            for (Map.Entry<String, String> header : epic.getHeaders().entrySet()) {
                response.addHeader(header.getKey(), header.getValue());
            }
        } catch (IllegalStateException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        if (epic.isText())
            fillText(response, (EPICTextResponse) epic);
        else 
            fillBinary(response, (EPICBinaryResponse) epic);
        //logger.log(Level.INFO, "Headers After: {0}", response.getHeaderNames());
    }

    /**
     * Fills HttpServletResponse with text data from EPICResponse
     * 
     * @param response HttpServeltResponse used by tomcat.
     * @param epic Internal response used by SecureProxy.
     */
    private void fillText(HttpServletResponse response, EPICTextResponse epic) {
        try {
            PrintWriter in = response.getWriter();
            in.print(epic.getBody());
            in.flush();
            in.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Fills HttpServletResponse with binary data from EPICResponse.
     * 
     * @param response HttpServeltResponse used by tomcat.
     * @param epic Internal response used by SecureProxy.
     */
    private void fillBinary(HttpServletResponse response, EPICBinaryResponse epic) {
        try {
            ServletOutputStream in = response.getOutputStream();
            byte[] data = epic.getBody();
            
            for (int i=0; i<data.length; i++)
                in.print(data[i]);
            
            in.flush();
            in.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Converts HttpServletRequest to EPICRequest.
     * 
     * @param request HTTP request coming from servlet.
     * @return HTTP request converted to internal EPICRequest.
     */
    private EPICRequest convertToEPICRequest(HttpServletRequest request) {
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
            e.setUri(request.getRequestURI());
        }
        return e;
    }
}
