/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.ses;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;
import com.cosmicpush.common.AbstractDelegate;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.common.system.AppContext;
import com.cosmicpush.pub.common.RequestStatus;
import com.cosmicpush.pub.push.SesEmailPush;
import org.crazyyak.dev.common.StringUtils;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;

public class SesEmailDelegate extends AbstractDelegate {

  private final Domain domain;

  private final SesEmailPush push;
  private final SesEmailConfig config;
  private final AppContext appContext;

  public SesEmailDelegate(PluginContext pluginContext, Domain domain, PushRequest pushRequest, SesEmailPush push, SesEmailConfig config) {
    super(pluginContext, pushRequest);
    this.push = ExceptionUtils.assertNotNull(push, "push");
    this.config = ExceptionUtils.assertNotNull(config, "config");
    this.domain = ExceptionUtils.assertNotNull(domain, "domain");
    this.appContext = pluginContext.getAppContext();
  }

  @Override
  public synchronized RequestStatus processRequest() throws Exception {

    String apiMessage = sendEmail();

    return pushRequest.processed(apiMessage);
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

    String subject = push.getEmailSubject();
    subject = appContext.getBitlyApi().parseAndShorten(subject);
    Content subjectContent = new Content().withCharset("UTF-8").withData(subject);

    sendEmailRequest.setMessage(new Message(subjectContent, body));

    AWSCredentials awsCredentials = new BasicAWSCredentials(config.getAccessKeyId(), config.getSecretKey());
    new AmazonSimpleEmailServiceClient(awsCredentials).sendEmail(sendEmailRequest);

    return apiMessage;
  }
}
