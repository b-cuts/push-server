/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts.actions;

import com.cosmicpush.pub.internal.RequestErrors;
import com.cosmicpush.pub.internal.ValidationUtils;
import org.crazyyak.dev.common.StringUtils;
import org.crazyyak.dev.common.exceptions.ApiException;

import java.time.ZoneId;
import java.util.List;

public class CreateAccountAction extends AccountAction {

  private final ZoneId timeZone;

  private final String userName;
  private final String emailAddress;

  private final String firstName;
  private final String lastName;

  private final String password;
  private final String passwordConfirmed;

  public CreateAccountAction(
      ZoneId timeZone,
      String userName, String emailAddress,
      String firstName, String lastName,
      String password, String passwordConfirmed) {

    this.timeZone = timeZone;

    this.userName = userName;
    this.emailAddress = emailAddress;

    this.firstName = firstName;
    this.lastName = lastName;

    this.password = password;
    this.passwordConfirmed = passwordConfirmed;
  }

  public ZoneId getTimeZone() {
    return timeZone;
  }

  public String getUserName() {
    return userName;
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
    validateUserName(errors, userName);

    ValidationUtils.requireValue(errors, firstName, "Your first name must be specified.");
    ValidationUtils.requireValue(errors, lastName, "Your last name must be specified.");
    ValidationUtils.requireValue(errors, emailAddress, "Your email address must be specified.");

    return errors;
  }

  public static void validateUserName(List<String> errors, String userName) {
    if (StringUtils.isBlank(userName)) {
      errors.add("Your user name must be specified.");
    } else if (Character.isLetter(userName.charAt(0)) == false) {
      errors.add("Your user name must start with a letter.");
    } else {
      for (char chr : userName.toCharArray()) {
        if (Character.isLetter(chr) == false && Character.isDigit(chr) == false) {
          errors.add("Your user name can contain only letters and numbers.");
          break;
        }
      }
    }
  }
}
