/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client.notifications;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.push.NotificationPush;

public class DomainNotificationModel {

  private final Account account;
  private final Domain domain;

  private final ApiRequest request;
  private final NotificationPush notification;

  public DomainNotificationModel(Account account, Domain domain, ApiRequest request, NotificationPush notification) {
    this.account = account;
    this.domain = domain;
    this.request = request;
    this.notification = notification;
  }

  public Account getAccount() {
    return account;
  }

  public Domain getDomain() {
    return domain;
  }

  public ApiRequest getRequest() {
    return request;
  }

  public NotificationPush getNotification() {
    return notification;
  }
}
