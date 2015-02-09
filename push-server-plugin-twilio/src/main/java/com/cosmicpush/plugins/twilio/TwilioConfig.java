/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.twilio;

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

@CouchEntity(TwilioConfigStore.TWILIO_CONFIG_DESIGN_NAME)
public class TwilioConfig implements PluginConfig, Serializable {

  private String configId;
  private String revision;

  private String domainId;

  private String accountSid;
  private String authToken;
  private String fromPhoneNumber;
  private String recipient;
  private String message;

  public TwilioConfig() {}

  @JsonCreator
  public TwilioConfig(@JacksonInject("configId") String configId,
                      @JacksonInject("revision") String revision,
                      @JsonProperty("domainId") String domainId,
                      @JsonProperty("accountSid") String accountSid,
                      @JsonProperty("authToken") String authToken,
                      @JsonProperty("fromPhoneNumber") String fromPhoneNumber,
                      @JsonProperty("recipient") String recipient,
                      @JsonProperty("message") String message) {
    this.configId = configId;
    this.revision = revision;

    this.domainId = domainId;

    this.accountSid = accountSid;
    this.authToken = authToken;
    this.fromPhoneNumber = fromPhoneNumber;
    this.recipient = recipient;
    this.message = message;
  }

  public TwilioConfig apply(UpdateTwilioConfigAction configAction) {
    configAction.validate(new RequestErrors()).assertNoErrors();

    if (domainId != null && EqualsUtils.objectsNotEqual(domainId, configAction.getDomain().getDomainId())) {
      String msg = "The specified Update Config Action domain type and this class are not for the same domain.";
      throw new IllegalArgumentException(msg);
    }

    this.domainId = configAction.getDomain().getDomainId();
    this.configId = TwilioConfigStore.toDocumentId(configAction.getDomain());

    this.accountSid = configAction.getAccountSid();
    this.authToken = configAction.getAuthToken();
    this.fromPhoneNumber = configAction.getFromPhoneNumber();
    this.recipient = configAction.getRecipient();
    this.message = configAction.getMessage();

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

  public String getAccountSid() {
    return accountSid;
  }

  public String getAuthToken() {
    return authToken;
  }

  public String getFromPhoneNumber() {
    return fromPhoneNumber;
  }

  public String getRecipient() {
    return recipient;
  }

  public String getMessage() {
    return message;
  }
}
