package webserver.http.response.handler;

import db.Database;
import exception.PasswordMismatchException;
import exception.UserNotFoundException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileContentUtil;
import webserver.http.request.param.BodyParams;
import webserver.http.request.Request;
import webserver.http.request.param.CookieParams;
import webserver.http.response.Response;
import webserver.http.response.ResponseBuilder;
import webserver.http.session.Session;
import webserver.http.session.SessionContainer;

import java.util.Optional;

import static webserver.http.common.ContentType.*;
import static webserver.http.common.StatusCode.*;
import static webserver.http.common.UrlPattern.*;

public class DynamicHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(DynamicHandler.class);

    @Override
    public Response handle(Request request) {
        String path = request.getRequestLine("path");
        CookieParams cookieParams = request.getCookie();
        Optional<String> sessionId = Optional.empty();

        try {
            if (path.equals(USER_CREATE.getPattern())) {
                createUser(request);
            }

            if (path.equals(USER_LOGIN.getPattern())) {
                sessionId = login(request);
            }

            if (path.equals(USER_LOGOUT.getPattern())) {
                logout(request);
            }

        } catch (UserNotFoundException | PasswordMismatchException e) {
            return handleError(e.getMessage(), getCurrentSession(cookieParams));
        }

        return new ResponseBuilder(FOUND, "/", sessionId, getCurrentSession(cookieParams)).build();
    }

    @Override
    public Optional<Session> getCurrentSession(CookieParams cookieParams) {
        SessionContainer sessionContainer = SessionContainer.getInstance();
        return sessionContainer.getCurrentSession(cookieParams.get("sid"));
    }

    private Response handleError(String errorMessage, Optional<Session> currentSession) {
        logger.error("요청 실패: {}",errorMessage);
        Optional<byte[]> errorBody = FileContentUtil.getFileContent("static/user/login_failed.html");
        return new ResponseBuilder(UNAUTHORIZED, errorBody.get(), HTML.getContentType(), currentSession).build();
    }

    private void createUser(Request request) {
        BodyParams body = request.getBody();

        String userId = body.get("userId");
        String password = body.get("password");
        String name = body.get("name");
        String email = body.get("email");
        User user = new User(userId, password, name, email);
        Database.addUser(user);
    }

    private Optional<String> login(Request request) {
        BodyParams body = request.getBody();

        String loginUserId = body.get("userId");
        String loginUserPw = body.get("password");

        User user = Database.findUserById(loginUserId);

        if (user == null) {
            throw new UserNotFoundException();
        }

        if (!loginUserPw.equals(user.getPassword())) {
            throw new PasswordMismatchException();
        }

        Session session = new Session();
        session.setAttributes("loginUser", user);

        SessionContainer sessionContainer = SessionContainer.getInstance();
        sessionContainer.add(session);

        return Optional.of(session.getId());
    }

    public void logout(Request request) {
        CookieParams cookie = request.getCookie();
        String loginUserSid = cookie.get("sid");

        SessionContainer sessionContainer = SessionContainer.getInstance();
        if (sessionContainer.containKey(loginUserSid)) {
            sessionContainer.remove(loginUserSid);
        }
    }
}
