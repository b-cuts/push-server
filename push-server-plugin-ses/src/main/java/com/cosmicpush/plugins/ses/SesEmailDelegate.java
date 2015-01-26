/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.ses;

import com.amazonaws.auth.*;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;
import com.cosmicpush.common.AbstractDelegate;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.common.RequestStatus;
import com.cosmicpush.pub.push.SesEmailPush;
import org.crazyyak.dev.common.StringUtils;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;

public class SesEmailDelegate extends AbstractDelegate {

  private final Account account;
  private final ApiClient apiClient;

  private final SesEmailPush push;
  private final SesEmailConfig config;

  public SesEmailDelegate(PluginContext context, Account account, ApiClient apiClient, ApiRequest apiRequest, SesEmailPush push, SesEmailConfig config) {
    super(context.getObjectMapper(), apiRequest, context.getApiRequestStore());
    this.push = ExceptionUtils.assertNotNull(push, "push");
    this.config = ExceptionUtils.assertNotNull(config, "config");
    this.account = ExceptionUtils.assertNotNull(account, "account");
    this.apiClient = ExceptionUtils.assertNotNull(apiClient, "apiClient");
  }

  @Override
  public synchronized RequestStatus processRequest() throws Exception {
    String reasonNotPermitted = account.getReasonNotPermitted(push);
    if (StringUtils.isNotBlank(reasonNotPermitted)) {
      return apiRequest.denyRequest(reasonNotPermitted);
    }

    String apiMessage = sendEmail();

    return apiRequest.processed(apiMessage);
  }

  /**
   * Backdoor to the SesEmailDelegate to send an email message without any validation.
   * @return The API message to log into history.
   */
  public String sendEmail() {
    String apiMessage = null;

    Body body = new Body();
    if (StringUtils.isBlank(push.getHtmlContent())) {
      body.withText(new Content().withCharset("UTF-8").withData("-no message-"));
    } else {
      body.withHtml(new Content().withCharset("UTF-8").withData(push.getHtmlContent()));
    }

    SendEmailRequest sendEmailRequest = new SendEmailRequest();
    sendEmailRequest.withSource(push.getFromAddress());
    sendEmailRequest.withReturnPath(push.getFromAddress());
    sendEmailRequest.withReplyToAddresses(push.getFromAddress());

    if (StringUtils.isNotBlank(config.getRecipientOverride())) {
      // This is NOT a "production" request.
      sendEmailRequest.setDestination(new Destination().withToAddresses(config.getRecipientOverride()));
      apiMessage = String.format("Request sent to recipient override, %s.", config.getRecipientOverride());
    } else {
      // This IS a "production" request.
      sendEmailRequest.setDestination(new Destination().withToAddresses(push.getToAddress()));
    }

    Content subject = new Content().withCharset("UTF-8").withData(push.getEmailSubject());
    sendEmailRequest.setMessage(new Message(subject, body));

    AWSCredentials awsCredentials = new BasicAWSCredentials(config.getAccessKeyId(), config.getSecretKey());
    new AmazonSimpleEmailServiceClient(awsCredentials).sendEmail(sendEmailRequest);

    return apiMessage;
  }
}
