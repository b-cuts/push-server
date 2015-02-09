/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.api.deprecated;

import com.cosmicpush.common.AbstractDelegate;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.common.system.AppContext;
import com.cosmicpush.pub.common.RequestStatus;
import com.cosmicpush.pub.push.GoogleTalkPush;
import com.cosmicpush.pub.push.NotificationPush;
import org.crazyyak.dev.common.StringUtils;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;

public class NotificationDelegate extends AbstractDelegate {

  private final Account account;
  private final Domain domain;

  private final PluginContext pluginContext;
  private final NotificationPush push;

  private final AppContext appContext;

  public NotificationDelegate(PluginContext context, Account account, Domain domain, ApiRequest apiRequest, NotificationPush push) {
    super(context.getObjectMapper(), apiRequest, context.getApiRequestStore());
    this.push = ExceptionUtils.assertNotNull(push, "push");
    this.pluginContext = ExceptionUtils.assertNotNull(context, "context");
    this.account = ExceptionUtils.assertNotNull(account, "account");
    this.domain = ExceptionUtils.assertNotNull(domain, "domain");
    this.appContext = context.getAppContext();
  }

  @Override
  public synchronized RequestStatus processRequest() throws Exception {
    String reasonNotPermitted = account.getReasonNotPermitted(push);
    if (StringUtils.isNotBlank(reasonNotPermitted)) {
      return apiRequest.denyRequest(reasonNotPermitted);
    }

    String id = apiRequest.getApiRequestId();

    String url = String.format("%s/q/%s", pluginContext.getBaseURI(), id);
    url = appContext.getBitlyApi().shortenUnencodedUrl(url);

    String message = push.getMessage() + " >> " + url;
    GoogleTalkPush push = GoogleTalkPush.newPush("jacob.parr@gmail.com", message, null);

    pluginContext.getPushProcessor().execute(apiRequest.getApiVersion(), account, domain, push);
    return apiRequest.processed();
  }
}
