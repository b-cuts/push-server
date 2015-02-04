package com.cosmicpush.pub.push;

import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.internal.RequestErrors;
import com.cosmicpush.pub.internal.ValidationUtils;

import java.io.Serializable;

public class OcsPush implements Push, Serializable {

  private final String recipient;
  private final String message;
  private final String callbackUrl;

  public OcsPush(String recipient, String message, String callbackUrl) {
    this.recipient = recipient;
    this.message = message;
    this.callbackUrl = callbackUrl;
  }

  @Override
  public String getCallbackUrl() {
    return callbackUrl;
  }

  @Override
  public PushType getPushType() {
    return PushType.ocs;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) {
    ValidationUtils.requireValue(errors, recipient, "The recipient must be specified.");
    ValidationUtils.requireValue(errors, message, "The chat message must be specified.");
    return errors;
  }

}
