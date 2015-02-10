package com.cosmicpush.common.system;

import org.crazyyak.dev.common.ReflectUtils;
import org.crazyyak.dev.common.StringUtils;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.Map;

public class SessionStore {

  public static final String SESSION_COOKIE_NAME = "session-id";
  private static final Object LOCK = new Object();

  private long sessionDuration;

  private Map<String,Session> map = new HashMap<>();

  /**
   * Creates a new session store
   * @param sessionDuration the life of the session in milliseconds
   */
  public SessionStore(long sessionDuration) {
    this.sessionDuration = sessionDuration;
  }

  public Session newSession(String username) {
    removeExpiredSessions();
    synchronized (LOCK) {
      Session session = new Session(sessionDuration, username);
      map.put(session.getSessionId(), session);
      return session;
    }
  }

  public long getSessionDuration() {
    return sessionDuration;
  }

  public Session getSession(String sessionId) {
    synchronized (LOCK) {
      Session session = map.get(sessionId);
      return (session != null && session.isNonExpired() ? session : null);
    }
  }

  public void remove(Session session) {
    remove(session == null ? null : session.getSessionId());
  }

  public void remove(String sessionId) {
    if (sessionId == null) {
      return;
    }
    synchronized (LOCK) {
      map.remove(sessionId);
    }
  }

  public void removeExpiredSessions() {
    synchronized (LOCK) {
      Session[] sessions = ReflectUtils.toArray(Session.class, map.values());
      for (Session session : sessions) {
        if (session.isExpired()) {
          map.remove(session.getSessionId());
        }
      }
    }
  }

  public boolean isValid(Session session) {
    if (session == null) return false;
    if (session.isExpired()) return false;
    return (map.containsKey(session.getSessionId()));
  }

  public Session getSession(ContainerRequestContext requestContext) {
    Cookie cookie = requestContext.getCookies().get(SESSION_COOKIE_NAME);
    if (cookie == null) {
      return null;
    }
    String sessionId = cookie.getValue();
    return getSession(sessionId);
  }

  public static NewCookie toCookie(UriInfo uriInfo, Session session) {

    int maxAge = (session == null) ? 0 : session.getSecondsToExpire();
    String sessionId = (session == null) ? null : session.getSessionId();

    String path = uriInfo.getBaseUri().toString();
    int pos = path.indexOf("://");
    if (pos >= 0) {
      pos = path.indexOf("/", pos+3);
      path = StringUtils.substring(path, pos, -1);
    }
    if (StringUtils.isBlank(path)) {
      path = "/";
    }

    return new NewCookie("session-id", sessionId, path, null, null, maxAge, false, true);
  }
}
