package webserver.http.response.handler;

import webserver.http.request.Request;
import webserver.http.request.param.CookieParams;
import webserver.http.response.Response;
import webserver.http.session.Session;

import java.io.IOException;
import java.util.Optional;

public interface Handler {
    Response handle(Request request) throws IOException;
    Optional<Session> getCurrentSession(CookieParams cookieParams);
}
