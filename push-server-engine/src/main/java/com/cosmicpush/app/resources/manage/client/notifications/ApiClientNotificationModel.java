/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client.notifications;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.push.NotificationPush;

public class ApiClientNotificationModel {

  private final Account account;
  private final ApiClient apiClient;

  private final ApiRequest request;
  private final NotificationPush notification;

  public ApiClientNotificationModel(Account account, ApiClient apiClient, ApiRequest request, NotificationPush notification) {
    this.account = account;
    this.apiClient = apiClient;
    this.request = request;
    this.notification = notification;
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

  public NotificationPush getNotification() {
    return notification;
  }
}
