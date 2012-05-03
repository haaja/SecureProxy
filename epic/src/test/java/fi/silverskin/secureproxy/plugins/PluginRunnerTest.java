package fi.silverskin.secureproxy.plugins;

import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICTextResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Iivari Äikäs
 */
public class PluginRunnerTest {

	private PluginRunner runner;
	private PluginRunner brokenRunner;

	private Properties conf;
	private Properties brokenConf;
	private InputStream in1;
	private InputStream in2;

	EPICTextResponse resp;
	EPICRequest req;
	
	public PluginRunnerTest() {
        this.in1 = getClass().getClassLoader().getResourceAsStream("plugin_loader_test.properties");
        this.conf = new Properties();

        this.in2 = getClass().getClassLoader().getResourceAsStream("plugin_loader_test_broken.properties");
        this.brokenConf = new Properties();
	}

	@Before
	public void setUp() throws IOException {
		conf.load(in1);
		runner = new PluginRunner(conf);

		brokenConf.load(in2);
		brokenRunner = new PluginRunner(brokenConf);

		resp = new EPICTextResponse();
		resp.setBody("");
		req = new EPICRequest(EPICRequest.RequestType.POST);
		req.setBody("");
	}
	
	@After
	public void tearDown() {
	}

	@Test
	public void testRunEPICRequest() {
		runner.run(req);
		assertEquals("Set by TestPlugin1Set by TestPlugin2", req.getBody());
	}
	
	@Test
	public void testBrokenRunEPICRequest() {
		brokenRunner.run(req);
		assertEquals("", req.getBody());
	}


	@Test
	public void testRunEPICTextResponse() {
		runner.run(resp);
		assertEquals("Set by TestPlugin1Set by TestPlugin2", resp.getBody());
	}

	@Test
	public void testBrokenRunEPICTextResponse() {
		brokenRunner.run(resp);
		assertEquals("", resp.getBody());
	}
}
