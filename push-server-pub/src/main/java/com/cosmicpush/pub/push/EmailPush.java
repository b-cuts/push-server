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
import com.cosmicpush.pub.internal.*;
import java.io.Serializable;
import java.util.*;
import org.crazyyak.dev.common.*;

public abstract class EmailPush implements Push, Serializable {

  private final String fromAddress;
  private final String toAddress;

  private final String emailSubject;
  private final String htmlContent;

  private final LinkedHashMap<String,String> traits = new LinkedHashMap<>();

  private final String callbackUrl;

  public EmailPush(String toAddress,
                   String fromAddress,
                   String emailSubject,
                   String htmlContent,
                   String callbackUrl,
                   String...traits) {
    this(toAddress, fromAddress, emailSubject, htmlContent, callbackUrl, BeanUtils.toMap(traits));
  }

  public EmailPush(String toAddress,
                   String fromAddress,
                   String emailSubject,
                   String htmlContent,
                   String callbackUrl,
                   Map<String, String> traits) {

    this.toAddress =   toAddress;
    this.fromAddress = fromAddress;

    this.emailSubject = emailSubject;

    String content = StringUtils.getTagContents(htmlContent, "body", 0);
    this.htmlContent = StringUtils.isNotBlank(content) ? content : htmlContent;

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

  public String getFromAddress() {
    return fromAddress;
  }

  public String getToAddress() {
    return toAddress;
  }

  public String getEmailSubject() {
    return emailSubject;
  }

  public String getHtmlContent() {
    return htmlContent;
  }

  @Override
  public String getCallbackUrl() {
    return callbackUrl;
  }

  public LinkedHashMap<String, String> getTraits() {
    return traits;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) {
    ValidationUtils.requireValue(errors, toAddress, "The \"to\" address must be specified.");
    ValidationUtils.requireValue(errors, fromAddress, "The \"from\" address must be specified.");

    if (StringUtils.isBlank(emailSubject) && StringUtils.isBlank(htmlContent)) {
      errors.add("At least the subject and/or the HTML content must be specified.");
    }

    return errors;
  }
}
