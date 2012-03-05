/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.silverskin.secureproxy;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author peltoel
 */
public class ProxyConfigurerTest {
    
    private ProxyConfigurer configures;
    
    public ProxyConfigurerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        configures = new ProxyConfigurer();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getConfigure method, of class ProxyConfigurer.
     */
    @Test
    public void testGetConfigure() {
        String testKey = "publicURI";
        String control = "http://palomuuri.users.cs.helsinki.fi";
        String result = configures.getConfigure(testKey);
        assertEquals(control, result);
    }
}
