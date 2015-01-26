/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.smtp;

import com.cosmicpush.common.config.SmtpAuthType;
import com.cosmicpush.common.plugins.PluginConfig;
import com.cosmicpush.pub.internal.RequestErrors;
import com.couchace.annotations.*;
import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import org.crazyyak.dev.common.*;

@CouchEntity(SmtpEmailConfigStore.SMTP_EMAIL_CONFIG_DESIGN_NAME)
public class SmtpEmailConfig implements PluginConfig, Serializable {

  private String configId;
  private String revision;

  private String apiClientId;

  private String userName;
  private String password;

  private SmtpAuthType authType;
  private String serverName;
  private String portNumber;

  private String recipientOverride;
  private String testAddress;

  public SmtpEmailConfig() {
  }

  @JsonCreator
  public SmtpEmailConfig(@JacksonInject("configId") String configId,
                         @JacksonInject("revision") String revision,
                         @JsonProperty("apiClientId") String apiClientId,
                         @JsonProperty("userName") String userName,
                         @JsonProperty("password") String password,
                         @JsonProperty("authType") SmtpAuthType authType,
                         @JsonProperty("serverName") String serverName,
                         @JsonProperty("portNumber") String portNumber,
                         @JsonProperty("recipientOverride") String recipientOverride,
                         @JsonProperty("testAddress") String testAddress) {

    this.configId = configId;
    this.revision = revision;

    this.apiClientId = apiClientId;

    this.userName = userName;
    this.password = password;

    this.authType = authType;
    this.serverName = serverName;
    this.portNumber = portNumber;

    this.recipientOverride = recipientOverride;
    this.testAddress = testAddress;
  }

  public SmtpEmailConfig apply(UpdateSmtpEmailConfigAction action) {
    action.validate(new RequestErrors()).assertNoErrors();

    if (apiClientId != null && EqualsUtils.objectsNotEqual(apiClientId, action.getApiClient().getApiClientId())) {
      String msg = "The specified action and this class are not for the same API Client ID.";
      throw new IllegalArgumentException(msg);
    }

    this.apiClientId = action.getApiClient().getApiClientId();
    this.configId = SmtpEmailConfigStore.toDocumentId(action.getApiClient());

    this.userName = action.getUserName();
    this.password = action.getPassword();

    this.authType = action.getAuthType();
    this.serverName = action.getServerName();

    if (StringUtils.isNotBlank(action.getPortNumber())) {
      this.portNumber = action.getPortNumber();
    } else {
      this.portNumber = this.authType.getDefaultPort();
    }

    this.testAddress = action.getTestAddress();
    this.recipientOverride = action.getRecipientOverride();

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

  public SmtpAuthType getAuthType() {
    return authType;
  }

  public String getServerName() {
    return serverName;
  }

  public String getPortNumber() {
    return portNumber;
  }

  public String getRecipientOverride() {
    return recipientOverride;
  }

  public String getTestAddress() {
    return testAddress;
  }
}
