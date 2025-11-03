package webserver.http.common;

public enum UrlPattern {
    USER_CREATE("POST", "/user/create"),
    USER_LOGIN("POST","/user/login"),
    USER_LOGOUT("POST", "/user/logout");

    private final String method;
    private final String pattern;

    UrlPattern(String method, String pattern) {
        this.method = method;
        this.pattern = pattern;
    }

    public String getMethod() {
        return method;
    }

    public String getPattern() {
        return pattern;
    }

    public static boolean contain(String requestedMethod, String requestedPath) {
        for (UrlPattern urlPattern : UrlPattern.values()) {
            String methods = urlPattern.getMethod();
            String pattern = urlPattern.getPattern();
            if (methods.equals(requestedMethod) && pattern.equals(requestedPath)) return true;
        }

        return false;
    }
}
