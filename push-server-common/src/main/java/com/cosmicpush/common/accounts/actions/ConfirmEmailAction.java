/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts.actions;

import com.cosmicpush.pub.internal.*;
import org.crazyyak.dev.common.exceptions.ApiException;

public class ConfirmEmailAction extends AccountAction {

  private final String confirmationCode;

  public ConfirmEmailAction(String confirmationCode) {
    this.confirmationCode = confirmationCode;
  }

  public String getConfirmationCode() {
    return confirmationCode;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) throws ApiException {
    ValidationUtils.requireValue(errors, confirmationCode, "The confirmation code must be specified.");
    return errors;
  }
}
