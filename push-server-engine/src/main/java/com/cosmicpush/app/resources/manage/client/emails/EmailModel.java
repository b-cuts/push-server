/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.app.resources.manage.client.emails;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.push.*;

public class EmailModel {

  private final Account account;
  private final Domain domain;

  private final ApiRequest request;
  private final EmailPush email;

  public EmailModel(Account account, Domain domain, ApiRequest request, EmailPush email) {
    this.account = account;
    this.domain = domain;
    this.request = request;
    this.email = email;
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

  public EmailPush getEmail() {
    return email;
  }

  public String getRetryUrl() {
    return String.format("/manage/domain/%s/emails/%s/retry", domain.getDomainKey(), request.getApiRequestId());
  }
}
