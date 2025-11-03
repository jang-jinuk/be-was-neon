package webserver.http.request.parser;

import webserver.http.request.Request;
import webserver.http.request.param.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestParser {

    private final InputStream in;
    private final RequestLineParser requestLineParser;
    private final QueryParser queryParser;
    private final CookieParser cookieParser;

    public RequestParser(InputStream in) {
        this.in = in;
        this.requestLineParser = new RequestLineParser();
        this.queryParser = new QueryParser();
        this.cookieParser = new CookieParser();
    }

    public Request parseRequest() throws IOException {

        String[] requestLineParts = readLine().split(" ");

        RequestLineParams requestLine = requestLineParser.parse(requestLineParts);
        HeaderParams headers = parseRequestHeaders();
        QueryParams query = new QueryParams(Collections.emptyMap());
        BodyParams body = new BodyParams(Collections.emptyMap());
        CookieParams cookie = new CookieParams(Collections.emptyMap());

        if (isIncludeBody(headers)) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            body = parseRequestBody(contentLength);
        }

        if (isIncludeCookie(headers)) {
            cookie = cookieParser.parse(headers);
        }

        if (requestLineParts[1].contains("?")) {
            int queryStart = requestLineParts[1].indexOf('?');
            query = new QueryParams(queryParser.parse(requestLineParts[1].substring(queryStart + 1)));
        }

        return new Request(requestLine, headers, body, query, cookie);
    }

    private HeaderParams parseRequestHeaders() throws IOException {
        Map<String, String> headers = new LinkedHashMap<>();

        String line;

        while (!(line = readLine()).isEmpty()) {
            String[] headerParts = parseLine(line);
            headers.put(headerParts[0], headerParts[1].trim());
        }

        return new HeaderParams(headers);
    }

    private BodyParams parseRequestBody(int contentLength) throws IOException {
        Map<String, String> body;

        byte[] readBody = readRequestBody(contentLength);

        String bodyString = new String(readBody, StandardCharsets.UTF_8);
        body = queryParser.parse(bodyString);

        return new BodyParams(body);
    }

    private byte[] readRequestBody(int contentLength) throws IOException {
        byte[] body = new byte[contentLength];

        int byteRead = 0;
        while (byteRead < contentLength) {
            int read = in.read(body, 0, contentLength);
            if (read == -1) break;
            byteRead += read;
        }

        return body;
    }

    private static String[] parseLine(String line) {
        return line.split(":");
    }

    private String readLine() throws IOException {
        StringBuilder lineBuilder = new StringBuilder();
        int readChar;
        while ((readChar = in.read()) != -1) {
            if (readChar == '\r') continue;
            if (readChar == '\n') break;
            lineBuilder.append((char) readChar);
        }
        return lineBuilder.toString();
    }

    private boolean isIncludeBody(HeaderParams headers) {
        return headers.containsKey("Content-Length");
    }

    private boolean isIncludeCookie(HeaderParams headerParams) {
        return headerParams.containsKey("Cookie");
    }
}
