/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client.userevents;

import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.pub.push.UserEventPush;
import org.crazyyak.dev.common.EqualsUtils;
import org.crazyyak.dev.common.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

public class UserEventGroup implements Comparable<UserEventGroup> {

  private final String deviceId;
  private LocalDateTime updatedAt;
  private final List<UserEventSession> sessions = new ArrayList<UserEventSession>();

  private int count;
  private String userName;
  private String ipAddress;

  private String botName;

  public UserEventGroup(UserEventPush userEvent) {
    this.deviceId = userEvent.getDeviceId();
    this.updatedAt = userEvent.getCreatedAt();
  }

  public UserEventGroup(List<PushRequest> pushRequests) {
    this(pushRequests.get(0).getUserEventPush());
    for (PushRequest pushRequest : pushRequests) {
      add(pushRequest);
    }
  }

  public void add(PushRequest pushRequest) {
    UserEventPush userEvent = pushRequest.getUserEventPush();
    if (userEvent.isSendStory()) return;

    if (EqualsUtils.objectsNotEqual(deviceId, userEvent.getDeviceId())) {
      String msg = String.format("Invalid device id for push %s.", userEvent.getDeviceId());
      throw new IllegalArgumentException(msg);
    }

    UserEventSession session = findSession(userEvent);

    if (session == null) {
      session = new UserEventSession(userEvent);
      sessions.add(session);
    }

    session.add(pushRequest);

    if (userEvent.getCreatedAt().isAfter(this.updatedAt)) {
      this.updatedAt = userEvent.getCreatedAt();
    }
    if (StringUtils.isBlank(this.userName)) {
      this.userName = session.getUserName();
    }

    this.ipAddress = session.getIpAddress();

    count++;
    Collections.sort(sessions);
  }

  public UserEventSession findSession(UserEventPush userEvent) {
    for (UserEventSession session : sessions) {
      if (session.getSessionId().equals(userEvent.getSessionId())) {
        return session;
      }
    }
    return null;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public String getUserName() {
    return userName;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public String getBotName() {
    return botName;
  }

  public List<UserEventSession> getSessions() {
    return Collections.unmodifiableList(sessions);
  }

  public int getCount() {
    return count;
  }

  public Set<PushRequest> getLastThree() {
    return sessions.isEmpty() ?
        Collections.<PushRequest>emptySet() :
        sessions.get(0).getLastThree();
  }

  public boolean equals(Object o) {
    if (o instanceof UserEventGroup) {
      UserEventGroup that = (UserEventGroup)o;
      return this.getDeviceId().equals(that.getDeviceId());
    }
    return false;
  }

  @Override
  public int compareTo(UserEventGroup that) {
    int diff = this.getUpdatedAt().compareTo(that.getUpdatedAt());
    if (diff != 0) return diff;

    String thisDeviceId = this.getDeviceId();
    if (thisDeviceId == null) thisDeviceId = "";

    String thatDeviceId = that.getDeviceId();
    if (thatDeviceId == null) thatDeviceId = "";

    return thisDeviceId.compareTo(thatDeviceId);
  }

  public String toString() {
    return "Device " + deviceId;
  }

  private static final Map<String,String> botsMap = new HashMap<>();
  static {
    botsMap.put("googlebot", "Googlebot");
    botsMap.put("bing", "Bingbot");
    botsMap.put("Baiduspider", "Baidu Spider");
  }
}
