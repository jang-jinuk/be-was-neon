package webserver.http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.param.*;

import java.util.Map;

public class Request {
    private static final Logger logger = LoggerFactory.getLogger(Request.class);

    private final RequestLineParams requestLine;
    private final HeaderParams headers;
    private final BodyParams body;
    private final QueryParams query;
    private final CookieParams cookie;

    public Request(RequestLineParams requestLine, HeaderParams headers, BodyParams body, QueryParams query, CookieParams cookie) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
        this.query = query;
        this.cookie = cookie;
        print();
    }

    public String getRequestLine(String key) {
        return requestLine.get(key);
    }

    public BodyParams getBody() {
        return body;
    }

    public CookieParams getCookie() {
        return cookie;
    }

    private void print() {
        logger.debug("================Client Request==================");
        logger.debug("{}", requestLine);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            logger.debug("{}: {}", entry.getKey(), entry.getValue());
        }
        logger.debug("================================================");
    }
}
