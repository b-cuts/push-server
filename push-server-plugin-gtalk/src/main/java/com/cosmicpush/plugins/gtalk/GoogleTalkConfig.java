/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.gtalk;

import com.cosmicpush.common.plugins.PluginConfig;
import com.cosmicpush.pub.internal.RequestErrors;
import com.couchace.annotations.*;
import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import org.crazyyak.dev.common.EqualsUtils;

@CouchEntity(GoogleTalkConfigStore.GOOGLE_TALK_CONFIG_DESIGN_NAME)
public class GoogleTalkConfig implements PluginConfig, Serializable {

  private String configId;
  private String revision;

  private String apiClientId;

  private String userName;
  private String password;
  private String recipientOverride;
  private String testAddress;

  public GoogleTalkConfig() {
  }

  @JsonCreator
  public GoogleTalkConfig(@JacksonInject("configId") String configId,
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

  public GoogleTalkConfig apply(UpdateGoogleTalkConfigAction push) {
    push.validate(new RequestErrors()).assertNoErrors();

    if (apiClientId != null && EqualsUtils.objectsNotEqual(apiClientId, push.getApiClient().getApiClientId())) {
      String msg = "The specified push and this class are not for the same API Client ID.";
      throw new IllegalArgumentException(msg);
    }

    this.apiClientId = push.getApiClient().getApiClientId();
    this.configId = GoogleTalkConfigStore.toDocumentId(push.getApiClient());

    this.userName = push.getUserName();
    this.password = push.getPassword();
    this.recipientOverride = push.getRecipientOverride();
    this.testAddress = push.getTestAddress();

    return this;
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
