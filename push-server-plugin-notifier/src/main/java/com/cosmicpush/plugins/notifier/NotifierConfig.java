/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.notifier;

import com.cosmicpush.common.plugins.PluginConfig;
import com.couchace.annotations.*;
import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;

@CouchEntity(NotifierConfigStore.NOTIFIER_CONFIG_DESIGN_NAME)
public class NotifierConfig implements PluginConfig, Serializable {

  private String configId;
  private String revision;

  private String apiClientId;

  private String userName;
  private String password;
  private String recipientOverride;
  private String testAddress;

  public NotifierConfig() {
  }

  @JsonCreator
  public NotifierConfig(@JacksonInject("configId") String configId,
                        @JacksonInject("revision") String revision,
                        @JsonProperty("apiClientId") String apiClientId,
                        @JsonProperty("userName") String userName,
                        @JsonProperty("password") String password,
                        @JsonProperty("recipientOverride") String recipientOverride,
                        @JsonProperty("testAddress") String testAddress) {

    this.configId = configId;
    this.revision = revision;

    this.apiClientId = apiClientId;

    this.userName = userName;
    this.password = password;
    this.recipientOverride = recipientOverride;
    this.testAddress = testAddress;
  }

  @CouchId
  public String getConfigId() {
    return configId;
  }

  @CouchRevision
  public String getRevision() {
    return revision;
  }

  public String getApiClientId() {
    return apiClientId;
  }

  public String getUserName() {
    return userName;
  }

  public String getPassword() {
    return password;
  }

  public String getRecipientOverride() {
    return recipientOverride;
  }

  public String getTestAddress() {
    return testAddress;
  }
}
