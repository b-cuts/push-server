package com.cosmicpush.app.deprecated;

import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.internal.PushUtils;
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

public class NotificationPushV1 implements Push, Serializable {

  public static final PushType PUSH_TYPE = new PushType(NotificationPushV1.class, "notification", "Notification");

  private final String message;
  private final LinkedHashMap<String,String> traits = new LinkedHashMap<>();

  private final String remoteHost;
  private final String remoteAddress;

  private final String callbackUrl;

  @JsonCreator
  private NotificationPushV1(@JsonProperty("message") String message,
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
    return PUSH_TYPE;
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

  public static NotificationPushV1 newPush(String message,
                                           String callbackUrl,
                                           String...traits) {

    InetAddress remoteAddress = PushUtils.getLocalHost();
    return new NotificationPushV1(message, callbackUrl,
                                  remoteAddress.getCanonicalHostName(),
                                  remoteAddress.getHostAddress(),
                                  BeanUtils.toMap(traits));
  }

  public static NotificationPushV1 newPush(String message,
                                           String callbackUrl,
                                           Map<String,String> traits) {

    InetAddress remoteAddress = PushUtils.getLocalHost();
    return new NotificationPushV1(message, callbackUrl,
                                  remoteAddress.getCanonicalHostName(),
                                  remoteAddress.getHostAddress(),
                                  traits);
  }
}

