/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.ses;

import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.pub.internal.*;
import javax.ws.rs.core.MultivaluedMap;

public class UpdateSesEmailConfigAction implements ValidatableAction {

  private final ApiClient apiClient;

  private String accessKeyId;
  private String secretKey;
  private String recipientOverride;
  private String testToAddress;
  private String testFromAddress;

  public UpdateSesEmailConfigAction(ApiClient apiClient, MultivaluedMap<String, String> formParams) {

    this.apiClient = apiClient;

    this.accessKeyId = formParams.getFirst("accessKeyId");
    this.secretKey = formParams.getFirst("secretKey");

    this.testToAddress = formParams.getFirst("testToAddress");
    this.testFromAddress = formParams.getFirst("testFromAddress");
    this.recipientOverride = formParams.getFirst("recipientOverride");
  }

  public UpdateSesEmailConfigAction(ApiClient apiClient, String accessKeyId, String secretKey, String testToAddress, String testFromAddress, String recipientOverride) {

    this.apiClient = apiClient;

    this.accessKeyId = accessKeyId;
    this.secretKey = secretKey;

    this.testToAddress = testToAddress;
    this.testFromAddress = testFromAddress;
    this.recipientOverride = recipientOverride;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) {

    ValidationUtils.requireValue(errors, accessKeyId, "The AWS Access Key ID must be specified.");
    ValidationUtils.requireValue(errors, secretKey, "The AWS Secret Key must be specified.");

    return errors;
  }

  public ApiClient getApiClient() {
    return apiClient;
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
}
