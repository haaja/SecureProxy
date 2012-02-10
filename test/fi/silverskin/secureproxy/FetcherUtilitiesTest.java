/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy;

import fi.silverskin.secureproxy.resourcefetcher.FetcherUtilities;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.*;

/**
 *
 * @author orva
 */
public class FetcherUtilitiesTest {
    
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
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of responseToEPICResponse method, of class EPICUtilities.
     */
    @Test
    public void testResponseToEPICResponse() {
        System.out.println("responseToEPICResponse");
        HttpResponse e = null;
        EPICResponse expResult = null;
        EPICResponse result = FetcherUtilities.responseToEPICResponse(e);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBody method, of class EPICUtilities.
     */
    @Test
    public void testGetBody() {
        System.out.println("getBody");
        HttpEntity e = null;
        String expResult = "";
        String result = FetcherUtilities.getBody(e);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of copyHeaders method, of class EPICUtilities.
     */
    @Test
    public void testCopyHeaders() {
        System.out.println("copyHeaders");
        EPICRequest epic = null;
        HttpRequest req = null;
        FetcherUtilities.copyHeaders(epic, req);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of copyBody method, of class EPICUtilities.
     */
    @Test
    public void testCopyBody() {
        System.out.println("copyBody");
        EPICRequest epic = null;
        HttpEntityEnclosingRequestBase req = null;
        FetcherUtilities.copyBody(epic, req);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
