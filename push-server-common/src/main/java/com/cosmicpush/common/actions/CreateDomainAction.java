/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.common.actions;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.pub.internal.RequestErrors;
import com.cosmicpush.pub.internal.ValidatableAction;
import com.cosmicpush.pub.internal.ValidationUtils;

public class CreateDomainAction implements ValidatableAction {

  private final String accountId;
  private final String domainKey;
  private final String domainPassword;

  public CreateDomainAction(Account account, String domainKey, String domainPassword) {
    this.accountId = account.getAccountId();
    this.domainKey = domainKey;
    this.domainPassword = domainPassword;
  }

  public String getAccountId() {
    return accountId;
  }

  public String getDomainKey() {
    return domainKey;
  }

  public String getDomainPassword() {
    return domainPassword;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) {
    ValidationUtils.requireValue(errors, accountId, "The accountId must be specified.");
    ValidationUtils.validateUserName(errors, domainKey, "domain's key");
    ValidationUtils.validatePassword(errors, domainPassword, "domain's password");
    return errors;
  }
}
