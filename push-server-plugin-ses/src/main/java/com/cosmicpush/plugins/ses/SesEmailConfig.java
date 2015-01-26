/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.ses;

import com.cosmicpush.common.plugins.PluginConfig;
import com.cosmicpush.pub.internal.RequestErrors;
import com.couchace.annotations.*;
import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import org.crazyyak.dev.common.EqualsUtils;

@CouchEntity(SesEmailConfigStore.SES_EMAIL_CONFIG_DESIGN_NAME)
public class SesEmailConfig implements PluginConfig, Serializable {

  private String configId;
  private String revision;

  private String apiClientId;

  private String accessKeyId;
  private String secretKey;
  private String recipientOverride;
  private String testAddress;

  public SesEmailConfig() {
  }

  @JsonCreator
  public SesEmailConfig(@JacksonInject("configId") String configId,
                        @JacksonInject("revision") String revision,
                        @JsonProperty("apiClientId") String apiClientId,
                        @JsonProperty("accessKeyId") String accessKeyId,
                        @JsonProperty("secretKey") String secretKey,
                        @JsonProperty("recipientOverride") String recipientOverride,
                        @JsonProperty("testAddress") String testAddress) {

    this.configId = configId;
    this.revision = revision;

    this.apiClientId = apiClientId;

    this.accessKeyId = accessKeyId;
    this.secretKey = secretKey;

    this.recipientOverride = recipientOverride;
    this.testAddress = testAddress;
  }

  public SesEmailConfig apply(UpdateSesEmailConfigAction push) {
    push.validate(new RequestErrors()).assertNoErrors();

    if (apiClientId != null && EqualsUtils.objectsNotEqual(apiClientId, push.getApiClient().getApiClientId())) {
      String msg = "The specified push and this class are not for the same API Client ID.";
      throw new IllegalArgumentException(msg);
    }

    this.apiClientId = push.getApiClient().getApiClientId();
    this.configId = SesEmailConfigStore.toDocumentId(push.getApiClient());

    this.accessKeyId = push.getAccessKeyId();
    this.secretKey = push.getSecretKey();
    this.testAddress = push.getTestAddress();
    this.recipientOverride = push.getRecipientOverride();

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

  public String getAccessKeyId() {
    return accessKeyId;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public String getRecipientOverride() {
    return recipientOverride;
  }

  public String getTestAddress() {
    return testAddress;
  }
}
