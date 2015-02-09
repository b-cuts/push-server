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
import com.cosmicpush.pub.internal.PushUtils;
import com.cosmicpush.pub.internal.RequestErrors;
import com.cosmicpush.pub.lqnotify.LqAttachment;
import com.cosmicpush.pub.lqnotify.LqExceptionInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.crazyyak.dev.common.BeanUtils;
import org.crazyyak.dev.common.ReflectUtils;
import org.crazyyak.dev.common.StringUtils;

import java.io.Serializable;
import java.net.InetAddress;
import java.time.ZonedDateTime;
import java.util.*;

public class LqNotificationPush implements Push, Serializable {

  public static final PushType PUSH_TYPE = new PushType(LqNotificationPush.class, "liquid-notification", "Lq Notification");

  private final String topic;
  private final String summary;
  private final String trackingId;
  private final ZonedDateTime createdAt;
  private final LqExceptionInfo exceptionInfo;
  private final Map<String, String> traits = new HashMap<>();
  private final List<LqAttachment> attachments = new ArrayList<>();

  private final String remoteHost;
  private final String remoteAddress;

  private final String callbackUrl;

  @JsonCreator
  private LqNotificationPush(@JsonProperty("topic") String topic,
                             @JsonProperty("summary") String summary,
                             @JsonProperty("trackingId") String trackingId,
                             @JsonProperty("createdAt") ZonedDateTime createdAt,
                             @JsonProperty("exceptionInfo") LqExceptionInfo exceptionInfo,
                             @JsonProperty("attachmentsArg") Collection<LqAttachment> attachments,
                             @JsonProperty("callbackUrl") String callbackUrl,
                             @JsonProperty("remoteHost") String remoteHost,
                             @JsonProperty("remoteAddress") String remoteAddress,
                             @JsonProperty("traits") Map<String, String> traits) {

    this.topic = (topic != null) ? topic : "none";
    this.summary  = (summary != null) ? summary : "none";
    this.trackingId = trackingId;
    this.exceptionInfo = exceptionInfo;
    this.createdAt = (createdAt != null) ? createdAt : ZonedDateTime.now();

    if (traits != null) {
      this.traits.putAll(traits);
    }

    if (attachments != null) {
      this.attachments.addAll(attachments);
    }

    this.remoteHost = remoteHost;
    this.remoteAddress = remoteAddress;

    this.callbackUrl = callbackUrl;

    String[] keys = ReflectUtils.toArray(String.class, this.traits.keySet());

    for (String key : keys) {
      if (StringUtils.isBlank(this.traits.get(key))) {
        this.traits.remove(key);
      }
    }
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

  @Override
  public PushType getPushType() {
    return PUSH_TYPE;
  }

  @Override
  public Map<String,String> getTraits() {
    return traits;
  }

  public String getTopic() {
    return topic;
  }

  public String getSummary() {
    return summary;
  }

  public String getTrackingId() {
    return trackingId;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public LqExceptionInfo getExceptionInfo() {
    return exceptionInfo;
  }

  public List<LqAttachment> getAttachments() {
    return attachments;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) {
    return errors;
  }

  @Deprecated
  public static LqNotificationPush newPushV1(String summary,
                                             String callbackUrl,
                                             String remoteHost,
                                             String remoteAddress,
                                             Map<String,String> traits) {

    return new LqNotificationPush("unit-test", summary, null,
                                  ZonedDateTime.now(), null, Collections.emptyList(),
                                  callbackUrl, remoteHost, remoteAddress, traits);
  }

  public static LqNotificationPush newPush(String topic,
                                           String summary,
                                           String trackingId,
                                           String callbackUrl,
                                           String...traits) {

    return newPush(topic, summary, trackingId, null, Collections.emptyList(), callbackUrl, BeanUtils.toMap(traits));
  }

  public static LqNotificationPush newPush(String topic,
                                           String summary,
                                           String trackingId,
                                           String callbackUrl,
                                           Map<String,String> traits) {

    return newPush(topic, summary, trackingId, null, Collections.emptyList(), callbackUrl, traits);
  }

  public static LqNotificationPush newPush(String topic,
                                           String summary,
                                           String trackingId,
                                           Throwable throwable,
                                           String callbackUrl,
                                           String...traits) {

    return newPush(topic, summary, trackingId, throwable, Collections.emptyList(), callbackUrl, BeanUtils.toMap(traits));
  }

  public static LqNotificationPush newPush(String topic,
                                           String summary,
                                           String trackingId,
                                           Throwable throwable,
                                           String callbackUrl,
                                           Map<String,String> traits) {

    return newPush(topic, summary, trackingId, throwable, Collections.emptyList(), callbackUrl, traits);
  }

  public static LqNotificationPush newPush(String topic,
                                           String summary,
                                           String trackingId,
                                           Throwable throwable,
                                           Collection<LqAttachment> attachments,
                                           String callbackUrl,
                                           String...traits) {

    return newPush(topic, summary, trackingId, throwable, attachments, callbackUrl, BeanUtils.toMap(traits));
  }

  public static LqNotificationPush newPush(String topic,
                                           String summary,
                                           String trackingId,
                                           Throwable throwable,
                                           Collection<LqAttachment> attachments,
                                           String callbackUrl,
                                           Map<String,String> traits) {

    InetAddress remoteAddress = PushUtils.getLocalHost();
    LqExceptionInfo exceptionInfo = (throwable == null) ? null : LqExceptionInfo.create(throwable);

    return new LqNotificationPush(topic, summary, trackingId,
                                  ZonedDateTime.now(), exceptionInfo, attachments, callbackUrl,
                                  remoteAddress.getCanonicalHostName(),
                                  remoteAddress.getHostAddress(),
                                  traits);
  }
}
