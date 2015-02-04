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

import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.common.UserAgent;
import com.cosmicpush.pub.internal.CpRemoteClient;
import com.cosmicpush.pub.internal.PushUtils;
import com.cosmicpush.pub.internal.RequestErrors;
import com.cosmicpush.pub.internal.ValidationUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.crazyyak.dev.common.BeanUtils;
import org.crazyyak.dev.common.DateUtils;
import org.crazyyak.dev.common.EqualsUtils;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;

import java.io.Serializable;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserEventPush implements Push, Comparable<UserEventPush>, Serializable {

  private final boolean sendStory;

  private final String deviceId;
  private final String sessionId;

  private final String ipAddress;
  private final String userName;
  private final LocalDateTime createdAt;

  private final String message;
  private final LinkedHashMap<String,String> traits = new LinkedHashMap<>();

  private final UserAgent userAgent;

  private final String remoteHost;
  private final String remoteAddress;

  private final String callbackUrl;

  private final PushType pushType = PushType.userEvent;

  @JsonCreator
  private UserEventPush(@JsonProperty("sendStory") boolean sendStory,
                        @JsonProperty("deviceId") String deviceId,
                        @JsonProperty("sessionId") String sessionId,
                        @JsonProperty("userName") String userName,
                        @JsonProperty("ipAddress") String ipAddress,
                        @JsonProperty("createdAt") LocalDateTime createdAt,
                        @JsonProperty("message") String message,
                        @JsonProperty("userAgent") UserAgent userAgent,
                        @JsonProperty("callbackUrl") String callbackUrl,
                        @JsonProperty("remoteHost") String remoteHost,
                        @JsonProperty("remoteAddress") String remoteAddress,
                        @JsonProperty("traits") Map<String, String> traits) {

    this.sendStory = sendStory;
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

    this.remoteHost = remoteHost;
    this.remoteAddress = remoteAddress;

    this.callbackUrl = callbackUrl;
  }

  @Override
  public String getRemoteHost() {
    return remoteHost;
  }

  @Override
  public String getRemoteAddress() {
    return remoteAddress;
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

  @Override
  public Map<String,String> getTraits() {
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
    return EqualsUtils.objectsEqual(traits.get(key), (value == null ? null : value.toString()));
  }

  public String toString() {
    return message;
  }

  public static UserEventPush sendStory(String sessionId, LocalDateTime createdAt, String callbackUrl) {

    InetAddress remoteAddress = PushUtils.getLocalHost();
    return new UserEventPush(
        true,
        null,
        sessionId,
        null,
        null,
        createdAt, null,
        null, callbackUrl,
        remoteAddress.getHostAddress(),
        remoteAddress.getCanonicalHostName(),
        Collections.emptyMap());
  }

  public static UserEventPush newPush(CpRemoteClient remoteClient,
                                      LocalDateTime createdAt,
                                      String message,
                                      UserAgent userAgent,
                                      String callbackUrl,
                                      String...traits) {

    return newPush(remoteClient, createdAt, message, userAgent, callbackUrl, BeanUtils.toMap(traits));
  }

  public static UserEventPush newPush(CpRemoteClient remoteClient,
                                      LocalDateTime createdAt,
                                      String message,
                                      UserAgent userAgent,
                                      String callbackUrl,
                                      Map<String, String> traits) {

    InetAddress remoteAddress = PushUtils.getLocalHost();
    ExceptionUtils.assertNotNull(remoteClient, "remoteClient");
    ExceptionUtils.assertNotNull(createdAt, "createdAt");
    ExceptionUtils.assertNotNull(remoteClient.getSessionId(), "sessionId");
    ExceptionUtils.assertNotNull(remoteClient.getIpAddress(), "ipAddress");

    message = (message == null) ? "No message" : message.trim();

    return new UserEventPush(
        false,
        remoteClient.getDeviceId(),
        remoteClient.getSessionId(),
        remoteClient.getUserName(),
        remoteClient.getIpAddress(),
        createdAt, message,
        userAgent, callbackUrl,
        remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(),
        traits);
  }
}
