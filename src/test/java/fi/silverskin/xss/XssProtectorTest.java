package fi.silverskin.xss;

import fi.silverskin.secureproxy.EPICRequest;
import java.net.URI;
import org.junit.*;
import static org.junit.Assert.*;

public class XssProtectorTest {

	XssProtector plugin;

	public XssProtectorTest() {
	}

	@Before
	public void setUp() {
		plugin = new XssProtector();
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testGetName() {
		assertEquals("XssProtector", plugin.getName());
	}

	@Test
	public void testCleanGetQuery() {
		EPICRequest req = new EPICRequest(EPICRequest.RequestType.GET);
		String uri = "http://example.com?first=value1&second=value";
		req.setUri(uri);
		plugin.run(req);

		assertEquals(uri, req.getUri().toString());
	}

	@Test
	public void testDirtyQueryValue() {
		EPICRequest req = new EPICRequest(EPICRequest.RequestType.GET);
		String uri = "http://example.com?first=<script>&second=value";
		req.setUri(uri);
		plugin.run(req);

		assertFalse(uri.equals(req.getUri().toString()));
	}

	@Test
	public void testDirtyQueryKey() {
		EPICRequest req = new EPICRequest(EPICRequest.RequestType.GET);
		String uri = "http://example.com?<script>=value1&second=value";
		req.setUri(uri);
		plugin.run(req);

		assertFalse(uri.equals(req.getUri().toString()));
	}


	@Test
	public void testRun_EPICTextResponse() {
	}

	@Test
	public void testRun_EPICBinaryResponse() {
	}
}
