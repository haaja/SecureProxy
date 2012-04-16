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
    private String privateHttpPort;
    private String privateHttpsPort;
    private String publicURI;
    private String publicHttpPort;
    private String publicHttpsPort;
    
    private HackAndSlashConfig conf;
    
    public HackAndSlashTest() throws URISyntaxException {
        ProxyConfigurer configurer = new ProxyConfigurer("config.properties");
        privateURI = configurer.getProperty("privateURI");
        privateHttpPort = configurer.getProperty("privateHttpPort");
        privateHttpsPort = configurer.getProperty("privateHttpsPort");
        publicURI = configurer.getProperty("publicURI");
        publicHttpPort = configurer.getProperty("publicHttpPort");
        publicHttpsPort = configurer.getProperty("publicHttpsPort");
        
        conf = new HackAndSlashConfig(privateURI,
                                      privateHttpPort,
                                      privateHttpsPort,
                                      publicURI,
                                      publicHttpPort,
                                      publicHttpsPort);
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
            String controlUrl = privateURI + ":" + privateHttpPort +
                                path.getPath();
        
            EPICRequest resultRequest = has.hackAndSlashIn(this.request);
            assertEquals(controlUrl, resultRequest.getUri().toString());
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
                privateURI + ":" + privateHttpPort + "\");}"
                + "</style> </head> <body>"
                + "<p> This is a test case: </p>"
                + "<a href=\"" + privateURI + ":" + privateHttpPort + "\">"
                + /*privateURI + ":" + privateHttpPort + */"</a></body></html>"
                );
        
        String controlBody =                
                "<html> <head> <style type=\"text/css\">"
                + "body {background-image:url(\"" + publicURI + ":" + publicHttpPort + "\");}"
                + "</style> </head> <body>"
                + "<p> This is a test case: </p>"
                + "<a href=\"" + publicURI + ":" + publicHttpPort + "\">"
                + /*publicURI + ":" + publicHttpPort + */"</a></body></html>";
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
        String expectedUrl = publicURI + ":" + publicHttpPort + "/test/testing.html";
        String testUrl = privateURI + ":" + privateHttpPort + "/test/testing.html";
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

    @Test
    public void testGetMaskedUrl_relativeUrl() {
        String expectedUrl = "/music/index.html";
        String testUrl = "/music/index.html";
        assertEquals(expectedUrl, has.getMaskedUrl(testUrl));
    }

    /**
     * Test of convertUrlInTag method, of class HackAndSlash.
     */
    @Test
    public void testConvertUrlInTag() {
        String tag = "<a href=\"http://128.214.9.12:80/testing\">Boo</a>";
        String attribute = "href";
        String resultTag = has.convertUrlInTag(tag, attribute);
        String expectedTag = "<a href=\"http://palomuuri.users.cs.helsinki.fi:"+publicHttpPort+"/testing\">Boo</a>";
        assertEquals(expectedTag, resultTag);
    }
}
