package com.cosmicpush.common;

import com.cosmicpush.common.accounts.AccountStore;
import com.cosmicpush.common.requests.ApiRequestStore;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.jackson.CpObjectMapper;

public class DiyBeanFactory {

  public static DiyBeanFactory SINGLETON = new DiyBeanFactory();
  public static DiyBeanFactory get() {
    return SINGLETON;
  }

  private final String databaseName;
  private final CpObjectMapper objectMapper;
  private final CpCouchServer couchServer;
  private final AccountStore accountStore;
  private final ApiRequestStore apiRequestStore;

  private DiyBeanFactory() {
    databaseName = "cosmic-push";
    this.objectMapper = new CpObjectMapper();
    couchServer = new CpCouchServer(databaseName);
    accountStore = new AccountStore(couchServer);
    apiRequestStore = new ApiRequestStore(couchServer);
  }

  public CpCouchServer getCouchServer() {
    return couchServer;
  }

  public AccountStore getAccountStore() {
    return accountStore;
  }

  public ApiRequestStore getApiRequestStore() {
    return apiRequestStore;
  }

  public CpObjectMapper getObjectMapper() {
    return objectMapper;
  }
}
