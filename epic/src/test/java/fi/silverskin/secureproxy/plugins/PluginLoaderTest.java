package fi.silverskin.secureproxy.plugins;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import static org.junit.Assert.*;
import org.junit.*;

public class PluginLoaderTest {

    private Properties config;

    public PluginLoaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream("plugin_loader_test.properties");
        this.config = new Properties();
        this.config.load(in);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLoadPlugins() {
        // Implement!
    }

    @Test
    public void testValidateConfig() throws IOException {
        boolean result = PluginLoader.validateConfig(config);
        assertEquals(true, result);

        result = PluginLoader.validateConfig(null);
        assertEquals(false, result);
    }

    @Test
    public void testGetPluginNames() throws IOException {
        String[] expResult = {"first", "second", "third"};
        String[] result = PluginLoader.getPluginNames(config);
        assertEquals(expResult, result);

        result = PluginLoader.getPluginNames(null);
        assertEquals(0, result.length);
    }

	@Test
    public void testGetPluginNamesWithNoNames() throws IOException {
		config.remove("load_order");

		boolean valid = PluginLoader.validateConfig(config);
		assertFalse(valid);

        String[] result = PluginLoader.getPluginNames(config);
		assertNull(result);
    }

	@Test
    public void testGetPluginNamesWithEmptyNames() throws IOException {
		config.setProperty("load_order", "");
        String[] result = PluginLoader.getPluginNames(config);
		assertNull(result);
    }


    @Test
    public void testGetPluginDir() throws IOException {
        File result = PluginLoader.getPluginDirFile(config);
        File actual = new File("src/test/resources/plugin_test_dir/");
        assertEquals(actual.getAbsolutePath(), result.getAbsolutePath());

        result = PluginLoader.getPluginDirFile(null);
        assertEquals(null, result);
    }

    @Test
    public void testGetPluginURLs() throws MalformedURLException {
        URL[] urls = PluginLoader.getPluginURLs(config);
        assertEquals(3, urls.length);  // there should be 3 jars
    }
}
