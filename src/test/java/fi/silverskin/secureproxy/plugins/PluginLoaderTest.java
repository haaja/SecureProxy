package fi.silverskin.secureproxy.plugins;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import static org.junit.Assert.*;
import org.junit.*;


public class PluginLoaderTest {

    public PluginLoaderTest() {
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

    @Test
    public void testLoadPlugins() {
        // Implement!
    }

    @Test
    public void testValidateConfig() throws IOException {
        InputStream in = null;

        try {
            in = getClass().getClassLoader().getResourceAsStream("plugin_loader_test.properties");
            Properties pluginConfig = new Properties();
            pluginConfig.load(in);
            boolean result = PluginLoader.validateConfig(pluginConfig);
            assertEquals(true, result);
        } finally {
            in.close();
        }
    }

    @Test
    public void testGetPluginNames() throws IOException {
        InputStream in = null;

        try {
            in = getClass().getClassLoader().getResourceAsStream("plugin_loader_test.properties");
            Properties pluginConfig = new Properties();
            pluginConfig.load(in);
            String[] expResult = {"first", "second", "third"};
            String[] result = PluginLoader.getPluginNames(pluginConfig);
            assertEquals(expResult, result);
        } finally {
            in.close();
        }
    }

    @Test
    public void testGetPluginDir() throws IOException {
        InputStream in = null;

        try {
            in = getClass().getClassLoader().getResourceAsStream("plugin_loader_test.properties");
            Properties pluginConfig = new Properties();
            pluginConfig.load(in);
            File result = PluginLoader.getPluginDirFile(pluginConfig);
            File actual = new File("plugin_test_dir/");
            assertEquals(actual.getAbsolutePath(), result.getAbsolutePath());
        } finally {
            in.close();
        }
    }
}
