package fi.silverskin.secureproxy;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EPICResponse extends EPICAbstraction {

    public EPICResponse() {
    }

    public EPICResponse(HashMap<String, String> headers) {
        super(headers);
    }
    
    public boolean isText() {
        String contentType = headers.get("Content-Type");
        if (contentType == null)
            contentType = "text/text";

        Pattern pattern = Pattern.compile("text/.*");
        Matcher isText = pattern.matcher(contentType);
        return isText.matches();
    }
}
