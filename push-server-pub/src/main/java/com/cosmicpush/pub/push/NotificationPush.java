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
import com.cosmicpush.pub.internal.RequestErrors;
import com.cosmicpush.pub.internal.ValidationUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.crazyyak.dev.common.BeanUtils;
import org.crazyyak.dev.common.ReflectUtils;
import org.crazyyak.dev.common.StringUtils;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.LinkedHashMap;
import java.util.Map;

public class NotificationPush implements Push, Serializable {

  private final String message;
  private final LinkedHashMap<String,String> traits = new LinkedHashMap<>();

  private final String remoteHost;
  private final String remoteAddress;

  private final String callbackUrl;

  private final PushType pushType = PushType.notification;

  @JsonCreator
  private NotificationPush(@JsonProperty("message") String message,
                          @JsonProperty("callbackUrl") String callbackUrl,
                          @JsonProperty("remoteHost") String remoteHost,
                          @JsonProperty("remoteAddress") String remoteAddress,
                          @JsonProperty("traits") Map<String, String> traits) {

    this.message = (message == null) ? "No message" : message.trim();

    this.remoteHost = remoteHost;
    this.remoteAddress = remoteAddress;

    this.callbackUrl = callbackUrl;

    // Get a list of all the keys so that we can loop on the map
    // and remove anything without an actual value (purge nulls).
    if (traits != null) {
      this.traits.putAll(traits);
    }
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
    return pushType;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public Map<String,String> getTraits() {
    return traits;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) {
    ValidationUtils.requireValue(errors, message, "The message must be specified.");
    return errors;
  }

  public static NotificationPush newPush(String message,
                                         String callbackUrl,
                                         InetAddress remoteAddress,
                                         String...traits) {

    return new NotificationPush(message, callbackUrl,
                                remoteAddress.getCanonicalHostName(),
                                remoteAddress.getHostAddress(),
                                BeanUtils.toMap(traits));
  }

  public static NotificationPush newPush(String message,
                                         String callbackUrl,
                                         InetAddress remoteAddress,
                                         Map<String,String> traits) {

    return new NotificationPush(message, callbackUrl,
                                remoteAddress.getCanonicalHostName(),
                                remoteAddress.getHostAddress(),
                                traits);
  }
}
