package fi.silverskin.secureproxy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class HackAndSlash {

    private EPICRequest request;
    private EPICResponse response;
    //To be replaced with proper settings
    private String remoteUrl = "localhost";
    private String remotePort = "8084";

    public HackAndSlash() {
        this.request = null;
        this.response = null;
    }

    public EPICRequest hackAndSlashIn(EPICRequest request) {

        throw new NotImplementedException();
    }

    public EPICResponse hackAndSlashIn(EPICResponse response) {

        throw new NotImplementedException();
    }

    public EPICRequest hackAndSlashOut(EPICRequest request) {

        throw new NotImplementedException();
    }

    public EPICResponse hackAndSlashOut(EPICResponse response) {

        
        Pattern tagPattern = Pattern.compile("<(\\s)*img[^>]*>");
        String oldResponse = response.getBody(), newResponse = "";
        Matcher tagMatcher = tagPattern.matcher(oldResponse);

        int index = 0;
        while (tagMatcher.find()) {
            
            if (tagMatcher.start() > index) {
                newResponse += oldResponse.substring(index, tagMatcher.start());
            }
            index = tagMatcher.end();
            Pattern sourcePattern =
                    Pattern.compile("src(\\s)*=(\\s)*\"[^\"]*\"");
            Matcher sourceMatcher = sourcePattern.matcher(tagMatcher.group());
            newResponse += sourceMatcher.replaceFirst("src=\"http://upload.wikimedia.org/wikipedia/"
                    + "commons/thumb/a/af/Tux.png/220px-Tux.png\"");
        }
        if (index == 0) {
            newResponse = oldResponse;
        } else if (index < oldResponse.length()) {
            newResponse +=
                    oldResponse.substring(index);
        }
        response.setBody(newResponse);
        return response;
    }
   
}
