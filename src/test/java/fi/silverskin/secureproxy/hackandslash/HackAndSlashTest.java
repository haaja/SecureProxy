/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy.hackandslash;

import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICTextResponse;
import fi.silverskin.secureproxy.ProxyConfigurer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.*;

/**
 *
 * @author peltoel
 */
public class HackAndSlashTest {
    
    private HackAndSlash has;
    private EPICRequest request;
    private EPICTextResponse textResponse;

    // Tests will fail if privateURI and publicURI don't start http://
    // please check the file properties.config
    private String privateURI;
    private String privatePort;
    private String publicURI;
    
    private HackAndSlashConfig conf;
    
    public HackAndSlashTest() throws URISyntaxException {
        ProxyConfigurer configurer = new ProxyConfigurer("config.properties");
        privateURI = configurer.getProperty("privateURI");
        privatePort = configurer.getProperty("privatePort");
        publicURI = configurer.getProperty("publicURI");
        conf = new HackAndSlashConfig(privateURI, privatePort, publicURI);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        HashMap<String, String> headers = new HashMap<String, String>();        
        String body = "";
        
        this.has = new HackAndSlash(conf);
        this.request = new EPICRequest("get", headers, body);
        this.textResponse = new EPICTextResponse(headers, body);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of hackAndSlashIn method, of class HackAndSlash.
     */
    @Test
    public void testHackAndSlashIn_EPICRequest() {
        String testUri = "http://www.cs.helsinki.fi/opiskelu";
        this.request.setUri(testUri);
        
        try { 
            URI path = new URI(testUri);
             String controlUrl = privateURI + ":" + privatePort + 
                path.getPath();
        
            EPICRequest resultRequest = has.hackAndSlashIn(this.request);
            assertEquals(controlUrl, resultRequest.getUri());
        } catch (URISyntaxException ex) { 
            fail(); 
        }
    }

    /**
     * Test of hackAndSlashOut method, of class HackAndSlash.
     */
    @Test
    public void testHackAndSlashOut_EPICTextResponse() {
        // under processing
        this.textResponse.setBody(
                "<html> <head> <style type=\"text/css\">"
                + "body {background-image:url(\"" +
                privateURI + ":" + privatePort + "\");}"
                + "</style> </head> <body>"
                + "<p> This is a test case: </p>"
                + "<a href=\"" + privateURI + ":" + privatePort + "\">"
                + "</a></body></html>"
                );
        String controlBody =
                
                "<html> <head> <style type=\"text/css\">"
                + "body {background-image:url(\"" + publicURI + "\");}"
                + "</style> </head> <body>"
                + "<p> This is a test case: </p>"
                + "<a href=\"" + publicURI + "\">"
                + "</a></body></html>";
        EPICTextResponse resultResponse = has.hackAndSlashOut(textResponse); 
        System.out.println(resultResponse.getBody());
        System.out.println("\n"+controlBody);
        assertEquals(controlBody, resultResponse.getBody());
    }

    /**
     * Test of getMaskedUrl method when url is own, of class HackAndSlash.
     */
    @Test
    public void testGetMaskedUrl_ownUrl() {
        // Under processing
        String expectedUrl = publicURI + "/test/testing.html";
        String testUrl = privateURI + ":" + privatePort + "/test/testing.html"; 
        String resultUrl = has.getMaskedUrl(testUrl);
        assertEquals(expectedUrl, resultUrl);
    }
    
    /**
     * Test of getMaskedUrl method when url isn't own, of class HackAndSlash.
     */
    @Test
    public void testGetMaskedUrl_notOwnUrl() {
        String foreignUrl = "http://www.google.com";
        String resultUrl = has.getMaskedUrl(foreignUrl);
        assertEquals(foreignUrl, resultUrl);
    }

    /**
     * Test of convertUrlInTag method, of class HackAndSlash.
     */
    @Test
    public void testConvertUrlInTag() {
        String tag = "<a href=\"http://128.214.9.12:80/testing\">Boo</a>";
        String attribute = "href";
        String resultTag = has.convertUrlInTag(tag, attribute);
        String controlTag = "<a href=\"http://palomuuri.users.cs.helsinki.fi/testing\">Boo</a>";
        assertEquals(controlTag, resultTag);
    }
}
