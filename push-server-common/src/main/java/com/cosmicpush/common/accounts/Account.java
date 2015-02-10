/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts;

import com.cosmicpush.common.accounts.actions.*;
import com.cosmicpush.common.actions.CreateDomainAction;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.pub.common.Push;
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
import org.crazyyak.dev.domain.account.AccountStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CouchEntity(AccountStore.ACCOUNT_DESIGN_NAME)
public class Account {

  public static final String INVALID_USER_NAME_OR_PASSWORD = "Invalid user name or password";

  private String accountId;
  private String revision;

  private LocalDateTime createdAt;

  private String userName;
  private String password;
  private String tempPassword;

  private String firstName;
  private String lastName;

  private String emailAddress;
  private boolean emailConfirmed;
  private String emailConfirmationCode;

  /*package*/ Permissions permissions;
  /*package*/ AccountStatus accountStatus;

  private int retentionDays;

  private List<String> domainIds = new ArrayList<>();

  @JsonCreator
  private Account(
      @JacksonInject("accountId") String accountId,
      @JacksonInject("revision") String revision,

      @JsonProperty("createdAt") LocalDateTime createdAt,

      @JsonProperty("userName") String userName,
      @JsonProperty("password") String password,
      @JsonProperty("tempPassword") String tempPassword,

      @JsonProperty("firstName") String firstName,
      @JsonProperty("lastName") String lastName,

      @JsonProperty("emailAddress") String emailAddress,
      @JsonProperty("emailConfirmed") boolean emailConfirmed,
      @JsonProperty("emailConfirmationCode") String emailConfirmationCode,

      @JsonProperty("roleTypes") Set<String> roleTypes,
      @JsonProperty("accountStatus") AccountStatus accountStatus,

      @JsonProperty("domainIds") List<String> domainIds) {

    this.accountId = accountId;

    this.revision = revision;

    this.createdAt = createdAt;

    this.userName = userName;
    this.password = password;
    this.tempPassword = tempPassword;

    this.firstName = firstName;
    this.lastName = lastName;

    this.emailAddress = emailAddress;
    this.emailConfirmed = emailConfirmed;
    this.emailConfirmationCode = emailConfirmationCode;

    this.permissions = new Permissions(roleTypes);
    this.accountStatus = accountStatus;

    if (domainIds != null) {
      this.domainIds.addAll(domainIds);
    }
  }

  public Account(CreateAccountAction action) {

    this.accountId = CpIdGenerator.newId();
    this.createdAt = DateUtils.currentLocalDateTime();

    this.permissions = new Permissions();

    this.userName = action.getUserName();

    validatePasswords(action.getPassword(), action.getPasswordConfirmed());
    this.password = action.getPassword();

    this.firstName = action.getFirstName();
    this.lastName = action.getLastName();
    this.emailAddress = action.getEmailAddress();

    this.emailConfirmed = false;
    this.emailConfirmationCode = String.valueOf(System.currentTimeMillis());
    this.emailConfirmationCode = emailConfirmationCode.substring(emailConfirmationCode.length()-5);

    this.accountStatus = new AccountStatus(true, true, true, true);
  }

  @CouchId
  public String getAccountId() {
    return accountId;
  }

  @CouchRevision
  public String getRevision() {
    return revision;
  }

  public List<String> getDomainIds() {
    return domainIds;
  }

  //  public List<Domain> getDomains() {
//    return domains;
//  }
  public Domain add(CreateDomainAction action) {
    action.validate(new RequestErrors()).assertNoErrors();
    Domain domain = new Domain(action);
    domainIds.add(domain.getDomainId());
    return domain;
  }

  public void remove(Domain domain) {
    if (domain == null) {
      throw ApiException.badRequest("The domain must be specified.");
    }
    domainIds.remove(domain.getDomainId());
  }

//  public Domain getDomainById(String domainId) {
//    for (Domain domain : domains) {
//      if (BeanUtils.objectsEqual(domain.getDomainId(), domainId)) {
//        return domain;
//      }
//    }
//    return null;
//  }
//  public Domain getDomainByName(String domainKey) {
//    for (Domain domain : domains) {
//      if (BeanUtils.objectsEqual(domain.getDomainKey(), domainKey)) {
//        return domain;
//      }
//    }
//    return null;
//  }

  public void apply(UpdateAccountStatusAction action) {
    accountStatus.setAccountNonExpired(action.isAccountNonExpired());
    accountStatus.setAccountNonLocked(action.isAccountNonLocked());
    accountStatus.setCredentialsNonExpired(action.isCredentialsNonExpired());
    accountStatus.setEnabled(action.isEnabled());
  }

  public void apply(UpdatePermissionsAction action) {
    this.permissions.setRoleTypes(action.getRoleTypes());
  }

  public void apply(UpdateAccountAction action) {
    // version check

    this.firstName = action.getFirstName();
    this.lastName = action.getLastName();

    this.emailAddress = action.getEmailAddress();
  }

  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Set<String> getRoleTypes() {
    return permissions.getRoleTypes();
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

  public Permissions getPermissions() {
    return permissions;
  }

  public AccountStatus getAccountStatus() {
    return accountStatus;
  }

  public void validatePassword(String password) {
    if (BeanUtils.objectsNotEqual(this.password, password)) {
      throw ApiException.badRequest(INVALID_USER_NAME_OR_PASSWORD);
    }
  }
  public void validatePasswords(String password, String confirmed) {
    if (StringUtils.isBlank(password) || password.equals(confirmed) == false) {
      throw ApiException.badRequest("The two passwords do not match.");
    }
  }

  public void changePassword(ChangePasswordAction action) {
    validatePassword(action.getCurrent());
    validatePasswords(action.getPassword(), action.getConfirmed());
    this.password = action.getPassword();
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

  public int getRetentionDays() {
    return retentionDays;
  }
  public void setRetentionDays(int retentionDays) {
    this.retentionDays = retentionDays;
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

  public boolean isOwner(Domain domain) {
    return domainIds.contains(domain.getDomainId());
  }

  public boolean isNotOwner(Domain domain) {
    return !isOwner(domain);
  }
}
