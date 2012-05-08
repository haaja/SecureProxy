package fi.silverskin.secureproxy.csrf;

import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICTextResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static org.junit.Assert.assertEquals;
import org.junit.*;

public class CsrfProtectorTest {
    
    EPICRequest request;
    EPICTextResponse response;
    CsrfProtector plugin;
    
    public CsrfProtectorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        plugin = new CsrfProtector();
        request = new EPICRequest(EPICRequest.RequestType.POST);
        request.setUri("http://example.com");
        response = new EPICTextResponse();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testGetName() {
        assertEquals("CsrfProtector", plugin.getName());
    }

    /**
     * Test of getCsrfKeyFromHeaders method, of class CsrfProtector.
     */
    @Test
    public void testGetCsrfKeyFromHeaders() {
        HashMap<String, String> headers = new HashMap<String, String>();
        String body = "";
        String csrfKey = UUID.randomUUID().toString();
        headers.put("host", "google.com");
        headers.put("user-agent", "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac "
                + "OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) "
                + "Mobile/7B405");
        headers.put("etag", "318274123FDASDFasdfasdfsdf");
        headers.put("if-modified-since", "long time ago");
        headers.put("from", "127.0.0.1");
        headers.put("cookie", "PART_NUMBER=RIDING_ROCKET_0023; "
                + "csrfKey="+csrfKey+"; PART_NUMBER=ROCKET_LAUNCHER_0001");
        
        request.setBody(body);
        request.setHeaders(headers);
        String result = plugin.getCsrfKeyFromHeaders(request);
        
        assertEquals(csrfKey, result);
    }
    
