/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts;

import com.cosmicpush.common.accounts.queries.*;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.pub.internal.RequestErrors;
import java.util.List;
import org.crazyyak.lib.couchace.DefaultCouchStore;

public class AccountStore extends DefaultCouchStore<Account> {

  public static final String ACCOUNT_DESIGN_NAME = "account";

  public AccountStore(CpCouchServer couchServer) {
    super(couchServer, couchServer.getDatabaseName(), Account.class);
  }

  public Account get(AccountQuery accountQuery) {
    RequestErrors errors = new RequestErrors();
    accountQuery.validate(errors);
    if (errors.isNotEmpty()) {
      throw errors.toBadRequestException();
    }

    if (accountQuery instanceof AccountIdQuery) {
      AccountIdQuery query = (AccountIdQuery)accountQuery;
      return getByAccountId(query.getAccountId());

    } else if (accountQuery instanceof AccountEntityQuery) {
      AccountEntityQuery query = (AccountEntityQuery)accountQuery;
      return query.getAccount();

    } else if (accountQuery instanceof AccountUserNameQuery) {
      AccountUserNameQuery query = (AccountUserNameQuery)accountQuery;
      return getByUsername(query.getUserName());

    } else if (accountQuery instanceof AccountEmailQuery) {
      AccountEmailQuery query = (AccountEmailQuery)accountQuery;
      return getByEmail(query.getEmailAddress());
    }

    String msg = String.format("The query %s is not supported.", accountQuery.getClass().getName());
    throw new UnsupportedOperationException(msg);
  }

  public Account getByAccountId(String accountId) {
    return super.getByDocumentId(accountId);
  }

  public List<Account> getAll() {
    return super.getEntities("byUserName");
  }

  public Account getByUsername(String userName) {
    if (userName == null) return null;
    List<Account> response = super.getEntities("byUserName", userName);
    return response.isEmpty() ? null : response.get(0);
  }

  private Account getByEmail(String emailAddress) {
    if (emailAddress == null) return null;
    List<Account> response = super.getEntities("byEmailAddress", emailAddress);
    return response.isEmpty() ? null : response.get(0);
  }

  public Account getByClientId(String clientId) {
    if (clientId == null) return null;
    List<Account> response = super.getEntities("byClientId", clientId);
    return response.isEmpty() ? null : response.get(0);
  }

  public Account getByClientName(String clientName) {
    if (clientName == null) return null;
    List<Account> response = super.getEntities("byClientName", clientName);
    return response.isEmpty() ? null : response.get(0);
  }

  @Override
  public String getDesignName() {
    return ACCOUNT_DESIGN_NAME;
  }
}
