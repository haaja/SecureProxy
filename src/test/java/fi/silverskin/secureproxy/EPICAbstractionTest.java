/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy;

import java.util.HashMap;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author peltoel
 */
public class EPICAbstractionTest {
    
    public EPICAbstractionTest() {
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
     * Test of getHeaders method, of class EPICAbstraction.
     */
    @Test
    public void testGetHeaders() {
        HashMap <String, String> headers = new HashMap <String, String>();
        headers.put("HEADER_1", "value 1");
        headers.put("HEADER_2", "value 2");
        EPICAbstraction abstraction = new EPICAbstractionImpl(headers);
        assertEquals(headers, abstraction.getHeaders());
    }

    /**
     * Test of setHeaders method, of class EPICAbstraction.
     */
    @Test
    public void testSetHeaders() {
        HashMap <String, String> headers = new HashMap <String, String>();
        HashMap <String, String> controlHeaders = new HashMap <String, String>();
        controlHeaders.put("HEADER_1", "value 1");
        controlHeaders.put("HEADER_2", "value 2");
        EPICAbstraction abstraction = new EPICAbstractionImpl(headers);
        abstraction.setHeaders(controlHeaders);
        assertEquals(controlHeaders, abstraction.getHeaders());
    }

    /**
     * Test of getUri method, of class EPICAbstraction.
     */
    @Test
    public void testGetUri() {
        String uri = "http://www.cs.helsinki.fi/opiskelu";
        EPICAbstraction abstraction = new EPICAbstractionImpl();
        abstraction.setUri(uri);
        assertEquals(uri, abstraction.getUri().toString());
    }

    /**
     * Test of setUri method, of class EPICAbstraction.
     */
    @Test
    public void testSetUri() {
        String uri = "http://www.cs.helsinki.fi/opiskelu";
        EPICAbstraction abstraction = new EPICAbstractionImpl();
        abstraction.setUri(uri);
        assertEquals(uri, abstraction.getUri().toString());
    }

    public class EPICAbstractionImpl extends EPICAbstraction {
        public EPICAbstractionImpl(HashMap<String, String> headers) {
            this.headers = headers;
        }
        public EPICAbstractionImpl() {};
    }
}
