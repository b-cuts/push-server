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
import java.util.*;
import org.crazyyak.dev.common.*;

public class NotificationPush implements Push, Serializable {

  private final String message;
  private final LinkedHashMap<String,String> traits = new LinkedHashMap<>();

  private final String callbackUrl;
  private final PushType pushType = PushType.notification;

  public NotificationPush(String message,
                          String callbackUrl,
                          String...traits) {
    this(message, callbackUrl, BeanUtils.toMap(traits));
  }

  @JsonCreator
  public NotificationPush(@JsonProperty("message") String message,
                          @JsonProperty("callbackUrl") String callbackUrl,
                          @JsonProperty("traits") Map<String, String> traits) {

    this.message = (message == null) ? "No message" : message.trim();
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

  public LinkedHashMap<String,String> getTraits() {
    return traits;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) {
    ValidationUtils.requireValue(errors, message, "The message must be specified.");
    return errors;
  }
}
