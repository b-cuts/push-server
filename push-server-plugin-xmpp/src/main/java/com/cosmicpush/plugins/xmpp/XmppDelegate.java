/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.xmpp;

import com.cosmicpush.common.AbstractDelegate;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.common.system.AppContext;
import com.cosmicpush.pub.common.RequestStatus;
import com.cosmicpush.pub.push.XmppPush;
import org.crazyyak.dev.common.StringUtils;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;
import org.jivesoftware.smack.XMPPException;

public class XmppDelegate extends AbstractDelegate {

  private final Domain domain;

  private final XmppPush push;
  private final XmppConfig config;
  private final AppContext appContext;

  public XmppDelegate(PluginContext pluginContext, Domain domain, PushRequest pushRequest, XmppPush push, XmppConfig config) {
    super(pluginContext, pushRequest);
    this.config = ExceptionUtils.assertNotNull(config, "config");
    this.push = ExceptionUtils.assertNotNull(push, "push");
    this.domain = ExceptionUtils.assertNotNull(domain, "domain");
    this.appContext = pluginContext.getAppContext();
  }

  @Override
  public synchronized RequestStatus processRequest() throws Exception {

    String apiMessage = sendMessage();

    return pushRequest.processed(apiMessage);
  }

  public String sendMessage() throws XMPPException {

    XmppFactory factory = new XmppFactory(config);

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
