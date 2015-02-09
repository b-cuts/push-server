package com.cosmicpush.plugins.ocs;

import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.system.CpCouchServer;
import org.crazyyak.lib.couchace.DefaultCouchStore;

public class OcsMessageConfigStore extends DefaultCouchStore<OcsMessageConfig> {

  public static final String OCS_CONFIG_DESIGN_NAME = "ocs-config";

  public OcsMessageConfigStore(CpCouchServer couchServer) {
    super(couchServer, couchServer.getDatabaseName(), OcsMessageConfig.class);
  }

  @Override
  public String getDesignName() {
    return OCS_CONFIG_DESIGN_NAME;
  }

  public static String toDocumentId(Domain domain) {
    return String.format("%s:ocs-config", domain.getDomainId());
  }
}
