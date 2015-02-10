/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts.actions;

import com.cosmicpush.pub.internal.RequestErrors;
import com.cosmicpush.pub.internal.ValidationUtils;
import org.crazyyak.dev.common.exceptions.ApiException;

import java.time.ZoneId;
import java.util.Arrays;

public class CreateAccountAction extends AccountAction {

  private final ZoneId timeZone;

  private final String emailAddress;

  private final String firstName;
  private final String lastName;

  private final String password;
  private final String passwordConfirmed;

  public CreateAccountAction(
      ZoneId timeZone,
      String emailAddress,
      String firstName, String lastName,
      String password, String passwordConfirmed) {

    this.timeZone = timeZone;

    this.emailAddress = emailAddress;

    this.firstName = firstName;
    this.lastName = lastName;

    this.password = password;
    this.passwordConfirmed = passwordConfirmed;
  }

  public ZoneId getTimeZone() {
    return timeZone;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getPassword() {
    return password;
  }

  public String getPasswordConfirmed() {
    return passwordConfirmed;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) throws ApiException {

    ValidationUtils.requireValue(errors, timeZone, "The time zone must be specified.");

    ValidationUtils.requireValue(errors, firstName, "Your first name must be specified.");
    ValidationUtils.requireValue(errors, lastName, "Your last name must be specified.");
    ValidationUtils.requireValue(errors, emailAddress, "Your email address must be specified.");

    for (String chr : Arrays.asList("<", ">")) {
      if (emailAddress != null && emailAddress.contains("<")) {
        String msg = String.format("Your email address must not contain the \"%s\" character.", chr);
        errors.add(msg);
      }
    }

    return errors;
  }

}
