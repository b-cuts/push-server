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

  private final String databaseName;
  private final CouchDatabase database;

  private static final List<String> designNames = Arrays.asList("account", "api-request");
  private static final String prefix = "/push-server-app/design-docs/";
  private static final String suffix = "-design.json";

  public CpCouchServer(String databaseName) throws IOException {
    super(new Module[]{
      new YakJacksonModule(),
      new CpJacksonModule()});

    this.databaseName = ExceptionUtils.assertNotZeroLength(databaseName, "databaseName");
    this.database = database(databaseName);

    validateDatabases();
  }

  public String getDatabaseName() {
    return databaseName;
  }

  public CouchDatabase getDatabase() {
    return database;
  }

  public void validateDatabases() throws IOException {
    CouchUtils.createDatabase(database, new TimeUuidIdGenerator(), "/push-server-app/json/account.json");
    CouchUtils.validateDesign(database, designNames, prefix, suffix);

    compactAndCleanAll();
  }

  public void compactAndCleanAll() {
    CouchUtils.compactAndCleanAll(database, designNames);
  }
}
