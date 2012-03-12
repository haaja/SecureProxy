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
        configures = new ProxyConfigurer("config.properties");
        //configures = new ProxyConfigurer();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getConfigure method, of class ProxyConfigurer.
     */
    @Test
    public void testGetConfigure() {
        // tests the normal case
        String testKey = "publicURI";
        String control = "http://palomuuri.users.cs.helsinki.fi";
        String[] result = configures.getConfigure(testKey);
        assertEquals(control, result[0]);
        
        // tests if key is wrong or cannot find
        String nullTest = "boo";
        String[] nullResult = configures.getConfigure(nullTest);
        assertNull(nullResult);
        
        // test the case of three parameters
        String threeTest = "tester";
        String[] threeControl = {"test1", "test2", "test3"};
        String[] threeResult = configures.getConfigure(threeTest);
        assertEquals(threeControl[0], threeResult[0]);
        assertEquals(threeControl[1], threeResult[1]);
        assertEquals(threeControl[2], threeResult[2]);
    }
}
