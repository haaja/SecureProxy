package fi.silverskin.xss;

import fi.silverskin.secureproxy.EPICBinaryResponse;
import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICTextResponse;
import fi.silverskin.secureproxy.plugins.SecureProxyPlugin;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;


/**
 * Protect against XSS attacks by escaping all content going to protected
 * server.
 */
public class XssProtector implements SecureProxyPlugin {

    private static final Logger LOGGER = Logger.getLogger(XssProtector.class.getName(), null);

	public String getName() {
		return "XssProtector";
	}

	public void run(EPICRequest epic) {
		LOGGER.entering(XssProtector.class.getName(), "run", epic);

		EPICRequest.RequestType type = epic.getType();

		if (type == EPICRequest.RequestType.GET) {
			handleGet(epic);
		} else if (type == EPICRequest.RequestType.POST) {
			handlePost(epic);
		} else if (type == EPICRequest.RequestType.PUT) {
			handlePut(epic);
		}

		LOGGER.exiting(XssProtector.class.getName(), "run", epic);
	}

	public void run(EPICTextResponse epic) {
		LOGGER.log(Level.WARNING, "XssProtector.run() was called with EPICTextResponse.");
	}

	public void run(EPICBinaryResponse epic) {
		LOGGER.log(Level.WARNING, "XssProtector.run() was called with EPICBinaryResponse.");
	}



	/**
	 * Escape query string.
	 * @param epic
	 */
	private void handleGet(EPICRequest epic) {
		LOGGER.entering(XssProtector.class.getName(), "handleGet", epic);

		try {
			URI uri = epic.getUri();
			String path = uri.getPath();
			String query = uri.getQuery();

			String escaped = safelyEscape(query);
			URI newUri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(),
				uri.getPort(), uri.getPath(), escaped, uri.getFragment());

			epic.setUri(newUri.toString());
		} catch (URISyntaxException ex) {
			LOGGER.exiting(XssProtector.class.getName(), "handleGet", ex);
		}

		LOGGER.exiting(XssProtector.class.getName(), "handleGet", epic);
	}


	/**
	 * Escape POST body.
	 * @param epic
	 */
	private void handlePost(EPICRequest epic) {
		LOGGER.entering(XssProtector.class.getName(), "handlePost", epic);

		String escapedBody = safelyEscape(epic.getBody());
		epic.setBody(escapedBody);

		LOGGER.exiting(XssProtector.class.getName(), "handlePost", epic);
	}


	/**
	 * Escape  PUT body.
	 * @param epic
	 */
	private void handlePut(EPICRequest epic) {
		LOGGER.entering(XssProtector.class.getName(), "handlePut", epic);

		String escapedBody = safelyEscape(epic.getBody());
		epic.setBody(escapedBody);

		LOGGER.exiting(XssProtector.class.getName(), "handlePut", epic);
	}



	/**
	 * We need to escape all keys and all values but keep '&' that separates
	 * those pairs intact.
	 */
	private String safelyEscape(String str) {
		LOGGER.entering(XssProtector.class.getName(), "safelyEscape", str);

		String[] pieces = str.split("&");
		String[] escaped = new String[pieces.length];

		for (int i=0; i<pieces.length; i++) {
			escaped[i] = escapeHtml4(pieces[i]);
		}

		String retval = StringUtils.join(escaped, "&");
		LOGGER.exiting(XssProtector.class.getName(), "safelyEscape", retval);
		return retval;
	}
}
