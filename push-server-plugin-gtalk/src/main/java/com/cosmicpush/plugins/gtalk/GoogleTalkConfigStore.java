package com.cosmicpush.plugins.gtalk;

import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.system.CpCouchServer;
import org.crazyyak.dev.couchace.DefaultCouchStore;

public class GoogleTalkConfigStore extends DefaultCouchStore<GoogleTalkConfig> {

  public static final String GOOGLE_TALK_CONFIG_DESIGN_NAME = "google-talk-config";

  public GoogleTalkConfigStore(CpCouchServer couchServer) {
    super(couchServer, couchServer.getDatabaseName(), GoogleTalkConfig.class);
  }

  @Override
  public String getDesignName() {
    return GOOGLE_TALK_CONFIG_DESIGN_NAME;
  }

  public static String toDocumentId(ApiClient apiClient) {
    return String.format("%s:google-talk-config", apiClient.getApiClientId());
  }
}
