/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.twilio;

import com.cosmicpush.common.plugins.PluginConfig;
import com.couchace.annotations.CouchEntity;
import com.couchace.annotations.CouchId;
import com.couchace.annotations.CouchRevision;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@CouchEntity(TwilioConfigStore.TWILIO_CONFIG_DESIGN_NAME)
public class TwilioConfig implements PluginConfig, Serializable {

  private String configId;
  private String revision;

  private String domainId;

  private String userName;
  private String password;
  private String recipientOverride;
  private String testAddress;

  public TwilioConfig() {}

  @JsonCreator
  public TwilioConfig(@JacksonInject("configId") String configId,
                      @JacksonInject("revision") String revision,
                      @JsonProperty("domainId") String domainId,
                      @JsonProperty("userName") String userName,
                      @JsonProperty("password") String password,
                      @JsonProperty("recipientOverride") String recipientOverride,
                      @JsonProperty("testAddress") String testAddress) {
    this.configId = configId;
    this.revision = revision;

    this.domainId = domainId;

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

  public String getDomainId() {
    return domainId;
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
