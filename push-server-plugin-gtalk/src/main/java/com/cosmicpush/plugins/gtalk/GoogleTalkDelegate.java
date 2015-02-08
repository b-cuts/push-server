/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.gtalk;

import com.cosmicpush.common.AbstractDelegate;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.common.system.AppContext;
import com.cosmicpush.pub.common.RequestStatus;
import com.cosmicpush.pub.push.GoogleTalkPush;
import org.crazyyak.dev.common.StringUtils;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;
import org.jivesoftware.smack.XMPPException;

public class GoogleTalkDelegate extends AbstractDelegate {

  private final Account account;
  private final ApiClient apiClient;

  private final GoogleTalkPush push;
  private final GoogleTalkConfig config;
  private final AppContext appContext;

  public GoogleTalkDelegate(PluginContext context, Account account, ApiClient apiClient, ApiRequest apiRequest, GoogleTalkPush push, GoogleTalkConfig config) {
    super(context.getObjectMapper(), apiRequest, context.getApiRequestStore());
    this.config = ExceptionUtils.assertNotNull(config, "config");
    this.push = ExceptionUtils.assertNotNull(push, "push");
    this.account = ExceptionUtils.assertNotNull(account, "account");
    this.apiClient = ExceptionUtils.assertNotNull(apiClient, "apiClient");
    this.appContext = context.getAppContext();
  }

  @Override
  public synchronized RequestStatus processRequest() throws Exception {
    String reasonNotPermitted = account.getReasonNotPermitted(push);
    if (StringUtils.isNotBlank(reasonNotPermitted)) {
      return apiRequest.denyRequest(reasonNotPermitted);
    }

    String apiMessage = sendMessage();

    return apiRequest.processed(apiMessage);
  }

  public String sendMessage() throws XMPPException {

    JabberFactory factory = new JabberFactory(config.getUserName(), config.getPassword());

    String message = push.getMessage();
    message = appContext.getBitlyApi().parseAndShorten(message);

    if (StringUtils.isNotBlank(config.getRecipientOverride())) {
      // This is NOT a "production" request.
      factory.sendTo(config.getRecipientOverride(), message);
      return String.format("Request sent to recipient override, %s.", config.getRecipientOverride());

    } else {
      // This IS a "production" request.
      factory.sendTo(push.getRecipient(), message);
      return null;
    }
  }
}
