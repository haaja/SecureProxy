/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author orva
 */
public class SecureProxyTest {
    
    public SecureProxyTest() {
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
     * Test of handleGet method, of class SecureProxy.
     */
    @Test
    public void testHandleGet() {
        System.out.println("handleGet");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        SecureProxy instance = new SecureProxy();
        instance.handleGet(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handlePost method, of class SecureProxy.
     */
    @Test
    public void testHandlePost() {
        System.out.println("handlePost");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        SecureProxy instance = new SecureProxy();
        instance.handlePost(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleDelete method, of class SecureProxy.
     */
    @Test
    public void testHandleDelete() {
        System.out.println("handleDelete");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        SecureProxy instance = new SecureProxy();
        instance.handleDelete(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handlePut method, of class SecureProxy.
     */
    @Test
    public void testHandlePut() {
        System.out.println("handlePut");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        SecureProxy instance = new SecureProxy();
        instance.handlePut(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
