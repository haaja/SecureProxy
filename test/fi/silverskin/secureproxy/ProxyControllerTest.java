/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author orva
 */
public class ProxyControllerTest {
    
    public ProxyControllerTest() {
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
     * Test of handleGet method, of class ProxyController.
     */
    @Test
    public void testHandleGet() {
        System.out.println("handleGet");
        EPICRequest request = null;
        EPICResponse response = null;
        ProxyController instance = new ProxyController();
        instance.handleGet(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handlePost method, of class ProxyController.
     */
    @Test
    public void testHandlePost() {
        System.out.println("handlePost");
        EPICRequest request = null;
        EPICResponse response = null;
        ProxyController instance = new ProxyController();
        instance.handlePost(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleDelete method, of class ProxyController.
     */
    @Test
    public void testHandleDelete() {
        System.out.println("handleDelete");
        EPICRequest request = null;
        EPICResponse response = null;
        ProxyController instance = new ProxyController();
        instance.handleDelete(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handlePut method, of class ProxyController.
     */
    @Test
    public void testHandlePut() {
        System.out.println("handlePut");
        EPICRequest request = null;
        EPICResponse response = null;
        ProxyController instance = new ProxyController();
        instance.handlePut(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleHead method, of class ProxyController.
     */
    @Test
    public void testHandleHead() {
        System.out.println("handleHead");
        EPICRequest request = null;
        EPICResponse response = null;
        ProxyController instance = new ProxyController();
        instance.handleHead(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleOptions method, of class ProxyController.
     */
    @Test
    public void testHandleOptions() {
        System.out.println("handleOptions");
        EPICRequest request = null;
        EPICResponse response = null;
        ProxyController instance = new ProxyController();
        instance.handleOptions(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
