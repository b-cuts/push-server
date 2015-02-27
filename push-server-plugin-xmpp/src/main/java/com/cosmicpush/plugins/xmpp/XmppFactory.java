/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.xmpp;

import org.apache.commons.logging.*;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.*;

public class XmppFactory {

  private static final Log log = LogFactory.getLog(XmppFactory.class);

  private final XmppConfig config;

  public XmppFactory(XmppConfig config) {
    this.config = config;
  }

  public synchronized void sendTo(final String recipient, final String message) throws XMPPException {

    log.info(String.format("%s: %s", recipient, message));

    // XMPPConnection connection = new XMPPConnection(new ConnectionConfiguration(config.getHost(),"talk.google.com", 5222, "gmail.com"));
    XMPPConnection connection = new XMPPConnection(new ConnectionConfiguration(config.getHost(), config.getPortInt(), config.getServiceName()));

    if (connection.isConnected() == false) {
      connection.connect();
    }

    if (connection.isAuthenticated() == false) {
      SASLAuthentication.supportSASLMechanism("PLAIN", 0);
      connection.login(config.getUsername(), config.getPassword());
      connection.sendPacket(new Presence(Presence.Type.available));
    }

    Message jabberMessage = new Message(recipient, Message.Type.chat);
    jabberMessage.setBody(message);

    connection.sendPacket(jabberMessage);
    connection.disconnect(new Presence(Presence.Type.unavailable));
  }
}
