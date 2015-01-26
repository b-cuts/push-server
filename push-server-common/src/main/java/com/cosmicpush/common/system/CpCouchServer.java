package com.cosmicpush.common.system;

import com.cosmicpush.jackson.CpJacksonModule;
import com.couchace.core.api.CouchDatabase;
import com.fasterxml.jackson.databind.Module;
import java.io.IOException;
import java.util.*;
import javax.annotation.PostConstruct;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;
import org.crazyyak.dev.common.id.TimeUuidIdGenerator;
import org.crazyyak.dev.couchace.DefaultCouchServer;
import org.crazyyak.dev.couchace.support.CouchUtils;
import org.crazyyak.dev.jackson.YakJacksonModule;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

@Component
public class CpCouchServer extends DefaultCouchServer {

  private final String databaseName;
  private final CouchDatabase database;

  private static final List<String> designNames = Arrays.asList("account", "api-request");
  private static final String prefix = "/push-server/design-docs/";
  private static final String suffix = "-design.json";

  @Autowired
  public CpCouchServer(@Value("${couchdb.name}") String databaseName) {
    super(new Module[]{
      new YakJacksonModule(),
      new CpJacksonModule()});

    this.databaseName = ExceptionUtils.assertNotZeroLength(databaseName, "databaseName");
    this.database = database(databaseName);
  }

  public String getDatabaseName() {
    return databaseName;
  }

  public CouchDatabase getDatabase() {
    return database;
  }

  @PostConstruct
  public void validateDatabases() throws IOException {
    CouchUtils.createDatabase(database, new TimeUuidIdGenerator(), "/push-server/json/account.json");
    CouchUtils.validateDesign(database, designNames, prefix, suffix);

    compactAndCleanAll();
  }

  public void compactAndCleanAll() {
    CouchUtils.compactAndCleanAll(database, designNames);
  }
}
