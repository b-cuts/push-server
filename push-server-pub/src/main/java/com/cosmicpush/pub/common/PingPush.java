package com.cosmicpush.pub.common;

import com.cosmicpush.pub.internal.PushUtils;
import com.cosmicpush.pub.internal.RequestErrors;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.InetAddress;
import java.util.Collections;
import java.util.Map;

public class PingPush implements Push {

  public static PushType PUSH_TYPE = new PushType(PingPush.class, "ping", "Ping");

  private final String remoteHost;
  private final String remoteAddress;

  private PingPush(@JsonProperty("remoteHost") String remoteHost,
                   @JsonProperty("remoteAddress") String remoteAddress) {

    this.remoteHost = remoteHost;
    this.remoteAddress = remoteAddress;
  }

  @Override
  @JsonIgnore
  public String getCallbackUrl() {
    return null;
  }

  @Override
  public PushType getPushType() {
    return PUSH_TYPE;
  }

  @Override
  public Map<String, String> getTraits() {
    return Collections.emptyMap();
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
  public RequestErrors validate(RequestErrors errors) {
    return errors;
  }

  public static PingPush newPush() {
    InetAddress remoteAddress = PushUtils.getLocalHost();
    return new PingPush(remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress());
  }
}
