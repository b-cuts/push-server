/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cosmicpush.pub.push;

import com.cosmicpush.pub.common.*;
import com.cosmicpush.pub.internal.*;
import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import org.crazyyak.dev.common.*;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;

public class UserEventPush implements Push, Comparable<UserEventPush>, Serializable {

  private final boolean sendStory;

  private final String deviceId;
  private final String sessionId;

  private final String ipAddress;
  private final String userName;
  private final LocalDateTime createdAt;

  private final String message;
  private final LinkedHashMap<String,String> traits = new LinkedHashMap<String,String>();

  private final UserAgent userAgent;

  private final String callbackUrl;
  private final PushType pushType = PushType.userEvent;

  private UserEventPush(String sessionId,
                        LocalDateTime createdAt,
                        String callbackUrl) {

    this.sendStory = true;
    this.sessionId = sessionId;
    this.createdAt = createdAt;
    this.callbackUrl = callbackUrl;

    deviceId = null;
    ipAddress = null;
    userName = null;
    message = null;

    userAgent = null;
  }

  public UserEventPush(CpRemoteClient remoteClient, LocalDateTime createdAt, String message, UserAgent userAgent, String callbackUrl) {
    this(remoteClient, createdAt, message, new LinkedHashMap<String,String>(), userAgent, callbackUrl);
  }

  public UserEventPush(CpRemoteClient remoteClient,
                       LocalDateTime createdAt,
                       String message,
                       LinkedHashMap<String, String> traits,
                       UserAgent userAgent,
                       String callbackUrl) {

    ExceptionUtils.assertNotNull(remoteClient, "remoteClient");

    this.sendStory = false;
    this.deviceId = remoteClient.getDeviceId();
    this.sessionId = ExceptionUtils.assertNotNull(remoteClient.getSessionId(), "sessionId");

    this.userName = remoteClient.getUserName();
    this.ipAddress = ExceptionUtils.assertNotNull(remoteClient.getIpAddress(), "ipAddress");

    this.createdAt = ExceptionUtils.assertNotNull(createdAt, "createdAt");

    this.message = (message == null) ? "No message" : message.trim();
    this.traits.putAll(traits);

    this.userAgent = userAgent;
    this.callbackUrl = callbackUrl;
  }

  @JsonCreator
  public UserEventPush(@JsonProperty("deviceId") String deviceId,
                       @JsonProperty("sessionId") String sessionId,
                       @JsonProperty("userName") String userName,
                       @JsonProperty("ipAddress") String ipAddress,
                       @JsonProperty("createdAt") LocalDateTime createdAt,
                       @JsonProperty("message") String message,
                       @JsonProperty("traits") Map<String, String> traits,
                       @JsonProperty("userAgent") UserAgent userAgent,
                       @JsonProperty("callbackUrl") String callbackUrl) {

    this.sendStory = false;
    this.deviceId = deviceId;
    this.sessionId = sessionId;

    this.userName = userName;
    this.ipAddress = ipAddress;

    this.createdAt = createdAt;

    this.message = (message == null) ? "No message" : message.trim();
    if (traits != null) {
      this.traits.putAll(traits);
    }

    this.userAgent = userAgent;
    this.callbackUrl = callbackUrl;
  }

  @Override
  public String getCallbackUrl() {
    return callbackUrl;
  }

  public boolean isSendStory() {
    return sendStory;
  }

  @Override
  public PushType getPushType() {
    return pushType;
  }

  public String getMessage() {
    return message;
  }

  public String getUserName() {
    return userName;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public String getSessionId() {
    return sessionId;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  /**
   * Provided for Backwards compatibility with Java 7 libraries.
   * @return The value of createdAt as a java.util.Date
   */
  @JsonIgnore
  public java.util.Date getCreatedAtUtilDate() {
    return DateUtils.toUtilDate(createdAt);
  }

  public LinkedHashMap<String,String> getTraits() {
    return traits;
  }

  public UserAgent getUserAgent() {
    return userAgent;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) {
    if (isSendStory() == false) {
      ValidationUtils.requireValue(errors, message, "The message must be specified.");
    }
    return errors;
  }

  @Override
  public int compareTo(UserEventPush that) {
    return this.getCreatedAt().compareTo(that.getCreatedAt());
  }

  public boolean containsTrait(String key, Object value) {
    if (traits == null) return false;
    return BeanUtils.objectsEqual(traits.get(key), (value == null ? null : value.toString()));
  }

  public String toString() {
    return message;
  }

  public static UserEventPush sendStory(String sessionId, LocalDateTime createdAt, String callbackUrl) {
    return new UserEventPush(sessionId, createdAt, callbackUrl);
  }
}
