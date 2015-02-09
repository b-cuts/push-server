/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client.userevents;

import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.pub.common.UserAgent;
import com.cosmicpush.pub.push.UserEventPush;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;
import org.crazyyak.dev.common.StringUtils;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;

public class UserEventSession implements Comparable<UserEventSession> {

  private final String sessionId;

  private String ipAddress;
  private String hostAddress;
  private UserAgent userAgent;

  private String userName;
  private LocalDateTime updatedAt;

  private final List<PushRequest> pushRequests = new ArrayList<PushRequest>();

  public UserEventSession(UserEventPush userEvent) {
    this.updatedAt = userEvent.getCreatedAt();
    this.sessionId = ExceptionUtils.assertNotNull(userEvent.getSessionId(), "sessionId");
  }

  public UserEventSession(List<PushRequest> requests) {
    this(requests.get(0).getUserEventPush());
    for (PushRequest request : requests) {
      add(request);
    }
  }

  public void add(PushRequest pushRequest) {
    UserEventPush userEvent = pushRequest.getUserEventPush();
    if (userEvent.isSendStory()) return;

    if (userEvent.getCreatedAt().isAfter(this.updatedAt)) {
      this.updatedAt = userEvent.getCreatedAt();
    }

    if (StringUtils.isBlank(this.userName)) {
      this.userName = userEvent.getUserName();
    }

    if (StringUtils.isBlank(this.ipAddress)) {
      this.ipAddress = userEvent.getIpAddress();
    }

    if (this.userAgent == null) {
      this.userAgent = userEvent.getUserAgent();
    }

    this.pushRequests.add(pushRequest);
    Collections.sort(pushRequests);
  }

  public String getSessionId() {
    return sessionId;
  }

  public String getUserName() {
    return userName;
  }

  public UserAgent getUserAgent() {
    return userAgent;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public String getHostAddress() {
    if (hostAddress == null) {
      InetAddress inetAddress = null;
      try { inetAddress = InetAddress.getByName(ipAddress);
      } catch (UnknownHostException ignored) {/*ignored*/}
      hostAddress = (inetAddress == null) ? null : inetAddress.getCanonicalHostName();
    }
    return hostAddress;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public List<PushRequest> getPushRequests() {
    return Collections.unmodifiableList(pushRequests);
  }

  public Set<PushRequest> getLastThree() {
    Set<PushRequest> set = new TreeSet<PushRequest>();
    int max = Math.min(3, pushRequests.size());
    for (int i = pushRequests.size()-1; i >= pushRequests.size()-max; i--) {
      set.add(pushRequests.get(i));
    }
    return set;
  }

  public boolean equals(Object o) {
    if (o instanceof UserEventSession) {
      UserEventSession that = (UserEventSession)o;
      return this.getSessionId().equals(that.getSessionId());
    }
    return false;
  }

  @Override
  public int compareTo(UserEventSession that) {
    int diff = this.getUpdatedAt().compareTo(that.getUpdatedAt());
    if (diff != 0) return -1 * diff;

    return -1 * this.getSessionId().compareTo(that.getSessionId());
  }

  public boolean contains(PushRequest pushRequest) {
    return pushRequests.contains(pushRequest);
  }

  public String toString() {
    return "Session " + sessionId;
  }
}


