package webserver.http.request.param;

import java.util.Map;

public class BodyParams {
    private final Map<String, String> params;

    public BodyParams(Map<String, String> params) {
        this.params = params;
    }

    public String get(String key) {
        return params.get(key);
    }
}
