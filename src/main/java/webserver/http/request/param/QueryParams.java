package webserver.http.request.param;

import java.util.Map;

public class QueryParams {
    private final Map<String, String> params;

    public QueryParams(Map<String, String> params) {
        this.params = params;
    }

    public String get(String key) {
        return params.get(key);
    }
}
