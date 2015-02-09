package com.cosmicpush.plugins.gtalk;

import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.system.CpCouchServer;
import org.crazyyak.lib.couchace.DefaultCouchStore;

public class GoogleTalkConfigStore extends DefaultCouchStore<GoogleTalkConfig> {

  public static final String GOOGLE_TALK_CONFIG_DESIGN_NAME = "google-talk-config";

  public GoogleTalkConfigStore(CpCouchServer couchServer) {
    super(couchServer, couchServer.getDatabaseName(), GoogleTalkConfig.class);
  }

  @Override
  public String getDesignName() {
    return GOOGLE_TALK_CONFIG_DESIGN_NAME;
  }

  public static String toDocumentId(Domain domain) {
    return String.format("%s:google-talk-config", domain.getDomainId());
  }
}
