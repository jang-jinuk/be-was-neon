package webserver.http.template;

import db.Database;
import model.User;
import util.FileContentUtil;
import webserver.http.session.Session;

import java.util.Collection;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TemplateEngine {

    private String templateHtml;
    private final Optional<Session> currentSession;

    public TemplateEngine(byte[] body, Optional<Session> currentSession) {
        this.templateHtml = new String(body);
        this.currentSession = currentSession;
    }

    public byte[] render() {
        templateHtml = renderHeader();

        if (currentSession.isPresent()) {
            templateHtml = renderLoginUserName();
        }

        templateHtml = renderUserList();

        return templateHtml.getBytes(UTF_8);
    }

    private String renderHeader() {
        String headerFragment = getHeaderFragment();
        return templateHtml.replace("{{header}}", headerFragment);
    }

    private String getHeaderFragment() {
        for (HeaderFragments fragment : HeaderFragments.values()) {
            if (fragment.getLoginStatus() == currentSession.isPresent()) {
                byte[] content = FileContentUtil.getFileContent("templates/" + fragment.getTemplatePath())
                        .orElse(new byte[0]);
                return new String(content);
            }
        }
        return "";
    }

    private String renderLoginUserName() {
        User loginUser = (User) currentSession.get().getAttribute("loginUser");
        return templateHtml.replace("{{name}}", loginUser.getName());
    }

    private String renderUserList() {
        Collection<User> users = Database.findAll();
        StringBuilder sb = new StringBuilder();

        for (User user : users) {
            byte[] content = FileContentUtil.getFileContent("templates/user_list.html").orElse(new byte[0]);
            String userListTemplate = new String(content);
            userListTemplate = userListTemplate.replace("{{name}}", user.getName());
            userListTemplate = userListTemplate.replace("{{email}}", user.getEmail());
            sb.append(userListTemplate);
        }

        return templateHtml.replace("{{user_list}}", sb.toString());
    }
}
