/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy;

import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author peltoel
 */
public class EPICTextResponseTest {
    
    public EPICTextResponseTest() {
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
     * Test of getBody method, of class EPICTextResponse.
     */
    @Test
    public void testGetBody() {
        HashMap <String, String> headers = new HashMap <String, String>();
        String body = "Some test body";
        EPICTextResponse response = new EPICTextResponse(headers, body);
        assertEquals(body, response.getBody());
    }

    /**
     * Test of setBody method, of class EPICTextResponse.
     */
    @Test
    public void testSetBody() {
        HashMap <String, String> headers = new HashMap <String, String>();
        String body = "Some test body";
        EPICTextResponse response = new EPICTextResponse(headers, body);
        String testBody = "Another test body";
        response.setBody(testBody);
        assertEquals(testBody, response.getBody());
    }

    /**
     * Test of toString method, of class EPICTextResponse.
     */
    @Test
    public void testToString() {
        HashMap <String, String> headers = new HashMap <String, String>();
        headers.put("HEADER_1", "value 1");
        headers.put("HEADER_2", "value 2");
        String body = "Test case body";
        EPICTextResponse response = new EPICTextResponse(headers, body);
        String controlCase = "Headers:\n"
                + "\tHEADER_1:value 1\n"
                + "\tHEADER_2:value 2\n"
                + "Body:\n"
                + "Test case body"
                ;
        assertEquals(controlCase, response.toString());
    }
}
