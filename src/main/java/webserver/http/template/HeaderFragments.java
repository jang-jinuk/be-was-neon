package webserver.http.template;

public enum HeaderFragments {
    HEADER_LOGGED_IN(true, "{{header}}", "header_logged_in.html"),
    HEADER_LOGGED_OUT(false, "{{header}}","header_logged_out.html");

    private final boolean loginStatus;
    private final String placeholder;
    private final String templatePath;

    HeaderFragments(boolean loginStatus, String placeholder, String templatePath) {
        this.loginStatus = loginStatus;
        this.placeholder = placeholder;
        this.templatePath = templatePath;
    }

    public boolean getLoginStatus() {
        return loginStatus;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getTemplatePath() {
        return templatePath;
    }
}
