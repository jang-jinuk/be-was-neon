package webserver.http.response.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.common.ContentType;
import webserver.http.common.StatusCode;
import webserver.http.request.Request;
import webserver.http.request.param.CookieParams;
import webserver.http.response.Response;
import webserver.http.response.ResponseBuilder;
import util.FileContentUtil;
import webserver.http.session.Session;
import webserver.http.session.SessionContainer;

import java.util.Optional;

import static webserver.http.common.ContentType.HTML;

public class StaticHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(StaticHandler.class);
    @Override
    public Response handle(Request request) {
        logger.debug("=====================Starting Handling===================");
        ResponseBuilder responseBuilder;
        String path = request.getRequestLine("path");
        CookieParams cookieParams = request.getCookie();
        String extension = FileContentUtil.getExtension(path);

        Optional<byte[]> body = FileContentUtil.getFileContent("static/"+ path);

        if (body.isEmpty()) {
            body = FileContentUtil.getFileContent("static/error/404.html");
            responseBuilder = new ResponseBuilder(StatusCode.NOT_FOUND, body.get(),HTML.getContentType(), getCurrentSession(cookieParams));
        } else {
            String contentType = ContentType.from(extension).getContentType();
            responseBuilder = new ResponseBuilder(StatusCode.OK, body.get(), contentType, getCurrentSession(cookieParams));
        }

        logger.debug("=====================End Handling===================");
        return responseBuilder.build();
    }

    @Override
    public Optional<Session> getCurrentSession(CookieParams cookieParams) {
        SessionContainer sessionContainer = SessionContainer.getInstance();
        return sessionContainer.getCurrentSession(cookieParams.get("sid"));
    }
}
