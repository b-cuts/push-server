/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.ApiRequest;
import java.util.List;

public class ApiClientRequestsModel {

  private final Account account;
  private final ApiClient apiClient;
  private final List<ApiRequest> requests;

  public ApiClientRequestsModel(Account account, ApiClient apiClient, List<ApiRequest> requests) {
    this.account = account;
    this.apiClient = apiClient;
    this.requests = requests;
  }

  public Account getAccount() {
    return account;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public List<ApiRequest> getRequests() {
    return requests;
  }
}
