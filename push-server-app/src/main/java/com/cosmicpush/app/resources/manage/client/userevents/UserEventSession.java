/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client.userevents;

import com.cosmicpush.common.requests.ApiRequest;
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

  private final List<ApiRequest> apiRequests = new ArrayList<ApiRequest>();

  public UserEventSession(UserEventPush userEvent) {
    this.updatedAt = userEvent.getCreatedAt();
    this.sessionId = ExceptionUtils.assertNotNull(userEvent.getSessionId(), "sessionId");
  }

  public UserEventSession(List<ApiRequest> requests) {
    this(requests.get(0).getUserEventPush());
    for (ApiRequest request : requests) {
      add(request);
    }
  }

  public void add(ApiRequest apiRequest) {
    UserEventPush userEvent = apiRequest.getUserEventPush();
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

    this.apiRequests.add(apiRequest);
    Collections.sort(apiRequests);
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

  public List<ApiRequest> getApiRequests() {
    return Collections.unmodifiableList(apiRequests);
  }

  public Set<ApiRequest> getLastThree() {
    Set<ApiRequest> set = new TreeSet<ApiRequest>();
    int max = Math.min(3, apiRequests.size());
    for (int i = apiRequests.size()-1; i >= apiRequests.size()-max; i--) {
      set.add(apiRequests.get(i));
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

  public boolean contains(ApiRequest apiRequest) {
    return apiRequests.contains(apiRequest);
  }

  public String toString() {
    return "Session " + sessionId;
  }
}