    @Test
    public void testValidateCsrfKey() {      
        String csrfKey = UUID.randomUUID().toString();
        String body = "search_block_form=johdatus&"
                + "op=Hae&form_build_id=form-afa50eaaa253e78c6830de9c8b518b62&"
                + "csrfKey="+csrfKey+"&form_id=search_block_form";

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("host", "google.com");
        headers.put("user-agent", "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like "
                + "'Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) "
                + "Mobile/7B405");
        headers.put("etag", "318274123FDASDFasdfasdfsdf");
        headers.put("if-modified-since", "long time ago");
        headers.put("from", "127.0.0.1");
        headers.put("cookie", "PART_NUMBER=RIDING_ROCKET_0023; "
                + "csrfKey="+csrfKey+"; PART_NUMBER=ROCKET_LAUNCHER_0001");

        request.setBody(body);
        request.setHeaders(headers);
        boolean result = plugin.validateCsrfKey(request);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testUpdateCookieWithCsrfKey() {

        String body = "";
        String csrfKey = "9750ae0a-e105-4f9a-9531-8dd581565626";
        
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("host", "google.com");
        headers.put("user-agent", "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like "
                + "'Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) "
                + "Mobile/7B405");
        headers.put("etag", "318274123FDASDFasdfasdfsdf");
        headers.put("if-modified-since", "long time ago");
        headers.put("from", "127.0.0.1");
        
        response.setHeaders(headers);
        response.setBody(body);
        
        plugin.updateCookieWithCsrfKey(response, csrfKey);
        
        Map<String, String> mutilatedHeaders = response.getHeaders();
        String key = mutilatedHeaders.get("Set-Cookie");
        
        String expected = "csrfKey=9750ae0a-e105-4f9a-9531-8dd581565626";
        
        assertEquals(expected, key);
    }
    
    @Test
    public void testInjectCsrfKeyField() {
        String csrfKey = UUID.randomUUID().toString();
        String body = "<html><head></head><body>"
                + "<form action=\"raportti.php\" method=\"post\">"
                + "<p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Mikä on paras vuodenaika?\"> Mikä on paras vuodenaika? "
                + "</p><p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Mikä on paras Tsohan aihe?\"> Mikä on paras Tsohan "
                + "aihe? </p><p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Mikä on paras väri?\"> Mikä on paras väri? </p>"
                + "<p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Ken on maassa kaunehin\"> Ken on maassa kaunehin </p>"
                + "<p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Ken on maassa ilkehin\"> Ken on maassa ilkehin </p>"
                + "<input type=\"submit\" value=\"Katsele tuloksia\" /></form>"
                
                + "<form action=\"raportti.php\" method=\"post\">"
                + "<p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Mikä on paras vuodenaika?\"> Mikä on paras vuodenaika? "
                + "</p><p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Mikä on paras Tsohan aihe?\"> Mikä on paras Tsohan "
                + "aihe? </p><p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Mikä on paras väri?\"> Mikä on paras väri? </p>"
                + "<p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Ken on maassa kaunehin\"> Ken on maassa kaunehin </p>"
                + "<p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Ken on maassa ilkehin\"> Ken on maassa ilkehin </p>"
                + "<input type=\"submit\" value=\"Katsele tuloksia\" /></form>"
                + "</body></html>";
        
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("host", "google.com");
        headers.put("user-agent", "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like "
                + "'Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) "
                + "Mobile/7B405");
        headers.put("etag", "318274123FDASDFasdfasdfsdf");
        headers.put("if-modified-since", "long time ago");
        headers.put("from", "127.0.0.1");

        response.setBody(body);
        response.setHeaders(headers);

        plugin.injectCsrfKeyField(response, csrfKey);
        
        String expectedBody = "<html><head></head><body>"
                + "<form action=\"raportti.php\" method=\"post\">"
                + "<input type=\"hidden\" name=\"csrfKey\" value=\""+ csrfKey +"\">"
                + "<p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Mikä on paras vuodenaika?\"> Mikä on paras vuodenaika? "
                + "</p><p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Mikä on paras Tsohan aihe?\"> Mikä on paras Tsohan "
                + "aihe? </p><p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Mikä on paras väri?\"> Mikä on paras väri? </p>"
                + "<p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Ken on maassa kaunehin\"> Ken on maassa kaunehin </p>"
                + "<p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Ken on maassa ilkehin\"> Ken on maassa ilkehin </p>"
                + "<input type=\"submit\" value=\"Katsele tuloksia\" /></form>"
                
                + "<form action=\"raportti.php\" method=\"post\">"
                + "<input type=\"hidden\" name=\"csrfKey\" value=\""+ csrfKey +"\">"
                + "<p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Mikä on paras vuodenaika?\"> Mikä on paras vuodenaika? "
                + "</p><p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Mikä on paras Tsohan aihe?\"> Mikä on paras Tsohan "
                + "aihe? </p><p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Mikä on paras väri?\"> Mikä on paras väri? </p>"
                + "<p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Ken on maassa kaunehin\"> Ken on maassa kaunehin </p>"
                + "<p><input type=\"radio\" name=\"valittuaanestys\" "
                + "value=\"Ken on maassa ilkehin\"> Ken on maassa ilkehin </p>"
                + "<input type=\"submit\" value=\"Katsele tuloksia\" /></form>"
                + "</body></html>";
        
        assertEquals(expectedBody, response.getBody());        
    }
    
    @Test
    @Ignore
    public void testValidateReferer_error() {
        HashMap<String, String> headers = new HashMap<String, String>();
        String body = "";
        String csrfKey = UUID.randomUUID().toString();
        headers.put("host", "google.com");
        headers.put("user-agent", "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac "
                + "OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) "
                + "Mobile/7B405");
        headers.put("etag", "318274123FDASDFasdfasdfsdf");
        headers.put("if-modified-since", "long time ago");
        headers.put("from", "127.0.0.1");
        headers.put("referer", "http://google.fi");
        
        request.setBody(body);
        request.setHeaders(headers);
        
        assertEquals(false, plugin.validateReferer(request));
    }
    
    @Test
    @Ignore 
    public void testValidateReferer_success() {
        
        String body = "";
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("host", "google.com");
        headers.put("user-agent", "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac "
                + "OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) "
                + "Mobile/7B405");
        headers.put("etag", "318274123FDASDFasdfasdfsdf");
        headers.put("if-modified-since", "long time ago");
        headers.put("from", "127.0.0.1");
        headers.put("referer", "http://palomuuri.users.cs.helsinki.fi");
        
        request.setBody(body);
        request.setHeaders(headers);
        
        assertEquals(true, plugin.validateReferer(request));
    }
}
