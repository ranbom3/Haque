package com.ranbom.haque.util;


/**
 * @author dingj
 */

public class GetResume {

    public static String getResume(String content) {
        content = HTMLSpirit.delHTMLTag(content);
        if (content.length() < 20) {
            return content;
        }
        return content.substring(0, 20) + "...";
    }
}
