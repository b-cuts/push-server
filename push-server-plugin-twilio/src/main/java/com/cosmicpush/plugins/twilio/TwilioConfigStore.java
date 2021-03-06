/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.twilio;

import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.system.CpCouchServer;
import org.crazyyak.lib.couchace.DefaultCouchStore;

public class TwilioConfigStore extends DefaultCouchStore<TwilioConfig> {

  public static final String TWILIO_CONFIG_DESIGN_NAME = "twilio-config";

  public TwilioConfigStore(CpCouchServer couchServer) {
    super(couchServer, TwilioConfig.class);
  }

  @Override
  public String getDatabaseName() {
    return CpCouchServer.DATABASE_NAME;
  }

  @Override
  public String getDesignName() {
    return TWILIO_CONFIG_DESIGN_NAME;
  }

  public static String toDocumentId(Domain domain) {
    return String.format("%s:twilio-config", domain.getDomainId());
  }
}
