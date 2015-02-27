package com.cosmicpush.plugins.xmpp;

import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.system.CpCouchServer;
import org.crazyyak.lib.couchace.DefaultCouchStore;

public class XmppConfigStore extends DefaultCouchStore<XmppConfig> {

  public static final String XMPP_CONFIG_DESIGN_NAME = "xmpp-config";

  public XmppConfigStore(CpCouchServer couchServer) {
    super(couchServer, couchServer.getDatabaseName(), XmppConfig.class);
  }

  @Override
  public String getDesignName() {
    return XMPP_CONFIG_DESIGN_NAME;
  }

  public static String toDocumentId(Domain domain) {
    return String.format("%s:xmpp-config", domain.getDomainId());
  }
}
