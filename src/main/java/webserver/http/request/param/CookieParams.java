package webserver.http.request.param;

import java.util.Map;

public class CookieParams {
    private final Map<String, String> params;

    public CookieParams(Map<String, String> params) {
        this.params = params;
    }

    public String get(String key) {
        return params.get(key);
    }
}
