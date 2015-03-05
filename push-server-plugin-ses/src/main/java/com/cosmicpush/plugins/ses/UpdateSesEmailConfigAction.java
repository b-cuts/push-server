/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.ses;

import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.pub.internal.*;
import javax.ws.rs.core.MultivaluedMap;

public class UpdateSesEmailConfigAction implements ValidatableAction {

  private final Domain domain;

  private String accessKeyId;
  private String secretKey;
  private String endpoint;
  private String recipientOverride;
  private String testToAddress;
  private String testFromAddress;

  public UpdateSesEmailConfigAction(Domain domain, MultivaluedMap<String, String> formParams) {

    this.domain = domain;

    this.accessKeyId = formParams.getFirst("accessKeyId");
    this.secretKey = formParams.getFirst("secretKey");
    this.endpoint = formParams.getFirst("endpoint");

    this.testToAddress = formParams.getFirst("testToAddress");
    this.testFromAddress = formParams.getFirst("testFromAddress");
    this.recipientOverride = formParams.getFirst("recipientOverride");
  }

  public UpdateSesEmailConfigAction(Domain domain, String accessKeyId, String secretKey, String endpoint, String testToAddress, String testFromAddress, String recipientOverride) {

    this.domain = domain;

    this.accessKeyId = accessKeyId;
    this.secretKey = secretKey;
    this.endpoint = endpoint;

    this.testToAddress = testToAddress;
    this.testFromAddress = testFromAddress;
    this.recipientOverride = recipientOverride;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) {

    ValidationUtils.requireValue(errors, accessKeyId, "The AWS-SES Access Key ID must be specified.");
    ValidationUtils.requireValue(errors, secretKey, "The AWS-SES Secret Key must be specified.");
    ValidationUtils.requireValue(errors, endpoint, "The AWS-SES Endpoint must be specified.");

    return errors;
  }

  public Domain getDomain() {
    return domain;
  }

  public String getAccessKeyId() {
    return accessKeyId;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public String getRecipientOverride() {
    return recipientOverride;
  }

  public String getTestToAddress() {
    return testToAddress;
  }

  public String getTestFromAddress() {
    return testFromAddress;
  }

  public String getEndpoint() {
    return endpoint;
  }
}
