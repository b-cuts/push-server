/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.domain.accounts;

import com.cosmicpush.common.accounts.*;
import com.cosmicpush.common.accounts.actions.*;
import com.cosmicpush.common.accounts.queries.AccountQuery;
import com.cosmicpush.pub.internal.RequestErrors;
import org.crazyyak.dev.common.StringUtils;
import org.crazyyak.dev.common.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceCouch implements AccountService {

  @Autowired
  private AccountStore accountStore;
  @Autowired
  private AccountOperationDelegate accountDelegate;

  public AccountServiceCouch() {
  }

  public AccountServiceCouch(AccountStore accountStore, AccountOperationDelegate accountDelegate) {
    this.accountStore = accountStore;
    this.accountDelegate = accountDelegate;
  }

  @Override
  public Account execute(AccountRequest purchaseRequest) {
    ExceptionUtils.assertNotNull(purchaseRequest, "request");
    AccountAction accountOperation = purchaseRequest.getOperation();

    RequestErrors errors = new RequestErrors();
    accountOperation.validate(errors);
    if (errors.isEmpty() == false) {
      String msg = StringUtils.toDelineatedString("\n", errors);
      throw ApiException.badRequest(msg);
    }

    Account account = accountStore.get(purchaseRequest.getQuery());

    if (accountOperation instanceof ChangePasswordAction) {
      ChangePasswordAction operation = (ChangePasswordAction)accountOperation;
      return accountDelegate.executeOperation(account, operation);

    } else if (accountOperation instanceof ConfirmEmailAction) {
      ConfirmEmailAction operation = (ConfirmEmailAction)accountOperation;
      return accountDelegate.executeOperation(account, operation);

    } else if (accountOperation instanceof CreateAccountAction) {
      CreateAccountAction operation = (CreateAccountAction)accountOperation;
      return accountDelegate.executeOperation(account, operation);

    } else if (accountOperation instanceof DeleteAccountAction) {
      DeleteAccountAction operation = (DeleteAccountAction)accountOperation;
      return accountDelegate.executeOperation(account);

    } else if (accountOperation instanceof LogInAction) {
      LogInAction operation = (LogInAction)accountOperation;
      return accountDelegate.executeOperation(account, operation);

    } else if (accountOperation instanceof ResetPasswordAction) {
      ResetPasswordAction operation = (ResetPasswordAction)accountOperation;
      return accountDelegate.executeOperation(account, operation);

    } else if (accountOperation instanceof UpdateAccountAction) {
      UpdateAccountAction operation = (UpdateAccountAction)accountOperation;
      return accountDelegate.executeOperation(account, operation);

    } else if (accountOperation instanceof UpdateAccountStatusAction) {
      UpdateAccountStatusAction operation = (UpdateAccountStatusAction)accountOperation;
      return accountDelegate.executeOperation(account, operation);

    } else if (accountOperation instanceof UpdatePermissionsAction) {
      UpdatePermissionsAction operation = (UpdatePermissionsAction)accountOperation;
      return accountDelegate.executeOperation(account, operation);
    }

    String msg = String.format("The operation %s is not supported.", accountOperation.getClass().getName());
    throw new UnsupportedOperationException(msg);
  }

  @Override
  public Account execute(AccountQuery query) {
    return accountStore.get(query);
  }
}
