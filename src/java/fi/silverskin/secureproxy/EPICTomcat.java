package fi.silverskin.secureproxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EPICTomcat {

    private ProxyController proxy;

    
    public EPICTomcat() {
        proxy = new ProxyController();
    }
    

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        EPICRequest convertedRequest = convertToEPICRequest(request);
        EPICResponse epic = proxy.handleRequest(convertedRequest);

        fillResponse(response, epic);
    }

    private void fillResponse(HttpServletResponse response, EPICResponse epic) {

        try {
            response.reset();

            for (Map.Entry<String, String> header : epic.getHeaders().entrySet()) {
                response.addHeader(header.getKey(), header.getValue());
            }
        } catch (IllegalStateException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }

        String contentType = epic.getHeaders().get("Content-Type");
        if (contentType == null) {
            contentType = "text/text";
        }

        Pattern pattern = Pattern.compile("text/.*");
        Matcher isText = pattern.matcher(contentType);

        // if text
        if (isText.matches()) {
            try {
                PrintWriter out = response.getWriter();
                out.print(epic.getBody());
                out.flush();
                out.close();
            } catch (IOException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            }

        }
        // if binary
        else {
            try {
                ServletOutputStream out = response.getOutputStream();                
                out.print(epic.getBody());
                out.flush();
                out.close();
            } catch (IOException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
    }

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
        } catch (java.io.UnsupportedEncodingException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        } catch (IllegalStateException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        } catch (java.io.IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
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
