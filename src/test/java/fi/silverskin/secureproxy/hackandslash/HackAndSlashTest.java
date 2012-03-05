/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy.hackandslash;

import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICTextResponse;
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

    private HackAndSlashConfig conf;

    // From the class. Replace if modified.    private EPICRequest request;
    //private String privateURI = "tkt_palo.users.cs.helsinki.fi";
    private String privateURI = "http://128.214.9.12";
    private String privatePort = "80";
    private String publicURI = "http://palomuuri.users.cs.helsinki.fi";
    
    public HackAndSlashTest() throws URISyntaxException {
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
                "<html> <body>"
                + "<p> This is a test case: </p>"
                + "<a href=\"" + privateURI + ":" + privatePort + "\">"
                + "</a></body></html>"
                );
        String controlBody =
                "<html> <body>"
                + "<p> This is a test case: </p>"
                + "<a href=\"" + publicURI + "\">"
                + "</a></body></html>";
        EPICTextResponse resultResponse = has.hackAndSlashOut(textResponse); 
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
