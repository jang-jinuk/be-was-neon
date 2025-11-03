package webserver.http.session;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    private final String id;
    private final Map<String, Object> attributes;

    public Session() {
        this.id = UUID.randomUUID().toString();
        this.attributes = new ConcurrentHashMap<>();
    }

    public String getId() {
        return id;
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void setAttributes(String key, Object value) {
        attributes.put(key, value);
    }
}
