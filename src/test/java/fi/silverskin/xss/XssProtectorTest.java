package fi.silverskin.xss;

import fi.silverskin.secureproxy.EPICRequest;
import java.net.URI;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Test output validation could be more specific, now we are just checking that
 * something done to elements that needs to be escaped.
 */
public class XssProtectorTest {

	XssProtector plugin;
	EPICRequest req;

	public XssProtectorTest() {
	}

	@Before
	public void setUp() {
		plugin = new XssProtector();
		req = new EPICRequest(EPICRequest.RequestType.POST);
		req.setUri("http://example.com");
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
		req = new EPICRequest(EPICRequest.RequestType.GET);
		String uri = "http://example.com?first=value1&second=value";
		req.setUri(uri);
		plugin.run(req);

		assertEquals(uri, req.getUri().toString());
	}

	@Test
	public void testDirtyGetQueryValue() {
		req = new EPICRequest(EPICRequest.RequestType.GET);
		String uri = "http://example.com?first=<script>&second=value";
		req.setUri(uri);
		plugin.run(req);

		assertFalse(uri.equals(req.getUri().toString()));
	}

	@Test
	public void testDirtyGetQueryKey() {
		req = new EPICRequest(EPICRequest.RequestType.GET);
		String uri = "http://example.com?<script>=value1&second=value";
		req.setUri(uri);
		plugin.run(req);

		assertFalse(uri.equals(req.getUri().toString()));
	}



	@Test
	public void testCleanPostRequest() {
		String body = "key=value&key2=value2";
		req.setBody(body);
		plugin.run(req);

		assertEquals(body, req.getBody());
	}

	@Test
	public void testDirtyPostKey() {
		String body = "key=value&key2<script>=value2";
		req.setBody(body);
		plugin.run(req);

		assertFalse(body.equals(req.getBody()));
	}

	@Test
	public void testDirtyPostValue() {
		String body = "key=value&key2=value2<script>";
		req.setBody(body);
		plugin.run(req);

		assertFalse(body.equals(req.getBody()));
	}



	@Test
	public void testCleanPutRequest() {
		req = new EPICRequest(EPICRequest.RequestType.PUT);
		String uri = "http://example.com";
		req.setUri(uri);

		String body = "key=value&key2=value2";
		req.setBody(body);
		plugin.run(req);

		assertEquals(body, req.getBody());
	}

	@Test
	public void testDirtyPutKey() {
		req = new EPICRequest(EPICRequest.RequestType.PUT);
		String uri = "http://example.com";
		req.setUri(uri);

		String body = "key=value&key2<script>=value2";
		req.setBody(body);
		plugin.run(req);

		assertFalse(body.equals(req.getBody()));
	}

	@Test
	public void testDirtyPutValue() {
		req = new EPICRequest(EPICRequest.RequestType.PUT);
		String uri = "http://example.com";
		req.setUri(uri);

		String body = "key=value&key2=value2<script>";
		req.setBody(body);
		plugin.run(req);

		assertFalse(body.equals(req.getBody()));
	}
}
