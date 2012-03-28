package fi.silverskin.secureproxy;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EPICResponse extends EPICAbstraction {

    /* HTTP status code */
    private int statusCode;

    public EPICResponse() {
    }

    public EPICResponse(HashMap<String, String> headers) {
        super(headers);
    }

    /**
     * Checks if HTTP response contains just text data or not.
     *
     * @return true if request contains only text data, otherwise false.
     */
    public boolean isText() {
        String contentType = headers.get("Content-Type");
        if (contentType == null)
            contentType = "text/text";

        Pattern pattern = Pattern.compile("text/.*");
        Matcher isText = pattern.matcher(contentType);
        return isText.matches();
    }


    /**
     * Set HTTP status code
     * @param statusCode
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Get HTTP status code
     * @return HTTP status code of the response
     */
    public int getStatusCode() {
        return statusCode;
    }
}
