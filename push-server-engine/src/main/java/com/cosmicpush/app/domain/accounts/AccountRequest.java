/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.domain.accounts;

import com.cosmicpush.common.accounts.actions.AccountAction;
import com.cosmicpush.common.accounts.queries.AccountQuery;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;

public class AccountRequest {

  private final AccountQuery query;
  private final AccountAction operation;

  public AccountRequest(AccountQuery query, AccountAction operation) {
    this.query = ExceptionUtils.assertNotNull(query, "query");
    this.operation = ExceptionUtils.assertNotNull(operation, "operation");
  }

  public AccountQuery getQuery() {
    return query;
  }

  public AccountAction getOperation() {
    return operation;
  }
}
