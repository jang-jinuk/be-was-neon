package webserver.http.session;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SessionContainer {
    private static volatile SessionContainer instance;
    private final Map<String, Session> sessions;

    private SessionContainer() {
        this.sessions = new ConcurrentHashMap<>();
    }

    public static SessionContainer getInstance() {
        if (instance == null) {
            synchronized (SessionContainer.class) {
                if (instance == null) {
                    instance = new SessionContainer();
                }
            }
        }
        return instance;
    }

    public Optional<Session> getCurrentSession(String loginSessionId) {
        if (sessions.containsKey(loginSessionId)) {
            return Optional.of(sessions.get(loginSessionId));
        }
        return Optional.empty();
    }

    public void add(Session session) {
        sessions.put(session.getId(), session);
    }

    public boolean containKey(String sessionId) {
        return sessions.containsKey(sessionId);
    }

    public void remove(String sessionId) {
        sessions.remove(sessionId);
    }
}
