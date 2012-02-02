package fi.silverskin.secureproxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.*;


public class EPICTomcatTest {
    
    public EPICTomcatTest() {
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
     * Test of handleGet method, of class EPICTomcat.
     */
    @Test
    public void testHandleGet() {
        System.out.println("handleGet");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        EPICTomcat instance = new EPICTomcat();
        instance.handleGet(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handlePost method, of class EPICTomcat.
     */
    @Test
    public void testHandlePost() {
        System.out.println("handlePost");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        EPICTomcat instance = new EPICTomcat();
        instance.handlePost(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleDelete method, of class EPICTomcat.
     */
    @Test
    public void testHandleDelete() {
        System.out.println("handleDelete");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        EPICTomcat instance = new EPICTomcat();
        instance.handleDelete(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handlePut method, of class EPICTomcat.
     */
    @Test
    public void testHandlePut() {
        System.out.println("handlePut");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        EPICTomcat instance = new EPICTomcat();
        instance.handlePut(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleHead method, of class EPICTomcat.
     */
    @Test
    public void testHandleHead() {
        System.out.println("handleHead");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        EPICTomcat instance = new EPICTomcat();
        instance.handleHead(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleOptions method, of class EPICTomcat.
     */
    @Test
    public void testHandleOptions() {
        System.out.println("handleOptions");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        EPICTomcat instance = new EPICTomcat();
        instance.handleOptions(request, response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of convertToEPICRequest method, of class EPICTomcat.
     */
    @Test
    public void testConvertToEPICRequest() {
        System.out.println("convertToEPICRequest");
        HttpServletRequest request = null;
        EPICTomcat instance = new EPICTomcat();
        EPICRequest expResult = null;
        EPICRequest result = instance.convertToEPICRequest(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of convertToEPICResponse method, of class EPICTomcat.
     */
    @Test
    public void testConvertToEPICResponse() {
        System.out.println("convertToEPICResponse");
        HttpServletResponse response = null;
        EPICTomcat instance = new EPICTomcat();
        EPICResponse expResult = null;
        EPICResponse result = instance.convertToEPICResponse(response);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
