/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts.queries;

import com.cosmicpush.pub.internal.*;

public class AccountUserNameQuery extends AccountQuery {

  private final String userName;

  protected AccountUserNameQuery(String userName) {
    this.userName = userName;
  }

  public String getUserName() {
    return userName;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) {
    ValidationUtils.requireValue(errors, userName, "The account's user name must be specified.");
    return errors;
  }
}
