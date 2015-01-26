/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush;

import com.cosmicpush.common.accounts.AccountStore;
import com.cosmicpush.common.requests.ApiRequestStore;
import com.cosmicpush.common.system.CpCouchServer;
import com.couchace.core.api.*;
import com.couchace.core.api.request.*;
import java.time.ZoneId;
import org.crazyyak.dev.common.DateUtils;
import org.crazyyak.dev.couchace.DefaultCouchServer;

public class TestFactory {

  private static TestFactory SINGLETON;
  public static final ZoneId westCoastTimeZone = DateUtils.PDT;

  private final AccountStore accountStore;
  private final ApiRequestStore apiRequestStore;

  public static TestFactory get() throws Exception {
    if (SINGLETON == null) {
      SINGLETON = new TestFactory();
    }
    return SINGLETON;
  }

  public TestFactory() throws Exception {

    String dbName = "cosmic-push-tests";

    CouchServer server = new DefaultCouchServer();
    CouchDatabase database = server.database(dbName, CouchFeatureSet.builder().add(CouchFeature.ALLOW_DB_DELETE, true).build());
    if (database.exists()) {
      database.deleteDatabase();
    }

    CpCouchServer couchServer = new CpCouchServer(dbName);
    couchServer.validateDatabases();

    accountStore = new AccountStore(couchServer);
    apiRequestStore = new ApiRequestStore(couchServer);
  }

  public AccountStore getAccountStore() {
    return accountStore;
  }

  public ApiRequestStore getApiRequestStore() {
    return apiRequestStore;
  }
}
