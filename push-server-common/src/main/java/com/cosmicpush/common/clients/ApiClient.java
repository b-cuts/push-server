/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.clients;

import com.cosmicpush.common.actions.*;
import com.cosmicpush.pub.internal.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"smtpEmailConfig", "awsEmailConfig", "googleTalkConfig"})
public class ApiClient {

  private String apiClientId;

  private String clientName;
  private String clientPassword;

  private String lastMessage;

  public ApiClient() {
  }

  public ApiClient create(CreateClientAction action) {
    action.validate(new RequestErrors()).assertNoErrors();

    this.apiClientId = CpIdGenerator.newId();
    this.clientName = action.getClientName();
    this.clientPassword = action.getClientPassword();

    return this;
  }

  public String getApiClientId() {
    return apiClientId;
  }

  public String getClientName() {
    return clientName;
  }

  public String getClientPassword() {
    return clientPassword;
  }

  public String getLastMessage() {
    return lastMessage;
  }

  public void setLastMessage(String lastMessage) {
    this.lastMessage = lastMessage;
  }

  public void apply(UpdateClientAction action) {
    action.validate(new RequestErrors()).assertNoErrors();

    this.clientName = action.getClientName();
    this.clientPassword = action.getClientPassword();

    this.lastMessage = "API Client configuration changed.";
  }

  public boolean equals(Object object) {
    if (object instanceof ApiClient) {
      ApiClient that = (ApiClient)object;
      return this.apiClientId.equals(that.apiClientId);
    }
    return false;
  }

  public String toString() {
    return clientName;
  }
}
