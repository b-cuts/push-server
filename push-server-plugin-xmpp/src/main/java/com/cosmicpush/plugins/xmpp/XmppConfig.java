/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.xmpp;

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

@CouchEntity(XmppConfigStore.XMPP_CONFIG_DESIGN_NAME)
public class XmppConfig implements PluginConfig, Serializable {

  private String configId;
  private String revision;

  private String domainId;

  private String username;
  private String password;
  private String recipientOverride;
  private String testAddress;

  private String host;
  private String port;
  private String serviceName;

  public XmppConfig() {
  }

  @JsonCreator
  public XmppConfig(@JacksonInject("configId") String configId,
                    @JacksonInject("revision") String revision,
                    @JsonProperty("domainId") String domainId,
                    @JsonProperty("username") String username,
                    @JsonProperty("password") String password,
                    @JsonProperty("host") String host,
                    @JsonProperty("port") String port,
                    @JsonProperty("serviceName") String serviceName,
                    @JsonProperty("recipientOverride") String recipientOverride,
                    @JsonProperty("testAddress") String testAddress) {

    this.configId = configId;
    this.revision = revision;

    this.domainId = domainId;

    this.username = username;
    this.password = password;

    this.host = host;
    this.port = port;
    this.serviceName = serviceName;

    this.recipientOverride = recipientOverride;
    this.testAddress = testAddress;
  }

  public XmppConfig apply(UpdateXmppConfigAction action) {
    action.validate(new RequestErrors()).assertNoErrors();

    if (domainId != null && EqualsUtils.objectsNotEqual(domainId, action.getDomain().getDomainId())) {
      String msg = "The specified push and this class are not for the same domain.";
      throw new IllegalArgumentException(msg);
    }

    this.domainId = action.getDomain().getDomainId();
    this.configId = XmppConfigStore.toDocumentId(action.getDomain());

    this.username = action.getUsername();
    this.password = action.getPassword();

    this.host = action.getHost();
    this.port = action.getPort();
    this.serviceName = action.getServiceName();

    this.recipientOverride = action.getRecipientOverride();
    this.testAddress = action.getTestAddress();

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

  public String getUsername() {
    return username;
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

  public String getHost() {
    return host;
  }

  public String getPort() {
    return port;
  }

  public int getPortInt() {
    return (port == null) ? 0 : Integer.valueOf(port);
  }

  public String getServiceName() {
    return serviceName;
  }
}
