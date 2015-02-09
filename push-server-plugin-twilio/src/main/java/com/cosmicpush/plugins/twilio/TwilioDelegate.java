/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.twilio;

import com.cosmicpush.common.AbstractDelegate;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.pub.common.RequestStatus;
import com.cosmicpush.pub.push.TwilioSmsPush;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.factory.MessageFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.crazyyak.dev.common.StringUtils;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

public class TwilioDelegate extends AbstractDelegate {

  private final Account account;
  private final Domain domain;

  private final TwilioSmsPush push;
  private final TwilioConfig config;

  public TwilioDelegate(PluginContext pluginContext, Account account, Domain domain, PushRequest pushRequest, TwilioSmsPush push, TwilioConfig config) {
    super(pluginContext, pushRequest);
    this.config = ExceptionUtils.assertNotNull(config, "config");
    this.push = ExceptionUtils.assertNotNull(push, "push");
    this.account = ExceptionUtils.assertNotNull(account, "account");
    this.domain = ExceptionUtils.assertNotNull(domain, "domain");
  }

  @Override
  public synchronized RequestStatus processRequest() throws Exception {
    String reasonNotPermitted = account.getReasonNotPermitted(push);
    if (StringUtils.isNotBlank(reasonNotPermitted)) {
      return pushRequest.denyRequest(reasonNotPermitted);
    }

    TwilioRestClient client = new TwilioRestClient(config.getAccountSid(), config.getAuthToken());

    // Build a filter for the MessageList
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("Body", push.getMessage()));
    params.add(new BasicNameValuePair("From", push.getFrom()));
    params.add(new BasicNameValuePair("To", push.getRecipient()));
    MessageFactory messageFactory = client.getAccount().getMessageFactory();
    messageFactory.create(params);

    return pushRequest.processed();
  }
}
