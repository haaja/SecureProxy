/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy.servlet;

import fi.silverskin.secureproxy.SecureProxy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author orva
 */
public class RequestHandler extends HttpServlet {
    
    private SecureProxy proxy;
    
    
    public RequestHandler() {
        proxy = new SecureProxy();
    }
    
    


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        proxy.handleGet(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        proxy.handlePost(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        proxy.handleDelete(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        proxy.handlePut(request, response);
    }

    @Override
    protected void doHead(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        proxy.handleHead(request, response);
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        proxy.handleOptions(request, response);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doTrace(req, resp);
    }
    

    


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    
    
    
    // These methods are just for demonstrating how to handle request data.
    
    private void printHeaders(PrintWriter out, HttpServletRequest req) {
        Enumeration<String> headerNames = req.getHeaderNames();
        out.println("<h1> Request headers were: </h1>");
        out.print("<ul>");
        while (headerNames.hasMoreElements()) {
            out.print("<li>");
            String header = headerNames.nextElement();
            Enumeration<String> values = req.getHeaders(header);
            out.print(header + ":");
            if (values != null) {
                while (values.hasMoreElements()) {
                    out.print(" " + values.nextElement());
                }
            }
            out.print("</li>");
        }
        out.print("</ul>");
    }

    private void printBody(PrintWriter out, HttpServletRequest req) throws IOException {
        BufferedReader in = req.getReader();
        try {
            if (in.ready()) {
                out.println("<h1> Request body was: </h1>");
                while (in.ready()) {
                    out.println(in.readLine());
                }
            }
        } finally {
            in.close();
        }
    }

    private void printQuery(PrintWriter out, HttpServletRequest req) {
        String query = req.getQueryString();
        if (query != null) {
            out.println("<h1> Query string was: </h1>");
            out.println(query);
        }
    }
    
    
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RequestHandler</title>");
            out.println("</head>");
            out.println("<body>");
            printHeaders(out, request);
            printQuery(out, request);
            // printBody(out, request);
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }    
}
