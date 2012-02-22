package fi.silverskin.secureproxy.hackandslash;

import fi.silverskin.secureproxy.EPICRequest;
import fi.silverskin.secureproxy.EPICResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class HackAndSlash {

    private EPICRequest request;
    private EPICResponse response;


    //TODO: To be replaced with proper settings
    private String remoteUrl = "corvus.kapsi.fi";
    private String remotePort = "80";


    public HackAndSlash() {
        this.request = null;
        this.response = null;
    }

    public EPICRequest hackAndSlashIn(EPICRequest request) {
        try {
            URI uri = new URI(request.getUri());
            request.setUri("http://" + remoteUrl + ":" + remotePort + uri.getPath());
            Logger.getLogger(HackAndSlash.class.getName()).log(Level.INFO, request.getUri().toString());

        } catch (URISyntaxException ex) {
            Logger.getLogger(HackAndSlash.class.getName()).log(Level.SEVERE, null, ex);
        }
        return request;
    }

    public EPICResponse hackAndSlashIn(EPICResponse response) {

        throw new NotImplementedException();
    }

    public EPICRequest hackAndSlashOut(EPICRequest request) {

        throw new NotImplementedException();
    }

 public EPICResponse hackAndSlashOut(EPICResponse response) {

        String[][] tagsAndAttributes = new String[2][];
        String[] tags = {"img", "a", "area", "iframe", "frame", "script","form", "base", "link",
        "input", "object", "object","object","object","input", "ins"};
        tagsAndAttributes[0] = tags;
        String[] attributes = {"src", "href", "href", "src", "src","src","action", "href", "href", 
        "src", "data", "classid","codebase","usemap", "usemap", "cite"};
        tagsAndAttributes[1] = attributes;
        String oldResponse = response.getBody(), newResponse = "";
        for (int i = 0; i < tagsAndAttributes[0].length; i++) {
            newResponse = "";
            Pattern tagPattern = Pattern.compile("<(\\s)*" + tagsAndAttributes[0][i] + "[^>]*>");
            Matcher tagMatcher = tagPattern.matcher(oldResponse);
            int index = 0;
            while (tagMatcher.find()) {   
                if (tagMatcher.start() > index) {
                    newResponse += oldResponse.substring(index, tagMatcher.start());
                }
                index = tagMatcher.end();
                String tag = tagMatcher.group();
                newResponse += convertUrlInTag(tag, tagsAndAttributes[1][i]);
            }
            if (index == 0) {
                newResponse = oldResponse;
            } else if (index < oldResponse.length()) {
                newResponse += oldResponse.substring(index);
            }
            oldResponse = newResponse;
        }
        response.setBody(newResponse);
        return response;
    }

    public String getPseudoUrl(String URL) {
        return "pseudoURL";
    }

    public String convertUrlInTag(String tag, String attributeName) {

        Pattern sourcePattern = Pattern.compile(attributeName + "(\\s)*=(\\s)*\"[^\"]*\"");
        Matcher sourceMatcher = sourcePattern.matcher(tag.toLowerCase());
        if (sourceMatcher.find()) {
            int attributeStart = sourceMatcher.start();
            int attributeEnd = sourceMatcher.end();
            String temp = tag.substring(attributeStart, attributeEnd);
            Pattern urlPattern = Pattern.compile("\"[^\"]+\"");
            Matcher urlMatcher = urlPattern.matcher(temp);
            if ((urlMatcher.find())) {
                String url = urlMatcher.group();
                url = url.substring(1, url.length() - 1).trim();
                if (!url.equals("")) {
                    return tag.substring(0, attributeStart + urlMatcher.start() + 1)
                            + getPseudoUrl(url) + tag.substring(attributeEnd - 1);
                }
            }
        }
        return tag;
    }
    
    
    public static void main(String[] args){
        HackAndSlash hack = new HackAndSlash();
        String test = "jfhjfh<img  src=\"hkhgdkjgk\">tt<img src=\"  s\">yy<a href=\" y\">";
        EPICResponse res = new EPICResponse();
        res.setBody(test);
        System.out.println(hack.hackAndSlashOut(res).getBody());
    }
}
