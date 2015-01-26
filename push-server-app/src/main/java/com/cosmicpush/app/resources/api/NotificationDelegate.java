/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.api;

import com.cosmicpush.common.AbstractDelegate;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.common.*;
import com.cosmicpush.pub.push.*;
import org.crazyyak.dev.common.StringUtils;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;

public class NotificationDelegate extends AbstractDelegate {

  private final Account account;
  private final ApiClient apiClient;

  private final PluginContext context;
  private final NotificationPush push;

  public NotificationDelegate(PluginContext context, Account account, ApiClient apiClient, ApiRequest apiRequest, NotificationPush push) {
    super(context.getObjectMapper(), apiRequest, context.getApiRequestStore());
    this.push = ExceptionUtils.assertNotNull(push, "push");
    this.context = ExceptionUtils.assertNotNull(context, "context");
    this.account = ExceptionUtils.assertNotNull(account, "account");
    this.apiClient = ExceptionUtils.assertNotNull(apiClient, "apiClient");
  }

  @Override
  public synchronized RequestStatus processRequest() throws Exception {
    String reasonNotPermitted = account.getReasonNotPermitted(push);
    if (StringUtils.isNotBlank(reasonNotPermitted)) {
      return apiRequest.denyRequest(reasonNotPermitted);
    }

    String id = apiRequest.getApiRequestId();
    String message = push.getMessage() + " >> https://www.cosmicpush.com/q/" + id;
    GoogleTalkPush push = new GoogleTalkPush("me@jacobparr.com", message, null);

    PushResponse response = context.getPushProcessor().execute(account, apiClient, push);
    return response.getRequestStatus();
  }
}
