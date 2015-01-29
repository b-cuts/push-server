/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts;

import com.cosmicpush.common.accounts.actions.*;
import com.cosmicpush.common.actions.CreateClientAction;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.internal.*;
import com.couchace.annotations.*;
import com.fasterxml.jackson.annotation.*;
import java.time.LocalDateTime;
import java.util.*;
import org.crazyyak.dev.common.*;
import org.crazyyak.dev.common.exceptions.ApiException;

@JsonIgnoreProperties({"testConfig"})
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

  private List<ApiClient> apiClients = new ArrayList<>();

  private int retentionDays;

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

      @JsonProperty("apiClients") List<ApiClient> apiClients) {

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

    this.apiClients = apiClients;
  }

  public Account(CreateAccountAction action) {

    this.accountId = CpIdGenerator.newId();
    this.createdAt = DateUtils.currentDateTime();

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

  public List<ApiClient> getApiClients() {
    return apiClients;
  }
  public ApiClient add(CreateClientAction action) {
    action.validate(new RequestErrors()).assertNoErrors();
    ApiClient apiClient = new ApiClient().create(action);
    apiClients.add(apiClient);
    return apiClient;
  }
  public void remove(ApiClient apiClient) {
    if (apiClient == null) {
      throw ApiException.badRequest("The API Client must be specified.");
    }
    apiClients.remove(apiClient);
  }

  public ApiClient getApiClientById(String apiClientId) {
    for (ApiClient apiClient : apiClients) {
      if (BeanUtils.objectsEqual(apiClient.getApiClientId(), apiClientId)) {
        return apiClient;
      }
    }
    return null;
  }
  public ApiClient getApiClientByName(String clientName) {
    for (ApiClient apiClient : apiClients) {
      if (BeanUtils.objectsEqual(apiClient.getClientName(), clientName)) {
        return apiClient;
      }
    }
    return null;
  }

  public void apply(UpdateAccountStatusAction action) {
    accountStatus.apply(action);
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
    if (BeanUtils.objectsNotEqual(action.getConfirmationCode(), this.emailConfirmationCode)) {
      throw ApiException.badRequest("Invalid confirmation code.");
    }
    this.emailConfirmed = true;
    this.emailConfirmationCode = null;
  }

  public String getReasonNotPermitted(Push action) {
    return null; // We will support this later.
  }

  public String toString() {
    return emailAddress;
  }
}
