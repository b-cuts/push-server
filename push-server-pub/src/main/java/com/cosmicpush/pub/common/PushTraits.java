package com.cosmicpush.pub.common;

import java.util.Map;

public class PushTraits {

  private final String domainKey;
  private final String pushRequestId;
  private final Map<String, String> traits;

  public PushTraits(String pushRequestId, String domainKey, Map<String, String> traits) {
    this.traits = traits;
    this.domainKey = domainKey;
    this.pushRequestId = pushRequestId;
  }

  public String getDomainKey() {
    return domainKey;
  }

  public String getPushRequestId() {
    return pushRequestId;
  }

  public Map<String, String> getTraits() {
    return traits;
  }
}
