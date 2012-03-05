/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy.hackandslash;

import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICResponse;
import fi.silverskin.secureproxy.EPICTextResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author peltoel
 */
public class HackAndSlashTest {
    
    private HackAndSlash has;
    private EPICRequest request;
    private EPICTextResponse response;

    private HackAndSlashConfig conf;

    // From the class. Replace if modified.
    private String remoteUrl = "tkt_palo.users.cs.helsinki.fi";
    private String remotePort = "80";
    private String basePseudoURI = "http://palomuuri.users.cs.helsinki.fi";
    
    public HackAndSlashTest() throws URISyntaxException {
        conf = new HackAndSlashConfig(remoteUrl, remotePort, basePseudoURI);
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
        this.response = new EPICTextResponse(headers, body);
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
            URI forPath = new URI(testUri);
             String controlUrl = "http://" + remoteUrl + ":" + remotePort + 
                forPath.getPath();
        
            EPICRequest testRequest = has.hackAndSlashIn(this.request);
            assertEquals(controlUrl, testRequest.getUri());
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
        this.response.setBody(
                "<html> <body>"
                + "<p> This is a test case: </p>"
                + "<a href=" + remoteUrl + ":" + remotePort + ">"
                + "</a></body></html>"
                );
        String controlBody =
                "<html> <body>"
                + "<p> This is a test case: </p>"
                + "<a href=" + basePseudoURI + ">"
                + "</a></body></html>";
        EPICTextResponse testResponse = (EPICTextResponse)has.hackAndSlashOut(response); 
        assertEquals(controlBody, testResponse.getBody());
    }

    /**
     * Test of getPseudoUrl method when url is own, of class HackAndSlash.
     */
    @Test
    public void testGetPseudoUrl_ownUrl() {
        // Under processing
        String ownUrl = "http://" + basePseudoURI + "/test/testing.html";
        String testUrl = has.getPseudoUrl(ownUrl);
        assertEquals(ownUrl, testUrl);
    }
    
    /**
     * Test of getPseudoUrl method when url isn't own, of class HackAndSlash.
     */
    @Test
    public void testGetPseudoUrl_notOwnUrl() {
        String foreignUrl = "http://www.google.com";
        String testUrl = has.getPseudoUrl(foreignUrl);
        assertEquals(foreignUrl, testUrl);
    }

    /**
     * Test of convertUrlInTag method, of class HackAndSlash.
     */
    @Test
    public void testConvertUrlInTag() {
        fail("The test case is a prototype.");
    }
}
