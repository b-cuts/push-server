/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.common.actions;

import com.cosmicpush.pub.internal.*;

public class CreateClientAction implements ValidatableAction {

  private final String clientName;
  private final String clientPassword;

  public CreateClientAction(String clientName, String clientPassword) {
    this.clientName = clientName;
    this.clientPassword = clientPassword;
  }

  public String getClientName() {
    return clientName;
  }

  public String getClientPassword() {
    return clientPassword;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) {
    ValidationUtils.validateUserName(errors, clientName, "client's name");
    ValidationUtils.validatePassword(errors, clientPassword, "client's name");
    return errors;
  }
}
