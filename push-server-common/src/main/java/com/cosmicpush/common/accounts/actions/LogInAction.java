/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts.actions;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.pub.internal.*;
import org.crazyyak.dev.common.StringUtils;
import org.crazyyak.dev.common.exceptions.ApiException;

public class LogInAction extends AccountAction {

  private final String password;

  public LogInAction(String password) {
    this.password = StringUtils.emptyToNull(password);
  }

  public String getPassword() {
    return password;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) throws ApiException {
    ValidationUtils.requireValue(errors, password, Account.INVALID_USER_NAME_OR_PASSWORD);
    return errors;
  }
}
