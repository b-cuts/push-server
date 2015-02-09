package com.cosmicpush.plugins.notifier;

import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.system.CpCouchServer;
import org.crazyyak.lib.couchace.DefaultCouchStore;

public class NotifierConfigStore extends DefaultCouchStore<NotifierConfig> {

  public static final String NOTIFIER_CONFIG_DESIGN_NAME = "notifier-config";

  public NotifierConfigStore(CpCouchServer couchServer) {
    super(couchServer, couchServer.getDatabaseName(), NotifierConfig.class);
  }

  @Override
  public String getDesignName() {
    return NOTIFIER_CONFIG_DESIGN_NAME;
  }

  public static String toDocumentId(Domain domain) {
    return String.format("%s:notifier-config", domain.getDomainId());
  }
}
