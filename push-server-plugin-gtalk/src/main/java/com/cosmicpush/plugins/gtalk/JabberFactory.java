/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.gtalk;

import org.apache.commons.logging.*;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.*;

public class JabberFactory {

  private static final Log log = LogFactory.getLog(JabberFactory.class);

  private final String userName;
  private final String password;

  public JabberFactory(String userName, String password) {
    this.userName = userName;
    this.password = password;
  }

  public synchronized void sendTo(final String recipient, final String message) throws XMPPException {

    log.info(String.format("%s: %s", recipient, message));

    XMPPConnection connection = new XMPPConnection(new ConnectionConfiguration("talk.google.com", 5222, "gmail.com"));

    if (connection.isConnected() == false) {
      connection.connect();
    }

    if (connection.isAuthenticated() == false) {
      SASLAuthentication.supportSASLMechanism("PLAIN", 0);
      connection.login(userName, password);
      connection.sendPacket(new Presence(Presence.Type.available));
    }

    Message jabberMessage = new Message(recipient, Message.Type.chat);
    jabberMessage.setBody(message);

    connection.sendPacket(jabberMessage);
    connection.disconnect(new Presence(Presence.Type.unavailable));
  }
}
