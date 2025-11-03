package webserver.http.request.param;

import java.util.Map;
import java.util.Set;

public class HeaderParams {
    private final Map<String, String> params;

    public HeaderParams(Map<String, String> params) {
        this.params = params;
    }

    public String get(String key) {
        return params.get(key);
    }

    public boolean containsKey(String key) {
        return params.containsKey(key);
    }

    public Set<Map.Entry<String, String>> entrySet() {
        return params.entrySet();
    }

}
