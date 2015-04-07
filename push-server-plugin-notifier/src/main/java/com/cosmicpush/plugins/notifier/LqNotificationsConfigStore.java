package com.cosmicpush.plugins.notifier;

import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.system.CpCouchServer;
import org.crazyyak.lib.couchace.DefaultCouchStore;

public class LqNotificationsConfigStore extends DefaultCouchStore<LqNotificationsConfig> {

  public static final String NOTIFIER_CONFIG_DESIGN_NAME = "notifier-config";

  public LqNotificationsConfigStore(CpCouchServer couchServer) {
    super(couchServer, LqNotificationsConfig.class);
  }

  @Override
  public String getDatabaseName() {
    return CpCouchServer.DATABASE_NAME;
  }

  @Override
  public String getDesignName() {
    return NOTIFIER_CONFIG_DESIGN_NAME;
  }

  public static String toDocumentId(Domain domain) {
    return String.format("%s:notifier-config", domain.getDomainId());
  }
}
