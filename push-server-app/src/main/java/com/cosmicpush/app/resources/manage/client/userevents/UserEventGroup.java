/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client.userevents;

import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.push.UserEventPush;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.*;
import org.crazyyak.dev.common.*;

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

  public UserEventGroup(List<ApiRequest> apiRequests) {
    this(apiRequests.get(0).getUserEventPush());
    for (ApiRequest apiRequest : apiRequests) {
      add(apiRequest);
    }
  }

  public void add(ApiRequest apiRequest) {
    UserEventPush userEvent = apiRequest.getUserEventPush();
    if (userEvent.isSendStory()) return;

    if (BeanUtils.objectsNotEqual(deviceId, userEvent.getDeviceId())) {
      String msg = String.format("Invalid device id for push %s.", userEvent.getDeviceId());
      throw new IllegalArgumentException(msg);
    }

    UserEventSession session = findSession(userEvent);

    if (session == null) {
      session = new UserEventSession(userEvent);
      sessions.add(session);
    }

    session.add(apiRequest);

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

  @JsonIgnore
  public java.util.Date getUpdatedAtUtilDate() {
    return DateUtils.toUtilDate(updatedAt);
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

  public Set<ApiRequest> getLastThree() {
    return sessions.isEmpty() ?
        Collections.<ApiRequest>emptySet() :
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
