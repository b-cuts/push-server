/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.notifier;

import com.cosmicpush.common.AbstractDelegate;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.common.system.AppContext;
import com.cosmicpush.pub.common.RequestStatus;
import com.cosmicpush.pub.push.XmppPush;
import com.cosmicpush.pub.push.LqNotificationPush;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;

public class LqNotificationsDelegate extends AbstractDelegate {

  private final Domain domain;

  private final LqNotificationPush push;
  private final LqNotificationsConfig config;
  private AppContext appContext;

  public LqNotificationsDelegate(PluginContext pluginContext, Domain domain, PushRequest pushRequest, LqNotificationPush push, LqNotificationsConfig config) {
    super(pluginContext, pushRequest);
    this.appContext = pluginContext.getAppContext();
    this.config = ExceptionUtils.assertNotNull(config, "config");
    this.push = ExceptionUtils.assertNotNull(push, "push");
    this.domain = ExceptionUtils.assertNotNull(domain, "domain");
  }

  @Override
  public synchronized RequestStatus processRequest() throws Exception {

    String id = pushRequest.getPushRequestId();
    String url = String.format("%s/q/%s", pluginContext.getBaseURI(), id);

    String message = push.getSummary() + " " + url;
    message = appContext.getBitlyApi().parseAndShorten(message);

    String recipient = config.getUserName();
    XmppPush push = XmppPush.newPush(recipient, message, null);

    pluginContext.getPushProcessor().execute(pushRequest.getApiVersion(), domain, push);
    return pushRequest.processed();
  }
}
