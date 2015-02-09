package com.cosmicpush.plugins.ses;

import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.system.CpCouchServer;
import org.crazyyak.lib.couchace.DefaultCouchStore;

public class SesEmailConfigStore extends DefaultCouchStore<SesEmailConfig> {

  public static final String SES_EMAIL_CONFIG_DESIGN_NAME = "ses-email-config";


  public SesEmailConfigStore(CpCouchServer couchServer) {
    super(couchServer, couchServer.getDatabaseName(), SesEmailConfig.class);
  }

  @Override
  public String getDesignName() {
    return SES_EMAIL_CONFIG_DESIGN_NAME;
  }

  public static String toDocumentId(Domain domain) {
    return String.format("%s:ses-email-config", domain.getDomainId());
  }
}
