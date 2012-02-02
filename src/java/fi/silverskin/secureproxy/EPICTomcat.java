package fi.silverskin.secureproxy;

import java.io.BufferedReader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EPICTomcat {
    private ProxyController proxy;

    
    public EPICTomcat() {
        this.proxy = new ProxyController();
    }
    
    public void handleGet(HttpServletRequest request, HttpServletResponse response) {
        EPICRequest convertedRequest = convertToEPICRequest(request);
        EPICResponse convertedResponse = convertToEPICResponse(response);
        
        this.proxy.handleGet(convertedRequest, convertedResponse);

    }

    public void handlePost(HttpServletRequest request, HttpServletResponse response) {
        EPICRequest convertedRequest = convertToEPICRequest(request);
        EPICResponse convertedResponse = convertToEPICResponse(response);
        
        this.proxy.handlePost(convertedRequest, convertedResponse);
    }

    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        EPICRequest convertedRequest = convertToEPICRequest(request);
        EPICResponse convertedResponse = convertToEPICResponse(response);
        
        this.proxy.handleDelete(convertedRequest, convertedResponse);
    }

    public void handlePut(HttpServletRequest request, HttpServletResponse response) {
        EPICRequest convertedRequest = convertToEPICRequest(request);
        EPICResponse convertedResponse = convertToEPICResponse(response);
        
        this.proxy.handlePut(convertedRequest, convertedResponse);
    }

    public void handleHead(HttpServletRequest request, HttpServletResponse response) {
        EPICRequest convertedRequest = convertToEPICRequest(request);
        EPICResponse convertedResponse = convertToEPICResponse(response);
        
        this.proxy.handleHead(convertedRequest, convertedResponse);
    }

    public void handleOptions(HttpServletRequest request, HttpServletResponse response) {
        EPICRequest convertedRequest = convertToEPICRequest(request);
        EPICResponse convertedResponse = convertToEPICResponse(response);
        
        this.proxy.handleOptions(convertedRequest, convertedResponse);
    }
    
    public EPICRequest convertToEPICRequest(HttpServletRequest request) {
        HashMap<String, String> headers = new HashMap();
        String body = new String();
        EPICRequest convertedRequest = new EPICRequest();
        
        Enumeration<String> headerNames = request.getHeaderNames();
        
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            
            headers.put(name, value);    
        }
        
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            
            String line = reader.readLine();
            while (line != null) {
                sb.append(line + "\n");
                line = reader.readLine();
            }
            
            reader.close();
            body = sb.toString();
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        
        convertedRequest.setBody(body);
        convertedRequest.setHeaders(headers);

        return convertedRequest;
    }
    
    public EPICResponse convertToEPICResponse(HttpServletResponse response) {
        HashMap<String, String> headers = new HashMap();
        String body = new String();
        EPICResponse convertedResponse = new EPICResponse();
        
        Collection<String> headerNames = response.getHeaderNames();
        Iterator headerIterator = headerNames.iterator();
        
        while (headerIterator.hasNext()) {
            String name = (String)headerIterator.next();
            String value = response.getHeader(name);
            
            headers.put(name, value);    
        }
        
        convertedResponse.setHeaders(headers);
        convertedResponse.setBody(null);
        
        return convertedResponse;
    }
}
