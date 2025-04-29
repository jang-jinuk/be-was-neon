package webserver.http.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.common.StatusCode;
import webserver.http.response.handler.DynamicHandler;
import webserver.http.session.Session;
import webserver.http.template.TemplateEngine;

import java.util.Optional;

public class ResponseBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

    private final StatusCode statusCode;
    private byte[] header;
    private byte[] body;
    private String contentType;
    private String redirectUrl;
    private final Optional<String> sessionId;
    private final Optional<Session> currentSession;

    public ResponseBuilder(StatusCode statusCode, byte[] body, String contentType, Optional<Session> currentSession) {
        this.statusCode = statusCode;
        this.header = new byte[0];
        this.body = body;
        this.contentType = contentType;
        this.sessionId = Optional.empty();
        this.currentSession = currentSession;
    }

    public ResponseBuilder(StatusCode statusCode, String redirectUrl, Optional<String> sessionId, Optional<Session> currentSession) {
        this.statusCode = statusCode;
        this.header = new byte[0];
        this.body = new byte[0];
        this.redirectUrl = redirectUrl;
        this.sessionId = sessionId;
        this.currentSession = currentSession;
    }

    public Response build() {
        TemplateEngine templateEngine = new TemplateEngine(body, currentSession);

        switch (statusCode) {
            case OK, NOT_FOUND, BAD_REQUEST, UNAUTHORIZED -> {
                body = templateEngine.render();
                logger.debug("=====================render body: {}===================", new String(body));
                writeDefaultMessage();
            }
            case FOUND -> writeRedirectMessage();
        }

        logger.debug("=====================header: {}===================", new String(header));
        logger.debug("=====================body: {}===================", new String(body));

        return new Response(header, body);
    }

    private void writeDefaultMessage() {
        String headers = "HTTP/1.1 " + statusCode.getCode() + " " + statusCode.getMessage() + "\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + body.length + "\r\n"
                + "\r\n";

        header = headers.getBytes();
    }

    private void writeRedirectMessage() {
        String headers = "HTTP/1.1 " + statusCode.getCode() + " " + statusCode.getMessage() + "\r\n"
                + "Location: " + redirectUrl + "\r\n";

        if (sessionId.isPresent()) {
            headers += "Set-Cookie: sid=" + sessionId.get() + "; Path=/" + "\r\n";
        }

        headers += "\r\n";

        header = headers.getBytes();
        body = "".getBytes();
    }
}

