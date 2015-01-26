package com.cosmicpush.plugins.smtp;

import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.system.CpCouchServer;
import org.crazyyak.dev.couchace.DefaultCouchStore;

public class SmtpEmailConfigStore extends DefaultCouchStore<SmtpEmailConfig> {

  public static final String SMTP_EMAIL_CONFIG_DESIGN_NAME = "smtp-email-config";


  public SmtpEmailConfigStore(CpCouchServer couchServer) {
    super(couchServer, couchServer.getDatabaseName(), SmtpEmailConfig.class);
  }

  @Override
  public String getDesignName() {
    return SMTP_EMAIL_CONFIG_DESIGN_NAME;
  }

  public static String toDocumentId(ApiClient apiClient) {
    return String.format("%s:smtp-email-config", apiClient.getApiClientId());
  }
}
