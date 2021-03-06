/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts.queries;

import com.cosmicpush.common.accounts.*;
import com.cosmicpush.pub.internal.ValidatableAction;

public abstract class AccountQuery<T> implements ValidatableAction {

  public static AccountIdQuery byId(String accountId) {
    return new AccountIdQuery(accountId);
  }

  public static AccountEntityQuery byAccount(Account account) {
    return new AccountEntityQuery(account);
  }

  public static AccountEmailQuery byEmail(String emailAddress) {
    return new AccountEmailQuery(emailAddress);
  }
}
