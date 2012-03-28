/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.paramcheck;

import fi.silverskin.secureproxy.EPICBinaryResponse;
import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICRequest.RequestType;
import fi.silverskin.secureproxy.EPICTextResponse;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.*;
import static org.junit.Assert.*;

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
    public void testRunWithRequestGET() throws URISyntaxException {
        
        EPICRequest request = new EPICRequest(RequestType.GET);
        String url = "http://cs.helsinki.fi/opiskelu?nimi=kissa&luku=1500&kurssi=perusteet";
        request.setUri(url);
        URI uri = new URI(url);
        System.out.println(uri.getQuery());
    
    }
    /**
     * Tests running plugin with EPICRequest POST.
     */
    @Test
    public void testRunWithRequestPOST() throws URISyntaxException {
        
        EPICRequest request = new EPICRequest(RequestType.POST);
    
    }

}
