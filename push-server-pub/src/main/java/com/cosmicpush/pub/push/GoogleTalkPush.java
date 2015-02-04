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
import java.util.Collections;
import java.util.Map;

@JsonIgnoreProperties({"imType"})
public class GoogleTalkPush implements Push, Serializable {

  public static PushType PUSH_TYPE = new PushType(GoogleTalkPush.class, "google-talk", "Google Talk IM");

  private final String recipient;
  private final String message;

  private final String callbackUrl;

  public GoogleTalkPush(@JsonProperty("recipient") String recipient,
                        @JsonProperty("message") String message,
                        @JsonProperty("callbackUrl") String callbackUrl) {

    this.recipient = recipient;
    this.message = message;
    this.callbackUrl = callbackUrl;
  }

  @Override
  public String getCallbackUrl() {
    return callbackUrl;
  }

  @Override
  public PushType getPushType() {
    return PUSH_TYPE;
  }

  public String getRecipient() {
    return recipient;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) {
    ValidationUtils.requireValue(errors, recipient, "The recipient must be specified.");
    ValidationUtils.requireValue(errors, message, "The chat message must be specified.");
    return errors;
  }

  public static GoogleTalkPush googleTalk(String recipient, String message, String callbackUrl) {
    return new GoogleTalkPush(recipient, message, callbackUrl);
  }

  @Override
  public Map<String, String> getTraits() {
    return Collections.emptyMap();
  }
}
