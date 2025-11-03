package webserver.http.request.parser;

import webserver.http.request.param.CookieParams;
import webserver.http.request.param.HeaderParams;

import java.util.HashMap;
import java.util.Map;

public class CookieParser {
    public CookieParams parse(HeaderParams headerParams) {
        String cookie = headerParams.get("Cookie");
        Map<String, String> cookieMap = new HashMap<>();

        String[] pairs = cookie.split(";\\s*");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                cookieMap.put(keyValue[0], keyValue[1]);
            }
        }

        return new CookieParams(cookieMap);
    }
}
