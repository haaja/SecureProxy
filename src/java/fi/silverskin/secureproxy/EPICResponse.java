package fi.silverskin.secureproxy;

import java.util.HashMap;

public class EPICResponse extends EPICAbstraction {

    public EPICResponse() {
    }

    public EPICResponse(HashMap<String, String> headers, String body) {
        super(headers, body);
    }
}
