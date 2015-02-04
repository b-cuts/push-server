package com.cosmicpush.pub.common;

import java.util.Map;

public class PushTraits {

  private final String clientName;
  private final String apiRequestId;
  private final Map<String, String> traits;

  public PushTraits(String apiRequestId, String clientName, Map<String, String> traits) {
    this.traits = traits;
    this.clientName = clientName;
    this.apiRequestId = apiRequestId;
  }

  public String getClientName() {
    return clientName;
  }

  public String getApiRequestId() {
    return apiRequestId;
  }

  public Map<String, String> getTraits() {
    return traits;
  }
}
