/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts.actions;

import com.cosmicpush.pub.internal.*;
import java.net.URL;
import org.crazyyak.dev.common.exceptions.ApiException;

public class ResetPasswordAction extends AccountAction {

  private final URL templateUrl;

  public ResetPasswordAction(URL templateUrl) {
    this.templateUrl = templateUrl;
  }

  public URL getTemplateUrl() {
    return templateUrl;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) throws ApiException {
    ValidationUtils.requireValue(errors, templateUrl, "The template URL must be specified.");
    return errors;
  }
}
