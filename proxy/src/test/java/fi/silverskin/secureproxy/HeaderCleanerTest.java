package fi.silverskin.secureproxy;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Properties;
import static org.junit.Assert.assertEquals;
import org.junit.*;

public class HeaderCleanerTest {

    private EPICRequest request;
    private EPICResponse response;
    private Properties configuration;

    public HeaderCleanerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        HashMap<String, String> headers = new HashMap<String, String>();
        String body = "";

        request = new EPICRequest("get", headers, body);
        response = new EPICResponse(headers);
        ProxyConfigurer configurer = new ProxyConfigurer("config.properties");
        configuration = configurer.getConfigurationProperties();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of cleanHeaders method, of class HeaderCleaner.
     */
    @Test
    public void testCleanHeaders() {
        HashMap expectedHeaders = new HashMap();
        HashMap testHeaders = new HashMap();

        testHeaders.put("host", "google.com");
        testHeaders.put("user-agent", "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like "
                + "Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) "
                + "Mobile/7B405");
        testHeaders.put("etag", "318274123FDASDFasdfasdfsdf");
        testHeaders.put("if-modified-since", "long time ago");
        testHeaders.put("from", "127.0.0.1");
        testHeaders.put("cookie", "PART_NUMBER=RIDING_ROCKET_0023; "
                + "PART_NUMBER=ROCKET_LAUNCHER_0001");

        expectedHeaders.put("cookie", "PART_NUMBER=RIDING_ROCKET_0023; "
                + "PART_NUMBER=ROCKET_LAUNCHER_0001");
        expectedHeaders.put("host", "tkt_palo.users.cs.helsinki.fi");

        request.setHeaders(testHeaders);
        EPICRequest result = HeaderCleaner.cleanHeaders(request, configuration);
        assertEquals(expectedHeaders, new HashMap<String, String>(result.getHeaders()));
        
    }

    /**
     * Test of cleanHeaders method, of class HeaderCleaner.
     */
    @Test
    public void testCleanHeaders2() {
        HashMap expectedHeaders = new HashMap();
        HashMap testHeaders = new HashMap();

        testHeaders.put("host", "google.com");
        testHeaders.put("user-agent", "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like "
                + "Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) "
                + "Mobile/7B405");
        testHeaders.put("etag", "318274123FDASDFasdfasdfsdf");
        testHeaders.put("if-modified-since", "long time ago");
        testHeaders.put("from", "127.0.0.1");
        testHeaders.put("cookie", "PART_NUMBER=RIDING_ROCKET_0023; "
                + "PART_NUMBER=ROCKET_LAUNCHER_0001");
        testHeaders.put("content-type", "text/html");

        expectedHeaders.put("cookie", "PART_NUMBER=RIDING_ROCKET_0023; "
                + "PART_NUMBER=ROCKET_LAUNCHER_0001");
        expectedHeaders.put("host", "tkt_palo.users.cs.helsinki.fi");
        expectedHeaders.put("content-type", "text/html");

        request.setHeaders(testHeaders);
        EPICRequest result = HeaderCleaner.cleanHeaders(request, configuration);
        assertEquals(expectedHeaders, 
                     new HashMap<String, String>(result.getHeaders()));

    }

    @Test
    public void testmaskLocationHeader() throws URISyntaxException {
        HashMap expectedHeaders = new HashMap();
        HashMap testHeaders = new HashMap();

        testHeaders.put("host", "google.com");
        testHeaders.put("user-agent", "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like "
                + "Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) "
                + "Mobile/7B405");
        testHeaders.put("etag", "318274123FDASDFasdfasdfsdf");
        testHeaders.put("if-modified-since", "long time ago");
        testHeaders.put("from", "127.0.0.1");
        testHeaders.put("cookie", "PART_NUMBER=RIDING_ROCKET_0023; "
                + "PART_NUMBER=ROCKET_LAUNCHER_0001");
        testHeaders.put("content-type", "text/html");
        testHeaders.put("Location", "http://128.214.9.12/blog/index.php");

        expectedHeaders.put("host", "google.com");
        expectedHeaders.put("user-agent", "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 "
                + "like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like "
                + "Gecko) Mobile/7B405");
        expectedHeaders.put("etag", "318274123FDASDFasdfasdfsdf");
        expectedHeaders.put("if-modified-since", "long time ago");
        expectedHeaders.put("from", "127.0.0.1");
        expectedHeaders.put("cookie", "PART_NUMBER=RIDING_ROCKET_0023; "
                + "PART_NUMBER=ROCKET_LAUNCHER_0001");
        expectedHeaders.put("content-type", "text/html");
        expectedHeaders.put("Location", 
                "http://palomuuri.users.cs.helsinki.fi:80/blog/index.php");

        response.setHeaders(testHeaders);
        EPICResponse result = HeaderCleaner.maskLocationHeader(response, 
                                                               configuration);
        EPICResponse expectedResult = new EPICResponse(expectedHeaders);
        assertEquals(expectedResult.getHeaders(), result.getHeaders());
    }
}
