/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.xmpp;

import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.pub.internal.*;
import javax.ws.rs.core.MultivaluedMap;

public class UpdateXmppConfigAction implements ValidatableAction {

  private final Domain domain;

  private final String username;
  private final String password;
  private final String recipientOverride;
  private String testAddress;
  private String host;
  private String port;
  private String serviceName;

  public UpdateXmppConfigAction(Domain domain, MultivaluedMap<String, String> formParams) {

    this.domain = domain;

    this.username = formParams.getFirst("username");
    this.password = formParams.getFirst("password");

    this.host = formParams.getFirst("host");
    this.port = formParams.getFirst("port");
    this.serviceName = formParams.getFirst("serviceName");

    this.testAddress = formParams.getFirst("testAddress");
    this.recipientOverride = formParams.getFirst("recipientOverride");
  }

  public UpdateXmppConfigAction(Domain domain, String username, String password, String host, String port, String serviceName, String testAddress, String recipientOverride) {

    this.domain = domain;

    this.username = username;
    this.password = password;

    this.host = host;
    this.port = port;
    this.serviceName = serviceName;

    this.testAddress = testAddress;
    this.recipientOverride = recipientOverride;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) {
    ValidationUtils.requireValue(errors, username, "The user's name must be specified.");
    ValidationUtils.requireValue(errors, password, "The password must be specified.");

    ValidationUtils.requireValue(errors, host, "The host name must be specified.");
    ValidationUtils.requireValue(errors, port, "The port must be specified.");

    return errors;
  }

  public Domain getDomain() {
    return domain;
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

  public String getServiceName() {
    return serviceName;
  }
}
