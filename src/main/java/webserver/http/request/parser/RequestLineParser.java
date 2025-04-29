package webserver.http.request.parser;

import webserver.http.common.UrlPattern;
import webserver.http.request.param.RequestLineParams;

import java.util.LinkedHashMap;
import java.util.Map;

public class RequestLineParser {

    public RequestLineParams parse(String[] requestLineParts) {
        Map<String, String> requestLine = new LinkedHashMap<>();
        requestLine.put("method", requestLineParts[0]);
        requestLine.put("path", parsePath(requestLineParts[1], requestLineParts[0]));
        requestLine.put("protocol", requestLineParts[2]);
        return new RequestLineParams(requestLine);
    }

    private String parsePath(String path, String method) {
        if (isFile(path) && isExcludeQuery(path)) {
            return path;
        } else if (path.equals("/")) {
            return "/index.html";
        }else if (!isFile(path) && isExcludeQuery(path) && !UrlPattern.contain(method, path)) {
            return path + "/index.html";
        } else {
            String[] pathParts = path.split("\\?", 2);
            path = pathParts[0];
            return path;
        }
    }

    private boolean isFile(String path) {
        return path.contains(".");
    }

    private boolean isExcludeQuery(String path) {
        return !path.contains("?");
    }
}
