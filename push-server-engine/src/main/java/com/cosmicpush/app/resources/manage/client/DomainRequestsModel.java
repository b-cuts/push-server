/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.requests.ApiRequest;
import java.util.List;

public class DomainRequestsModel {

  private final Account account;
  private final Domain domain;
  private final List<ApiRequest> requests;

  public DomainRequestsModel(Account account, Domain domain, List<ApiRequest> requests) {
    this.account = account;
    this.domain = domain;
    this.requests = requests;
  }

  public Account getAccount() {
    return account;
  }

  public Domain getDomain() {
    return domain;
  }

  public List<ApiRequest> getRequests() {
    return requests;
  }
}
