/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client.userevents;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import java.util.List;

public class UserEventSessionsModel {

  private final String deviceId;
  private final Account account;
  private final Domain domain;
  private final List<UserEventSession> sessions;

  public UserEventSessionsModel(Account account, Domain domain, String deviceId, List<UserEventSession> sessions) {
    this.deviceId = deviceId;
    this.account = account;
    this.domain = domain;
    this.sessions = sessions;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public Account getAccount() {
    return account;
  }

  public Domain getDomain() {
    return domain;
  }

  public List<UserEventSession> getSessions() {
    return sessions;
  }
}
