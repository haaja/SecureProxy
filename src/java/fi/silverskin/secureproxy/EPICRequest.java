package fi.silverskin.secureproxy;

import java.util.HashMap;

public class EPICRequest extends EPICAbstraction {

    public EPICRequest() {
    }

    public EPICRequest(HashMap<String, String> headers, String body) {
        super(headers, body);
    }
}
