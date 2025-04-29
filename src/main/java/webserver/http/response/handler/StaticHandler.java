package webserver.http.response.handler;

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
    @Override
    public Response handle(Request request) {
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

        return responseBuilder.build();
    }

    @Override
    public Optional<Session> getCurrentSession(CookieParams cookieParams) {
        SessionContainer sessionContainer = SessionContainer.getInstance();
        return sessionContainer.getCurrentSession(cookieParams.get("sid"));
    }
}
