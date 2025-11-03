package webserver.http.request.parser;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class QueryParser {

    public Map<String, String> parse(String queryString) {
        Map<String, String> queryMap = new LinkedHashMap<>();

        queryString = decodeQuery(queryString);

        String[] queryParameters = queryString.split("&");

        for (String queryParameter : queryParameters) {
            String[] keyValue = queryParameter.split("=", 2);
            if (keyValue.length == 2) {
                queryMap.put(keyValue[0], keyValue[1]);
            } else if (keyValue.length == 1) {
                // key만 있고 value는 없는 경우도 고려
                queryMap.put(keyValue[0], "");
            }
        }

        return queryMap;
    }

    private String decodeQuery(String query) {
        return URLDecoder.decode(query, StandardCharsets.UTF_8);
    }
}
