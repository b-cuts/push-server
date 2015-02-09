package com.cosmicpush.app.resources.manage.client.emails;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.requests.ApiRequest;

import java.util.*;

public class EmailsModel {
  private final Account account;
  private final Domain domain;
  private final List<ApiRequest> requests = new ArrayList<>();

  public EmailsModel(Account account, Domain domain, Collection<ApiRequest> requests) {

    this.account = account;
    this.domain = domain;

    Set<ApiRequest> sortedSet = new TreeSet<>(requests);
    List<ApiRequest> sortedList = new ArrayList<>(sortedSet);
    Collections.reverse(sortedList);
    this.requests.addAll(sortedList);
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
