/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.paramcheck;

import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICRequest.RequestType;
import java.net.URISyntaxException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.*;

/**
 *
 * @author peltoel
 */
public class ParamCheckTest {
    
    private ParamCheck check;
    
    public ParamCheckTest() {
        check = new ParamCheck();
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
     * Tests running plugin with EPICRequest GET.
     */
    @Test
    public void testValidGET() throws URISyntaxException {
        
        EPICRequest request = new EPICRequest(RequestType.GET);
        String url = "http://cs.helsinki.fi/opiskelu?nimi=kissa&luku=1500&kurssi=perusteet";
        request.setUri(url);
        check.run(request);
        assertTrue(check.getStatus());
    }
    
    /**
     * Tests running plugin with invalid GET, the bad param is <.
     */
    @Test
    public void testInvalidGET() throws URISyntaxException {
        
        EPICRequest request = new EPICRequest(RequestType.GET);
        String url = "http://cs.helsinki.fi/opiskelu?nimi=kissa&luku=1500&kurssi=<perusteet";
        request.setUri(url);
        check.run(request);
        assertFalse(check.getStatus());
    }
    
    /**
     * Tests running plugin with EPICRequest POST.
     */
    @Test
    public void testValidPOST() throws URISyntaxException {
        
        EPICRequest request = new EPICRequest(RequestType.POST);
        String body = "nimi=kissa&luku=1500&kurssi=perusteet";
        request.setBody(body);
        check.run(request);
        assertTrue(check.getStatus());
    }
    
    /**
     * Tests running plugin with invalid POST, the bad param is <.
     */
    @Test
    public void testInvalidPOST() throws URISyntaxException {
        
        EPICRequest request = new EPICRequest(RequestType.POST);
        String body = "nimi=kissa&luku=1500&kurssi=<perusteet";
        request.setBody(body);
        check.run(request);
        assertFalse(check.getStatus());
    }
    
    /*
     * Tests that Caps are ok to validator
     */
    @Test
    public void characterTestCaps() throws URISyntaxException {
        EPICRequest request = new EPICRequest(RequestType.POST);
        String body = "formID=20";
        request.setBody(body);
        check.run(request);
        assertTrue(check.getStatus());
    }

    /*
     * Tests that special mark _ is ok to validator
     */
    @Test
    public void characterTestSpecialMark1() throws URISyntaxException {
        EPICRequest request = new EPICRequest(RequestType.POST);
        String body = "user_name=jukka";
        request.setBody(body);
        check.run(request);
        assertTrue(check.getStatus());
    }
    /*
     * Tests that special mark - is ok to validator
     */
    @Test
    public void characterTestSpecialMark2() throws URISyntaxException {
        EPICRequest request = new EPICRequest(RequestType.POST);
        String body = "username=jukka-pekka";
        request.setBody(body);
        check.run(request);
        assertTrue(check.getStatus());
    }
        
    /*
     * Tests that special marks $ is NOT ok to validator
     */
    @Test
    public void characterTestInvalidMark1() throws URISyntaxException {
        EPICRequest request = new EPICRequest(RequestType.POST);
        String body = "user=$minna";
        request.setBody(body);
        check.run(request);
        assertFalse(check.getStatus());
    }
    
    /*
     * Tests that special marks [ is NOT ok to validator
     */
    @Test
    public void characterTestInvalidMark2() throws URISyntaxException {
        EPICRequest request = new EPICRequest(RequestType.POST);
        String body = "user=[minna";
        request.setBody(body);
        check.run(request);
        assertFalse(check.getStatus());
    }
}