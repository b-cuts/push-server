/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client.userevents;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import java.time.LocalDate;
import java.util.*;

public class UserEventGroupsModel {

  private final Account account;
  private final ApiClient apiClient;
  private final Map<LocalDate,List<UserEventGroup>> userEventGroups = new TreeMap<>();

  public UserEventGroupsModel(Account account, ApiClient apiClient, List<UserEventGroup> userEventGroups) {
    this.account = account;
    this.apiClient = apiClient;

    for (UserEventGroup group : userEventGroups) {
      LocalDate date = group.getUpdatedAt().toLocalDate();
      if (this.userEventGroups.containsKey(date) == false) {
        this.userEventGroups.put(date, new ArrayList<UserEventGroup>());
      }
      this.userEventGroups.get(date).add(group);
    }
  }

  public Account getAccount() {
    return account;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public List<List<UserEventGroup>> getCollection() {
    List<List<UserEventGroup>> list = new ArrayList<List<UserEventGroup>>();
    list.addAll(this.userEventGroups.values());
    Collections.reverse(list);
    return list;
  }
}
