/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.api.deprecated;

import com.cosmicpush.app.deprecated.NotificationPushV1;
import com.cosmicpush.common.AbstractDelegate;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.common.system.AppContext;
import com.cosmicpush.pub.common.RequestStatus;
import com.cosmicpush.pub.push.GoogleTalkPush;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;

public class NotificationDelegateV1 extends AbstractDelegate {

  private final Domain domain;

  private final PluginContext pluginContext;
  private final NotificationPushV1 push;

  private final AppContext appContext;

  public NotificationDelegateV1(PluginContext pluginContext, Domain domain, PushRequest pushRequest, NotificationPushV1 push) {
    super(pluginContext, pushRequest);
    this.push = ExceptionUtils.assertNotNull(push, "push");
    this.pluginContext = ExceptionUtils.assertNotNull(pluginContext, "context");
    this.domain = ExceptionUtils.assertNotNull(domain, "domain");
    this.appContext = pluginContext.getAppContext();
  }

  @Override
  public synchronized RequestStatus processRequest() throws Exception {

    String id = pushRequest.getPushRequestId();

    String url = String.format("%s/q/%s", pluginContext.getBaseURI(), id);
    url = appContext.getBitlyApi().shortenUnencodedUrl(url);

    String message = push.getMessage() + " >> " + url;
    GoogleTalkPush push = GoogleTalkPush.newPush("jacob.parr@gmail.com", message, null);

    pluginContext.getPushProcessor().execute(pushRequest.getApiVersion(), domain, push);
    return pushRequest.processed();
  }
}
