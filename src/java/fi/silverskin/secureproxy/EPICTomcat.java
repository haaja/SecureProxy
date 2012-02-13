package fi.silverskin.secureproxy;

import java.io.BufferedReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    
    private void fillResponse(HttpServletResponse response, EPICResponse r) {
        // TODO: extract data drom epicresonse and fill it into httpserletresponse
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

            char[] buffer = new char[4*1024];
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
        e.setUri(request.getRequestURL() + request.getQueryString());
        
        return e;
    }
}
