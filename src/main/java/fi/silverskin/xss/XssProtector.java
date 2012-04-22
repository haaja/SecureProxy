package fi.silverskin.xss;

import fi.silverskin.secureproxy.EPICBinaryResponse;
import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICTextResponse;
import fi.silverskin.secureproxy.plugins.SecureProxyPlugin;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;


public class XssProtector implements SecureProxyPlugin {

    private static final Logger LOGGER = Logger.getLogger(XssProtector.class.getName(), null);

	public String getName() {
		return "XssProtector";
	}

	public void run(EPICRequest epic) {
		EPICRequest.RequestType type = epic.getType();

		if (type == EPICRequest.RequestType.GET) {
			handleGet(epic);
		} else if (type == EPICRequest.RequestType.POST) {
			handlePost(epic);
		} else if (type == EPICRequest.RequestType.PUT) {
			handlePut(epic);
		}
	}

	public void run(EPICTextResponse epic) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void run(EPICBinaryResponse epic) {
		throw new UnsupportedOperationException("Not supported yet.");
	}



	private void handleGet(EPICRequest epic) {
		try {
			URI uri = epic.getUri();
			String path = uri.getPath();
			String query = uri.getQuery();

			String escaped = safelyEscape(query);
			URI newUri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(),
				uri.getPort(), uri.getPath(), escaped, uri.getFragment());

			epic.setUri(newUri.toString());
		} catch (URISyntaxException ex) {
			Logger.getLogger(XssProtector.class.getName()).log(Level.SEVERE, null, ex);
		}
	}


	private void handlePost(EPICRequest epic) {
		String escapedBody = safelyEscape(epic.getBody());
		epic.setBody(escapedBody);
	}


	private void handlePut(EPICRequest epic) {
		String escapedBody = safelyEscape(epic.getBody());
		epic.setBody(escapedBody);
	}



	/**
	 * We need to escape all keys and all values but keep '&' that separates
	 * those pairs intact.
	 */
	private String safelyEscape(String str) {
		String[] pieces = str.split("&");
		String[] escaped = new String[pieces.length];

		for (int i=0; i<pieces.length; i++) {
			escaped[i] = escapeHtml4(pieces[i]);
		}

		StringBuilder sb = new StringBuilder();
		for (int i=0; i<escaped.length; i++) {
			if (i != 0)
				sb.append("&");

			sb.append(escaped[i]);
		}

		return sb.toString();
	}
}
