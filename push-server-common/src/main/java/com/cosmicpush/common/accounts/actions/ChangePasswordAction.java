/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts.actions;

import com.cosmicpush.pub.internal.*;
import org.crazyyak.dev.common.exceptions.ApiException;

public class ChangePasswordAction extends AccountAction {

  private final boolean authenticate;

  private final String current;
  private final String password;
  private final String confirmed;

  public ChangePasswordAction(boolean authenticate, String current, String password, String confirmed) {
    this.current = current;
    this.password = password;
    this.confirmed = confirmed;
    this.authenticate = authenticate;
  }

  public String getCurrent() {
    return current;
  }

  public String getPassword() {
    return password;
  }

  public String getConfirmed() {
    return confirmed;
  }

  public boolean isAuthenticate() {
    return authenticate;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) throws ApiException {
    ValidationUtils.requireValue(errors, current, "The current password must be specified.");
    ValidationUtils.requireValue(errors, password, "The new password must be specified.");
    ValidationUtils.requireValue(errors, confirmed, "The confirmed password must be specified.");
    return errors;
  }
}
