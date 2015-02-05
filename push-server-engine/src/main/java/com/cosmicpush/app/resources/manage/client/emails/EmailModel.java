/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.app.resources.manage.client.emails;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.push.*;

public class EmailModel {

  private final Account account;
  private final ApiClient apiClient;

  private final ApiRequest request;
  private final EmailPush email;

  public EmailModel(Account account, ApiClient apiClient, ApiRequest request, EmailPush email) {
    this.account = account;
    this.apiClient = apiClient;
    this.request = request;
    this.email = email;
  }

  public Account getAccount() {
    return account;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public ApiRequest getRequest() {
    return request;
  }

  public EmailPush getEmail() {
    return email;
  }

  public String getRetryUrl() {
    return String.format("/manage/api-client/%s/emails/%s/retry", apiClient.getClientName(), request.getApiRequestId());
  }
}
