/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy;

import java.net.URI;
import java.net.URISyntaxException;
import static org.junit.Assert.assertEquals;
import org.junit.*;

public class SecureProxyUtilitiesTest {

    public SecureProxyUtilitiesTest() {
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
     * Test of makeUriFromString method, of class SecureProxyUtilities.
     */
    @Test
    public void testMakeUriFromString_absolute() throws URISyntaxException {
        System.out.println("makeUriFromString");
        String url = "http://www.google.com";
        URI expResult = new URI("http://www.google.com");
        URI result = SecureProxyUtilities.makeUriFromString(url);
        assertEquals(expResult, result);
    }

    /**
     * Test of makeUriFromString method, of class SecureProxyUtilities.
     */
    @Test
    public void testMakeUriFromString_absolute_urlencoded() throws URISyntaxException {
        System.out.println("makeUriFromString");
        String url = "http://www.google.com?file=document file.txt";
        URI expResult = new URI("http://www.google.com?file=document%20file.txt");
        URI result = SecureProxyUtilities.makeUriFromString(url);
        assertEquals(expResult, result);
    }

    /**
     * Test of makeUriFromString method, of class SecureProxyUtilities.
     */
    @Test
    public void testMakeUriFromString_relative() throws URISyntaxException {
        System.out.println("makeUriFromString");
        String url = "/blog/index.html";
        URI expResult = new URI("/blog/index.html");
        URI result = SecureProxyUtilities.makeUriFromString(url);
        assertEquals(expResult, result);
    }

    /**
     * Test of isProtectedUrl method, of class SecureProxyUtilities.
     */
    @Test
    public void testIsProtectedUrl_protectedURL() throws URISyntaxException {
        System.out.println("isProtectedUrl");
        URI privateUri = new URI("http://www.cs.helsinki.fi");
        URI locationUri = new URI("http://www.cs.helsinki.fi");
        boolean expResult = true;
        boolean result = SecureProxyUtilities.isProtectedUrl(privateUri, locationUri);
        assertEquals(expResult, result);
    }

    /**
     * Test of isProtectedUrl method, of class SecureProxyUtilities.
     */
    @Test
    public void testIsProtectedUrl_nonprotectedURL() throws URISyntaxException {
        System.out.println("isProtectedUrl");
        URI privateUri = new URI("http://www.cs.helsinki.fi");
        URI locationUri = new URI("http://www.google.com");
        boolean expResult = false;
        boolean result = SecureProxyUtilities.isProtectedUrl(privateUri, locationUri);
        assertEquals(expResult, result);
    }
}
