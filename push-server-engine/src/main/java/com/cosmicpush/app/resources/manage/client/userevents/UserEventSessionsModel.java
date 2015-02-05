/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client.userevents;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import java.util.List;

public class UserEventSessionsModel {

  private final String deviceId;
  private final Account account;
  private final ApiClient apiClient;
  private final List<UserEventSession> sessions;

  public UserEventSessionsModel(Account account, ApiClient apiClient, String deviceId, List<UserEventSession> sessions) {
    this.deviceId = deviceId;
    this.account = account;
    this.apiClient = apiClient;
    this.sessions = sessions;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public Account getAccount() {
    return account;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public List<UserEventSession> getSessions() {
    return sessions;
  }
}
