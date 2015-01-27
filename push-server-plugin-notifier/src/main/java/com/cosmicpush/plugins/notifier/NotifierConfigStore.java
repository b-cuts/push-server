package com.cosmicpush.plugins.notifier;

import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.system.CpCouchServer;
import org.crazyyak.lib.couchace.DefaultCouchStore;

public class NotifierConfigStore extends DefaultCouchStore<NotifierConfig> {

  public static final String GOOGLE_TALK_CONFIG_DESIGN_NAME = "google-talk-config";

  public NotifierConfigStore(CpCouchServer couchServer) {
    super(couchServer, couchServer.getDatabaseName(), NotifierConfig.class);
  }

  @Override
  public String getDesignName() {
    return GOOGLE_TALK_CONFIG_DESIGN_NAME;
  }

  public static String toDocumentId(ApiClient apiClient) {
    return String.format("%s:google-talk-config", apiClient.getApiClientId());
  }
}
