/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.smtp;

import com.cosmicpush.common.config.SmtpAuthType;
import com.cosmicpush.common.plugins.PluginConfig;
import com.cosmicpush.pub.internal.RequestErrors;
import com.couchace.annotations.CouchEntity;
import com.couchace.annotations.CouchId;
import com.couchace.annotations.CouchRevision;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.crazyyak.dev.common.EqualsUtils;
import org.crazyyak.dev.common.StringUtils;

import java.io.Serializable;

@CouchEntity(SmtpEmailConfigStore.SMTP_EMAIL_CONFIG_DESIGN_NAME)
public class SmtpEmailConfig implements PluginConfig, Serializable {

  private String configId;
  private String revision;

  private String domainId;

  private String userName;
  private String password;

  private SmtpAuthType authType;
  private String serverName;
  private String portNumber;

  private String recipientOverride;
  private String testToAddress;
  private String testFromAddress;

  public SmtpEmailConfig() {
  }

  @JsonCreator
  public SmtpEmailConfig(@JacksonInject("configId") String configId,
                         @JacksonInject("revision") String revision,
                         @JsonProperty("domainId") String domainId,
                         @JsonProperty("userName") String userName,
                         @JsonProperty("password") String password,
                         @JsonProperty("authType") SmtpAuthType authType,
                         @JsonProperty("serverName") String serverName,
                         @JsonProperty("portNumber") String portNumber,
                         @JsonProperty("recipientOverride") String recipientOverride,
                         @JsonProperty("testToAddress") String testToAddress,
                         @JsonProperty("testFromAddress") String testFromAddress) {

    this.configId = configId;
    this.revision = revision;

    this.domainId = domainId;

    this.userName = userName;
    this.password = password;

    this.authType = authType;
    this.serverName = serverName;
    this.portNumber = portNumber;

    this.recipientOverride = recipientOverride;
    this.testToAddress = testToAddress;
    this.testFromAddress = testFromAddress;
  }

  public SmtpEmailConfig apply(UpdateSmtpEmailConfigAction action) {
    action.validate(new RequestErrors()).assertNoErrors();

    if (domainId != null && EqualsUtils.objectsNotEqual(domainId, action.getDomain().getDomainId())) {
      String msg = "The specified action and this class are not for the same domain.";
      throw new IllegalArgumentException(msg);
    }

    this.domainId = action.getDomain().getDomainId();
    this.configId = SmtpEmailConfigStore.toDocumentId(action.getDomain());

    this.userName = action.getUserName();
    this.password = action.getPassword();

    this.authType = action.getAuthType();
    this.serverName = action.getServerName();

    if (StringUtils.isNotBlank(action.getPortNumber())) {
      this.portNumber = action.getPortNumber();
    } else {
      this.portNumber = this.authType.getDefaultPort();
    }

    this.testToAddress = action.getTestToAddress();
    this.testFromAddress = action.getTestFromAddress();
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

  public String getDomainId() {
    return domainId;
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

  public String getTestToAddress() {
    return testToAddress;
  }

  public String getTestFromAddress() {
    return testFromAddress;
  }
}
