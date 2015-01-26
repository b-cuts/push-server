/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts.queries;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.pub.internal.*;

public class AccountEntityQuery extends AccountQuery {

  private final Account account;

  protected AccountEntityQuery(Account account) {
    this.account = account;
  }

  public Account getAccount() {
    return account;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) {
    ValidationUtils.requireValue(errors, account, "The account must be specified.");
    return errors;
  }
}
