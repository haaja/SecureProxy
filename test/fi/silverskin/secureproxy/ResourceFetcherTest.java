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
public class ResourceFetcherTest {
    
    public ResourceFetcherTest() {
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
     * Test of handleGet method, of class ResourceFetcher.
     */
    @Test
    public void testHandleGet() {
        System.out.println("handleGet");
        EPICRequest req = null;
        ResourceFetcher instance = new ResourceFetcher();
        EPICResponse expResult = null;
        EPICResponse result = instance.handleGet(req);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handlePost method, of class ResourceFetcher.
     */
    @Test
    public void testHandlePost() {
        System.out.println("handlePost");
        EPICRequest req = null;
        ResourceFetcher instance = new ResourceFetcher();
        EPICResponse expResult = null;
        EPICResponse result = instance.handlePost(req);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handlePut method, of class ResourceFetcher.
     */
    @Test
    public void testHandlePut() {
        System.out.println("handlePut");
        EPICRequest req = null;
        ResourceFetcher instance = new ResourceFetcher();
        EPICResponse expResult = null;
        EPICResponse result = instance.handlePut(req);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleDelete method, of class ResourceFetcher.
     */
    @Test
    public void testHandleDelete() {
        System.out.println("handleDelete");
        EPICRequest req = null;
        ResourceFetcher instance = new ResourceFetcher();
        EPICResponse expResult = null;
        EPICResponse result = instance.handleDelete(req);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleHead method, of class ResourceFetcher.
     */
    @Test
    public void testHandleHead() {
        System.out.println("handleHead");
        EPICRequest req = null;
        ResourceFetcher instance = new ResourceFetcher();
        EPICResponse expResult = null;
        EPICResponse result = instance.handleHead(req);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
