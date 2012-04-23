package fi.silverskin.secureproxy.servlet;

import fi.silverskin.secureproxy.EPICTomcat;
import fi.silverskin.secureproxy.ProxyLogger;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class RequestHandler extends HttpServlet {
    
    private EPICTomcat tomcatHandler;
    private static final Logger LOGGER = 
            Logger.getLogger(RequestHandler.class.getName(), null);
    
    public RequestHandler() throws URISyntaxException {
        tomcatHandler = new EPICTomcat();

        //initialize logger settings for SecureProxy
        ProxyLogger.setup();
    }

    /**
     * Handles HTTP GET requests.
     * 
     * @param request HTTP request coming from the client.
     * @param response HTTP response to be sent to the client.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOGGER.info("Received HTTP GET request.");
        tomcatHandler.handleRequest(request, response);
    }

    /**
     * Handles HTTP POST requests.
     *
     * @param request HTTP request coming from the client.
     * @param response HTTP response to be sent to the client.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOGGER.info("Received HTTP POST request.");
        tomcatHandler.handleRequest(request, response);
    }

    /**
     * Handles HTTP DELETE requests.
     *
     * @param request HTTP request coming from the client.
     * @param response HTTP response to be sent to the client.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        LOGGER.info("Received HTTP DELETE request.");
        tomcatHandler.handleRequest(request, response);
    }

    /**
     * Handles HTTP PUT requests.
     *
     * @param request HTTP request coming from the client.
     * @param response HTTP response to be sent to the client.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOGGER.info("Received HTTP DELETE request.");
        tomcatHandler.handleRequest(request, response);
    }

    /**
     * Handles HTTP HEAD requests.
     *
     * @param request HTTP request coming from the client.
     * @param response HTTP response to be sent to the client.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doHead(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        LOGGER.info("Received HTTP HEAD request.");
        tomcatHandler.handleRequest(request, response);
    }

    /**
     * Handles HTTP OPTIONS requests.
     * 
     * @param request HTTP request coming from the client.
     * @param response HTTP response to be sent to the client.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        LOGGER.info("Received HTTP OPTIONS request.");
        tomcatHandler.handleRequest(request, response);
    }

    /**
     * Handles HTTP TRACE requests.
     *
     * @param req HTTP request coming from the client.
     * @param resp HTTP response to be sent to the client.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        LOGGER.info("Received HTTP TRACE request.");
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

}

