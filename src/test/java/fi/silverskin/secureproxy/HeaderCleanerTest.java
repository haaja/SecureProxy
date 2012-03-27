/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy;

import java.util.HashMap;
import java.util.Properties;
import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author haaja
 */
public class HeaderCleanerTest {

    private EPICRequest request;
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

        this.request = new EPICRequest("get", headers, body);
        ProxyConfigurer configurer = new ProxyConfigurer("config.properties");
        this.configuration = configurer.getConfigurationProperties();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of cleanHeaders method, of class HeaderCleaner.
     */
    @Test
    public void testCleanHeaders() {
        System.out.println("Testing cleanHeaders method of class HeaderCleaner.java");
        HashMap expectedHeaders = new HashMap();
        HashMap testHeaders = new HashMap();

        testHeaders.put("host", "google.com");
        testHeaders.put("user-agent", "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Mobile/7B405");
        testHeaders.put("etag", "318274123FDASDFasdfasdfsdf");
        testHeaders.put("if-modified-since", "long time ago");
        testHeaders.put("from", "127.0.0.1");
        testHeaders.put("cookie", "PART_NUMBER=RIDING_ROCKET_0023; PART_NUMBER=ROCKET_LAUNCHER_0001");

        expectedHeaders.put("cookie", "PART_NUMBER=RIDING_ROCKET_0023; PART_NUMBER=ROCKET_LAUNCHER_0001");
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
        System.out.println("Testing cleanHeaders method of class HeaderCleaner.java");
        HashMap expectedHeaders = new HashMap();
        HashMap testHeaders = new HashMap();

        testHeaders.put("host", "google.com");
        testHeaders.put("user-agent", "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Mobile/7B405");
        testHeaders.put("etag", "318274123FDASDFasdfasdfsdf");
        testHeaders.put("if-modified-since", "long time ago");
        testHeaders.put("from", "127.0.0.1");
        testHeaders.put("cookie", "PART_NUMBER=RIDING_ROCKET_0023; PART_NUMBER=ROCKET_LAUNCHER_0001");
        testHeaders.put("content-type", "text/html");

        expectedHeaders.put("cookie", "PART_NUMBER=RIDING_ROCKET_0023; PART_NUMBER=ROCKET_LAUNCHER_0001");
        expectedHeaders.put("host", "tkt_palo.users.cs.helsinki.fi");
        expectedHeaders.put("content-type", "text/html");

        request.setHeaders(testHeaders);
        EPICRequest result = HeaderCleaner.cleanHeaders(request, configuration);
        assertEquals(expectedHeaders, new HashMap<String, String>(result.getHeaders()));

    }
}
