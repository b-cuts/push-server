/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.common.actions;

import com.cosmicpush.pub.internal.*;

public class UpdateDomainAction implements ValidatableAction {

  private final String domainKey;
  private final String domainPassword;

  public UpdateDomainAction(String domainKey, String domainPassword) {
    this.domainKey = domainKey;
    this.domainPassword = domainPassword;
  }

  public String getDomainKey() {
    return domainKey;
  }

  public String getDomainPassword() {
    return domainPassword;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) {
    ValidationUtils.validateUserName(errors, domainKey, "domain's key");
    ValidationUtils.validatePassword(errors, domainPassword, "domain's password");
    return errors;
  }
}
