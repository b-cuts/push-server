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

package com.cosmicpush.pub.common;

import com.cosmicpush.pub.push.NotificationPush;
import com.cosmicpush.pub.push.OcsPush;
import com.cosmicpush.pub.push.TwilioPush;
import com.cosmicpush.pub.push.UserEventPush;

import java.util.HashMap;
import java.util.Map;

public class PushType implements Comparable<PushType>{

  private static Map<String,PushType> map = new HashMap<>();

  public static PushType find(String code) {
    if (map.containsKey(code) == false) {
      String msg = String.format("The push type \"%s\" was not found.%n", code);
      throw new IllegalArgumentException(msg);
    }
    return map.get(code);
  }

  public static void register(PushType pushType) {
    map.put(pushType.getCode(), pushType);
  }

  public static PushType notification = new PushType(NotificationPush.class, "notification", "Notification");
  public static PushType userEvent = new PushType(UserEventPush.class, "userEvent", "User Event");
  public static PushType ocs = new PushType(OcsPush.class, "ocs", "Office Communicator Server");
  public static PushType twilio = new PushType(TwilioPush.class, "twilio", "Twilio");

  private final String code;
  private final String label;
  private final Class<? extends Push> type;

  public PushType(String code) {
    PushType copy = PushType.find(code);
    this.code = copy.getCode();
    this.label = copy.getLabel();
    this.type = copy.getType();
  }

  public PushType(Class<? extends Push> type, String code, String label) {
    this.code = code;
    this.label = label;
    this.type = type;
    register(this);
  }

  public String getCode() {
    return code;
  }

  public String getLabel() {
    return label;
  }

  public Class<? extends Push> getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PushType pushType = (PushType) o;
    return code.equals(pushType.code);
  }

  @Override
  public int hashCode() {
    int result = PushType.class.hashCode();
    result = 31 * result + code.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return code;
  }

  @Override
  public int compareTo(PushType that) {
    return this.code.compareTo(that.code);
  }
}
