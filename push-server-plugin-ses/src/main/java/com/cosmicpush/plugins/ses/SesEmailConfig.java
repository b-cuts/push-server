/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.ses;

import com.cosmicpush.common.plugins.PluginConfig;
import com.cosmicpush.pub.internal.RequestErrors;
import com.couchace.annotations.CouchEntity;
import com.couchace.annotations.CouchId;
import com.couchace.annotations.CouchRevision;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.crazyyak.dev.common.EqualsUtils;

import java.io.Serializable;

@CouchEntity(SesEmailConfigStore.SES_EMAIL_CONFIG_DESIGN_NAME)
public class SesEmailConfig implements PluginConfig, Serializable {

  private String configId;
  private String revision;

  private String domainId;

  private String accessKeyId;
  private String secretKey;
  private String recipientOverride;
  private String testToAddress;
  private String testFromAddress;

  public SesEmailConfig() {
  }

  @JsonCreator
  public SesEmailConfig(@JacksonInject("configId") String configId,
                        @JacksonInject("revision") String revision,
                        @JsonProperty("domainId") String domainId,
                        @JsonProperty("accessKeyId") String accessKeyId,
                        @JsonProperty("secretKey") String secretKey,
                        @JsonProperty("recipientOverride") String recipientOverride,
                        @JsonProperty("testToAddress") String testToAddress,
                        @JsonProperty("testFromAddress") String testFromAddress) {

    this.configId = configId;
    this.revision = revision;

    this.domainId = domainId;

    this.accessKeyId = accessKeyId;
    this.secretKey = secretKey;

    this.recipientOverride = recipientOverride;
    this.testToAddress = testToAddress;
    this.testFromAddress = testFromAddress;
  }

  public SesEmailConfig apply(UpdateSesEmailConfigAction push) {
    push.validate(new RequestErrors()).assertNoErrors();

    if (domainId != null && EqualsUtils.objectsNotEqual(domainId, push.getDomain().getDomainId())) {
      String msg = "The specified push and this class are not for the same domain.";
      throw new IllegalArgumentException(msg);
    }

    this.domainId = push.getDomain().getDomainId();
    this.configId = SesEmailConfigStore.toDocumentId(push.getDomain());

    this.accessKeyId = push.getAccessKeyId();
    this.secretKey = push.getSecretKey();
    this.testToAddress = push.getTestToAddress();
    this.testFromAddress = push.getTestFromAddress();
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

  public String getDomainId() {
    return domainId;
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

  public String getTestToAddress() {
    return testToAddress;
  }

  public String getTestFromAddress() {
    return testFromAddress;
  }
}
