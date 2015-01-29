/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.domain.accounts;

import com.cosmicpush.common.accounts.*;
import com.cosmicpush.common.accounts.actions.*;
import java.net.URL;
import org.crazyyak.dev.common.*;
import org.crazyyak.dev.common.exceptions.ApiException;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AccountOperationDelegate {

  @Autowired
  private AccountStore store;

  @Autowired
  @Qualifier("authenticationManager")
  private AuthenticationManager authenticationManager;

  public AccountOperationDelegate() {
  }

  public Account executeOperation(Account account, ChangePasswordAction operation) {
    account.changePassword(operation);
    store.update(account);

    // We just changed the account's password so we can use it to authenticate the user.
    if (operation.isAuthenticate() == false) {
      return account;
    } else {
      return authenticateUser(account, account.getPassword());
    }
  }

  public Account executeOperation(Account account) {
    store.delete(account);
    return null;
  }

  public Account executeOperation(Account account, LogInAction operation) {

    if (account == null) {
      throw ApiException.badRequest(Account.INVALID_USER_NAME_OR_PASSWORD);
    }

    authenticateUser(account, operation.getPassword());
    // notificationService.signedIn(account);

    if (account.hasTempPassword()) {
      account.clearTempPassword();
      store.update(account);
    }

    return account;
  }

  public Account executeOperation(Account account, CreateAccountAction operation) {
    if (account != null) {
      String msg = String.format("The user name \"%s\" already exists.", operation.getUserName());
      throw ApiException.badRequest(msg);
    }

    account = new Account(operation);
    store.create(account);

    if (operation.isAuthenticate()) {
      // We only authenticate when the account was created
      // by the user themselves... tell me about it if you don't mind...
      // notificationService.createdAccount(account);

      // We just created the account so we can authenticate with it's own password.
      return authenticateUser(account, operation.getPassword());
    }

    return account;
  }

  public Account executeOperation(Account account, ResetPasswordAction operation) {
    if (account == null) {
      throw ApiException.badRequest("The email address was not found.");
    }

    String tempPassword = account.createTempPassword();
    store.update(account);

    String message = String.format("<h1 style='margin-top:0'>Password Reset</h1><p>Your temporary password for %s is %s.</p>", account.getUserName(), tempPassword);
    sendEmail(operation.getTemplateUrl(), account.getEmailAddress(), "Password Reset", message);

    return account;
  }

  private void sendEmail(URL templateUrl, String emailAddress, String subject, String message) {
    try {
      String template = IoUtils.toString(templateUrl);
      String content = template.replace("${message-content}", message);
//      AwsUtils.sendEmail(subject, content, "Munchie Monster <support@munchiemonster.com>", new MunMonEmailAddress(emailAddress));

    } catch (Exception e) {
      String msg = String.format("Exception sending email to %s", emailAddress);
      throw ApiException.internalServerError(msg, e);
    }
  }

  public Account executeOperation(Account account, ConfirmEmailAction operation) {
    account.confirmEmail(operation);
    store.update(account);
    return account;
  }

  public Account executeOperation(Account account, UpdateAccountAction operation) {
    // version check
    account.apply(operation);
    store.update(account);
    return account;
  }

  public Account executeOperation(Account account, UpdatePermissionsAction operation) {
    account.apply(operation);
    store.update(account);
    return account;
  }

  public Account executeOperation(Account account, UpdateAccountStatusAction operation) {
    account.apply(operation);
    store.update(account);
    return account;
  }

  private Account authenticateUser(Account account, String password) {

    // We can only ever authenticate the current user. This may not be necessary in cases like
    // a password change, but it's easier to always update the request context when authenticating.
    // RequestContext.get().setAccount(account);
    String userName = account.getUserName();

    if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
      // RequestContext.get().setAccount(null); // clear the context
      throw ApiException.badRequest(Account.INVALID_USER_NAME_OR_PASSWORD);
    }

    try {
      UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, password);
      Authentication authenticatedUser = authenticationManager.authenticate(token);
      SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
      return account;

    } catch (AuthenticationException e) {
      // RequestContext.get().setAccount(null); // clear the context
      throw ApiException.badRequest(Account.INVALID_USER_NAME_OR_PASSWORD);
    }
  }
}
