/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.gtalk;

import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.pub.internal.*;
import javax.ws.rs.core.MultivaluedMap;

public class UpdateGoogleTalkConfigAction implements ValidatableAction {

  private final Domain domain;

  private final String userName;
  private final String password;
  private final String recipientOverride;
  private String testAddress;

  public UpdateGoogleTalkConfigAction(Domain domain, MultivaluedMap<String, String> formParams) {

    this.domain = domain;

    this.userName = formParams.getFirst("userName");
    this.password = formParams.getFirst("password");

    this.testAddress = formParams.getFirst("testAddress");
    this.recipientOverride = formParams.getFirst("recipientOverride");
  }

  public UpdateGoogleTalkConfigAction(Domain domain, String userName, String password, String testAddress, String recipientOverride) {

    this.domain = domain;

    this.userName = userName;
    this.password = password;

    this.testAddress = testAddress;
    this.recipientOverride = recipientOverride;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) {
    ValidationUtils.requireValue(errors, userName, "The user's name must be specified.");
    ValidationUtils.requireValue(errors, password, "The password must be specified.");
    return errors;
  }

  public Domain getDomain() {
    return domain;
  }

  public String getUserName() {
    return userName;
  }

  public String getPassword() {
    return password;
  }

  public String getRecipientOverride() {
    return recipientOverride;
  }

  public String getTestAddress() {
    return testAddress;
  }
}
