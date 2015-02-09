/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.notifier;

import com.cosmicpush.common.AbstractDelegate;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.common.system.AppContext;
import com.cosmicpush.pub.common.RequestStatus;
import com.cosmicpush.pub.push.GoogleTalkPush;
import com.cosmicpush.pub.push.LqNotificationPush;
import org.crazyyak.dev.common.StringUtils;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;

public class LqNotificationsDelegate extends AbstractDelegate {

  private final Account account;
  private final Domain domain;

  private final LqNotificationPush push;
  private final LqNotificationsConfig config;
  private AppContext appContext;

  public LqNotificationsDelegate(PluginContext pluginContext, Account account, Domain domain, PushRequest pushRequest, LqNotificationPush push, LqNotificationsConfig config) {
    super(pluginContext, pushRequest);
    this.appContext = pluginContext.getAppContext();
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

    String id = pushRequest.getPushRequestId();

    String url = String.format("%s/q/%s", pluginContext.getBaseURI(), id);
    url = appContext.getBitlyApi().shortenUnencodedUrl(url);

    String message = push.getSummary() + " >> " + url;
    String recipient = config.getUserName();
    GoogleTalkPush push = GoogleTalkPush.newPush(recipient, message, null);

    pluginContext.getPushProcessor().execute(pushRequest.getApiVersion(), account, domain, push);
    return pushRequest.processed();
  }
}
