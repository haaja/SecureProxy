/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy;

import fi.silverskin.secureproxy.resourcefetcher.FetcherUtilities;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author orva
 */
public class FetcherUtilitiesTest {
    
    private EPICRequest epic;
    private final String bodyVal = "body";
    
    public FetcherUtilitiesTest() {
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
        headers.put("HEADER_1", "value 1");
        headers.put("HEADER_2", "value 2");
        
        this.epic = new EPICRequest("get", headers, bodyVal);        
    }
    
    @After
    public void tearDown() {
    }


    /**
     * Test of getBody method, of class EPICUtilities.
     */
    @Test
    public void testGetBody() throws UnsupportedEncodingException {
        HttpEntity e = new StringEntity("Hola");
        assertEquals(FetcherUtilities.getBody(e), "Hola");
    }

    /**
     * Test of copyHeaders method, of class EPICUtilities.
     */
    @Test
    public void testCopyHeaders() {
        HttpGet req = new HttpGet("http://google.com");
        FetcherUtilities.copyHeaders(epic, req);
        
        assertEquals(1, req.getHeaders("HEADER_1").length);
        assertEquals(1, req.getHeaders("HEADER_2").length);
        assertEquals("value 1", req.getFirstHeader("HEADER_1").getValue());
        
        req = new HttpGet("http://google.com");
        req.setHeader("ADDED_HEADER", "added header value");
        FetcherUtilities.copyHeaders(epic, req);
        assertEquals(3, req.getAllHeaders().length);
        
    }

    /**
     * Test of copyBody method, of class EPICUtilities.
     */
    @Test
    public void testCopyBody() {
        HttpPost req = new HttpPost("http://google.com"); 
        FetcherUtilities.copyBody(epic, req);
        
        assertEquals(bodyVal, FetcherUtilities.getBody(req.getEntity()));
    }
}
