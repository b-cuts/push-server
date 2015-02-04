/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.smtp;

import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.config.SmtpAuthType;
import com.cosmicpush.pub.internal.*;
import javax.ws.rs.core.MultivaluedMap;

public class UpdateSmtpEmailConfigAction implements ValidatableAction {

  private final ApiClient apiClient;

  private final String userName;
  private final String password;

  private final SmtpAuthType authType;
  private final String serverName;
  private final String portNumber;

  private final String recipientOverride;
  private String testToAddress;
  private String testFromAddress;

  public UpdateSmtpEmailConfigAction(ApiClient apiClient, MultivaluedMap<String, String> formParams) {

    this.apiClient = apiClient;

    this.userName = formParams.getFirst("userName");
    this.password = formParams.getFirst("password");

    this.authType = (formParams.containsKey("authType") == false) ? null : SmtpAuthType.valueOf(formParams.getFirst("authType"));
    this.serverName = formParams.getFirst("serverName");
    this.portNumber = formParams.getFirst("portNumber");

    this.testToAddress = formParams.getFirst("testToAddress");
    this.testFromAddress = formParams.getFirst("testFromAddress");
    this.recipientOverride = formParams.getFirst("recipientOverride");
  }

  public UpdateSmtpEmailConfigAction(ApiClient apiClient, String userName, String password, SmtpAuthType authType, String serverName, String portNumber, String testToAddress, String testFromAddress, String recipientOverride) {

    this.apiClient = apiClient;

    this.userName = userName;
    this.password = password;

    this.authType = authType;
    this.serverName = serverName;
    this.portNumber = portNumber;

    this.testToAddress = testToAddress;
    this.testFromAddress = testFromAddress;
    this.recipientOverride = recipientOverride;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) {

    ValidationUtils.requireValue(errors, apiClient, "The api client must be specified.");

    ValidationUtils.requireValue(errors, userName, "The user's name must be specified.");
    ValidationUtils.requireValue(errors, password, "The password must be specified.");
    ValidationUtils.requireValue(errors, authType, "The authentication type must be specified.");
    ValidationUtils.requireValue(errors, serverName, "The server's name must be specified.");

    ValidationUtils.requireValue(errors, portNumber, "The server's port number must be specified.");
    ValidationUtils.requireInteger(errors, portNumber, "The server's port number is not a valid number.");

    return errors;
  }

  public ApiClient getApiClient() {
    return apiClient;
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
