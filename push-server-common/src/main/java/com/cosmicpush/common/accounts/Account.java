/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts;

import com.cosmicpush.common.accounts.actions.ChangePasswordAction;
import com.cosmicpush.common.accounts.actions.ConfirmEmailAction;
import com.cosmicpush.common.accounts.actions.CreateAccountAction;
import com.cosmicpush.common.accounts.actions.UpdateAccountAction;
import com.cosmicpush.common.actions.CreateDomainAction;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.pub.internal.CpIdGenerator;
import com.cosmicpush.pub.internal.RequestErrors;
import com.couchace.annotations.CouchEntity;
import com.couchace.annotations.CouchId;
import com.couchace.annotations.CouchRevision;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.crazyyak.dev.common.BeanUtils;
import org.crazyyak.dev.common.DateUtils;
import org.crazyyak.dev.common.EqualsUtils;
import org.crazyyak.dev.common.StringUtils;
import org.crazyyak.dev.common.exceptions.ApiException;

import java.time.LocalDateTime;

@CouchEntity(AccountStore.ACCOUNT_DESIGN_NAME)
public class Account {

  public static final String INVALID_USER_NAME_OR_PASSWORD = "Invalid user name or password";

  private String accountId;
  private String revision;

  private LocalDateTime createdAt;

  private String password;
  private String tempPassword;

  private String firstName;
  private String lastName;

  private String emailAddress;
  private boolean emailConfirmed;
  private String emailConfirmationCode;

  @JsonCreator
  private Account(
      @JacksonInject("accountId") String accountId,
      @JacksonInject("revision") String revision,

      @JsonProperty("createdAt") LocalDateTime createdAt,

      @JsonProperty("password") String password,
      @JsonProperty("tempPassword") String tempPassword,

      @JsonProperty("firstName") String firstName,
      @JsonProperty("lastName") String lastName,

      @JsonProperty("emailAddress") String emailAddress,
      @JsonProperty("emailConfirmed") boolean emailConfirmed,
      @JsonProperty("emailConfirmationCode") String emailConfirmationCode) {

    this.accountId = accountId;

    this.revision = revision;

    this.createdAt = createdAt;

    this.password = password;
    this.tempPassword = tempPassword;

    this.firstName = firstName;
    this.lastName = lastName;

    this.emailAddress = emailAddress;
    this.emailConfirmed = emailConfirmed;
    this.emailConfirmationCode = emailConfirmationCode;
  }

  public Account(CreateAccountAction action) {

    this.accountId = CpIdGenerator.newId();
    this.createdAt = DateUtils.currentLocalDateTime();

    validatePasswords(action.getPassword(), action.getPasswordConfirmed());
    this.password = action.getPassword();

    this.firstName = action.getFirstName();
    this.lastName = action.getLastName();
    this.emailAddress = action.getEmailAddress();

    this.emailConfirmed = false;
    this.emailConfirmationCode = String.valueOf(System.currentTimeMillis());
    this.emailConfirmationCode = emailConfirmationCode.substring(emailConfirmationCode.length()-5);
  }

  @CouchId
  public String getAccountId() {
    return accountId;
  }

  @CouchRevision
  public String getRevision() {
    return revision;
  }

  public Domain add(CreateDomainAction action) {
    action.validate(new RequestErrors()).assertNoErrors();
    return new Domain(action);
  }

  public void apply(UpdateAccountAction action) {
    // version check

    this.firstName = action.getFirstName();
    this.lastName = action.getLastName();

    this.emailAddress = action.getEmailAddress();
  }

  public void apply(ChangePasswordAction action) {
    validatePassword(action.getCurrent());
    validatePasswords(action.getPassword(), action.getConfirmed());
    this.password = action.getPassword();
  }

  public boolean hasTempPassword() {
    return StringUtils.isNotBlank(tempPassword);
  }
  public String createTempPassword() {
    String tempPassword = String.valueOf(System.currentTimeMillis());
    return this.tempPassword = tempPassword.substring(tempPassword.length()-8);
  }
  public void clearTempPassword() {
    this.tempPassword = null;
  }

  public String getPassword() {
    return password;
  }

  public String getTempPassword() {
    return tempPassword;
  }

  public void validatePassword(String password) {
    if (EqualsUtils.objectsNotEqual(this.password, password)) {
      throw ApiException.badRequest(INVALID_USER_NAME_OR_PASSWORD);
    }
  }
  public void validatePasswords(String password, String confirmed) {
    if (StringUtils.isBlank(password) || password.equals(confirmed) == false) {
      throw ApiException.badRequest("The two passwords do not match.");
    }
  }

  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmailAddress() {
    return emailAddress;
  }
  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }


  public String getEmailConfirmationCode() {
    return emailConfirmationCode;
  }
  public void setEmailConfirmationCode(String emailConfirmationCode) {
    this.emailConfirmationCode = emailConfirmationCode;
  }

  public boolean isEmailConfirmed() {
    return emailConfirmed;
  }
  public void setEmailConfirmed(boolean emailConfirmed) {
    this.emailConfirmed = emailConfirmed;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public boolean equals(Object object) {
    if (object instanceof Account) {
      Account that = (Account)object;
      return this.accountId.equals(that.accountId);
    }
    return false;
  }

  public void confirmEmail(ConfirmEmailAction action) {
    if (EqualsUtils.objectsNotEqual(action.getConfirmationCode(), this.emailConfirmationCode)) {
      throw ApiException.badRequest("Invalid confirmation code.");
    }
    this.emailConfirmed = true;
    this.emailConfirmationCode = null;
  }

  public String toString() {
    return emailAddress;
  }
}
