/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy;

import java.util.HashMap;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author peltoel
 */
public class EPICResponseTest {
    
    public EPICResponseTest() {
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
     * Test of isText method, of class EPICResponse.
     */
    @Test
    public void testIsText() {
        HashMap <String, String> headers1 = new HashMap <String, String>();
        headers1.put("Content-Type", "text/text");
        EPICResponse response1 = new EPICResponse(headers1);
        assertEquals(true, response1.isText());
        
        HashMap <String, String> headers2 = new HashMap <String, String>();
        headers2.put("Content-Type", null);
        EPICResponse response2 = new EPICResponse(headers2);
        assertEquals(true, response2.isText());
        
        HashMap <String, String> headers3 = new HashMap <String, String>();
        headers3.put("Content-Type", "something else");
        EPICResponse response3 = new EPICResponse(headers3);
        assertEquals(false, response3.isText());
    }
}
