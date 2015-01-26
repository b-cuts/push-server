/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cosmicpush.pub.internal;

import org.crazyyak.dev.common.StringUtils;

import java.util.Arrays;
import java.util.List;

public class ValidationUtils {

  private static final List<String> invalidSecurityChr = Arrays.asList(" ", ",", "\t", "<", ">", "#", "|", "&", "~", "?", "(", ")", "{", "}");

  public static void requireValue(RequestErrors errors, Object value, String errorMessage) {

    if (value instanceof Integer) {
      if (((Integer)value) == 0) {
        errors.add(errorMessage);
      }
    } else if (value instanceof Long) {
      if (((Long)value) == 0) {
        errors.add(errorMessage);
      }
    } else if (StringUtils.isBlank(value)) {
      errors.add(errorMessage);
    }
  }

  public static void requireNull(RequestErrors errors, Object value, String errorMessage) {
    if (value instanceof Integer) {
      if (((Integer)value) != 0) {
        errors.add(errorMessage);
      }
    } else if (value instanceof Long) {
      if (((Long)value) != 0) {
        errors.add(errorMessage);
      }
    } else if (StringUtils.isNotBlank(value)) {
      errors.add(errorMessage);
    }
  }

  public static void validatePhoneNumber(String what, List<String> errors, String phoneNumber) {
    if (phoneNumber.length() != 10) {
      errors.add("The " + what + " number must be 10 digit long.");
    } else {
      try {
        // noinspection ResultOfMethodCallIgnored
        Long.valueOf(phoneNumber);
      } catch (NumberFormatException e) {
        errors.add("The " + what + " number specified is not a valid 10 digit phone number.");
      }
    }
  }

  private ValidationUtils() {
  }

  public static void validateUserName(RequestErrors errors, String userName, String what) {
    validSecurityField(errors, userName, String.format("The %s must be specified.", what));
  }

  public static void validatePassword(RequestErrors errors, String password, String what) {
    validSecurityField(errors, password, String.format("The %s must be specified.", what));
  }

  private static void validSecurityField(RequestErrors errors, String value, String what) {
    ValidationUtils.requireValue(errors, value, String.format("The %s must be specified.", what));
    if (StringUtils.isBlank(value)) return;

    for (String chr : invalidSecurityChr) {
      if (value.contains(chr)) {
        String list = StringUtils.toDelineatedString("  ", invalidSecurityChr);
        String msg = String.format("The %s cannot contain the following characters:  %s", what, list);
        errors.add(msg);
        break;
      }
    }
  }

  public static void requireInteger(RequestErrors errors, String value, String message) {
    if (StringUtils.isNotBlank(value)) {
      try {
        // noinspection ResultOfMethodCallIgnored
        Integer.valueOf(value);

      } catch (NumberFormatException e) {
        errors.add(message);
      }
    }
  }
}
