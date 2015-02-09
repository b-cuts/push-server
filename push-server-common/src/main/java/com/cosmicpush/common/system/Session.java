package com.cosmicpush.common.system;

import org.crazyyak.dev.common.id.uuid.TimeUuid;

public class Session {

  private final String username;
  private final String sessionId;
  private final long sessionDuration;
  private String lastMessage;

  private long expiresAt;

  /**
   * Creates a new session
   * @param sessionDuration the life of the session in milliseconds
   * @param username the username for the current session
   */
  public Session(long sessionDuration, String username) {
    this.username = username;
    this.sessionDuration = sessionDuration;
    this.sessionId = TimeUuid.randomUUID().toString();

    renew();
  }

  public String getSessionId() {
    return sessionId;
  }

  public String getUsername() {
    return username;
  }

  public long getSessionDuration() {
    return sessionDuration;
  }

  public long getExpiresAt() {
    return expiresAt;
  }

  /**
   * Updates the session's expiration to the current time + session duration.
   */
  public synchronized void renew() {
    expiresAt = System.currentTimeMillis() + sessionDuration;
  }

  public boolean isExpired() {
    return System.currentTimeMillis() >= expiresAt;
  }

  public boolean isNonExpired() {
    return System.currentTimeMillis() < expiresAt;
  }

  public String getLastMessage() {
    return lastMessage;
  }

  public void setLastMessage(String lastMessage) {
    this.lastMessage = lastMessage;
  }

  public int getSecondsToExpire() {
    long remaining = expiresAt - System.currentTimeMillis();
    return (int)(remaining / 1000);
  }
}
