package com.cosmicpush.common.system;

import com.cosmicpush.jackson.CpJacksonModule;
import com.couchace.core.api.CouchDatabase;
import com.fasterxml.jackson.databind.Module;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;
import org.crazyyak.dev.common.id.TimeUuidIdGenerator;
import org.crazyyak.dev.jackson.YakJacksonModule;
import org.crazyyak.lib.couchace.DefaultCouchServer;
import org.crazyyak.lib.couchace.support.CouchUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CpCouchServer extends DefaultCouchServer {

  public static String DATABASE_NAME = "cosmic-push";

  public static final List<String> designNames = Arrays.asList("account", "push-request", "domain");
  public static final String prefix = "/push-server-common/design-docs/";
  public static final String suffix = "-design.json";

  public CpCouchServer() {
    super(new Module[]{
      new YakJacksonModule(),
      new CpJacksonModule()});
  }
}
