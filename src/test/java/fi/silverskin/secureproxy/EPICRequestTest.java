/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy;

import fi.silverskin.secureproxy.EPICRequest.RequestType;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author peltoel
 */
public class EPICRequestTest {
    
    public EPICRequestTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getBody method, of class EPICRequest.
     */
    @Test
    public void testGetBody() {
        HashMap <String, String> headers = new HashMap <String, String>();
        String body = "Test case body";
        EPICRequest request = new EPICRequest(RequestType.POST, headers, body);
        assertEquals(body, request.getBody());
    }

    /**
     * Test of setBody method, of class EPICRequest.
     */
    @Test
    public void testSetBody() {
        HashMap <String, String> headers = new HashMap <String, String>();
        String body = "Test case body";
        EPICRequest request = new EPICRequest(RequestType.POST, headers, body);
        String testBody = "Different kind of body";
        request.setBody(testBody);
        assertEquals(testBody, request.getBody());
    }

    /**
     * Test of getType method, of class EPICRequest.
     */
    @Test
    public void testGetType() {
        EPICRequest request = new EPICRequest(RequestType.GET);
        assertEquals(RequestType.GET, request.getType());
    }

    /**
     * Test of toString method, of class EPICRequest.
     */
    @Test
    public void testToString() {
        HashMap <String, String> headers = new HashMap <String, String>();
        headers.put("HEADER_1", "value 1");
        headers.put("HEADER_2", "value 2");
        String body = "Test case body";
        EPICRequest request = new EPICRequest(RequestType.POST, headers, body);
        request.setUri("http://www.cs.helsinki.fi");
        String controlCase = "Type: POST\n"
                + "URI : http://www.cs.helsinki.fi\n"
                + "Headers:\n"
                + "\tHEADER_1:value 1\n"
                + "\tHEADER_2:value 2\n"
                + "Body:\n"
                + "Test case body"
                ;
        assertEquals(controlCase, request.toString());
    }

    /**
     * Test of getUri method, of class EPICAbstraction.
     */
    @Test
    public void testGetUri() {
        HashMap <String, String> headers = new HashMap <String, String>();
        headers.put("HEADER_1", "value 1");
        headers.put("HEADER_2", "value 2");
        String body = "Test case body";
        String uri = "http://www.cs.helsinki.fi/opiskelu";
        EPICRequest request = new EPICRequest(RequestType.GET, headers, body);
        request.setUri(uri);
        assertEquals(uri, request.getUri().toString());
    }

    /**
     * Test of setUri method, of class EPICAbstraction.
     */
    @Test
    public void testSetUri() {
        HashMap <String, String> headers = new HashMap <String, String>();
        headers.put("HEADER_1", "value 1");
        headers.put("HEADER_2", "value 2");
        String body = "Test case body";
        String uri = "http://www.cs.helsinki.fi/opiskelu";
        EPICRequest request = new EPICRequest(RequestType.POST,
                                                  headers,
                                                  body);
        request.setUri(uri);
        assertEquals(uri, request.getUri().toString());
    }
}