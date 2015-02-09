package com.cosmicpush.pub.common;

import java.util.Map;

public class PushTraits {

  private final String domainKey;
  private final String apiRequestId;
  private final Map<String, String> traits;

  public PushTraits(String apiRequestId, String domainKey, Map<String, String> traits) {
    this.traits = traits;
    this.domainKey = domainKey;
    this.apiRequestId = apiRequestId;
  }

  public String getDomainKey() {
    return domainKey;
  }

  public String getApiRequestId() {
    return apiRequestId;
  }

  public Map<String, String> getTraits() {
    return traits;
  }
}
