/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.accounts.AccountStore;
import com.cosmicpush.common.accounts.actions.CreateAccountAction;
import com.cosmicpush.common.actions.CreateClientAction;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.plugins.PushProcessor;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.common.requests.ApiRequestStore;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.jackson.CpObjectMapper;
import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.push.EmailPush;
import com.cosmicpush.pub.push.SmtpEmailPush;
import com.couchace.core.api.*;
import com.couchace.core.api.request.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.crazyyak.dev.common.DateUtils;
import org.crazyyak.lib.couchace.DefaultCouchServer;

public class TestFactory {

  private static TestFactory SINGLETON;
  public static final ZoneId westCoastTimeZone = DateUtils.PDT;

  private final CpCouchServer couchServer;
  private final CpObjectMapper objectMapper;
  private final AccountStore accountStore;
  private final ApiRequestStore apiRequestStore;

  public static TestFactory get() throws Exception {
    if (SINGLETON == null) {
      SINGLETON = new TestFactory();
    }
    return SINGLETON;
  }

  public TestFactory() throws Exception {

    objectMapper = new CpObjectMapper();

    String dbName = "cosmic-push-tests";

    CouchServer server = new DefaultCouchServer();
    CouchDatabase database = server.database(dbName, CouchFeatureSet.builder().add(CouchFeature.ALLOW_DB_DELETE, true).build());
    if (database.exists()) {
      database.deleteDatabase();
    }

    couchServer = new CpCouchServer(dbName);
    couchServer.validateDatabases();

    accountStore = new AccountStore(couchServer);
    apiRequestStore = new ApiRequestStore(couchServer);
  }

  public CpCouchServer getCouchServer() {
    return couchServer;
  }

  public CpObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public AccountStore getAccountStore() {
    return accountStore;
  }

  public ApiRequestStore getApiRequestStore() {
    return apiRequestStore;
  }

  public Account createAccount() {
    CreateAccountAction createAccount = new CreateAccountAction(
        false, TestFactory.westCoastTimeZone,
        "test", "Test Parr <test@jacobparr.com>",
        "Unit", "Test",
        "testing123", "testing123"
    );

    return new Account(createAccount);
  }

  public ApiClient createApiClient(Account account) {
    CreateClientAction createClient = new CreateClientAction("some.client", "some.password");
    return account.add(createClient);
  }

  public List<ApiRequest> createApiRequests_Emails(ApiClient apiClient) throws Exception {
    List<ApiRequest> requests = new ArrayList<>();

    Push push = new SmtpEmailPush("to@example.com", "from@example.com", "This is the subject", "<html><body><h1>Hello World</h1></body></html>", null);
    requests.add(new ApiRequest(apiClient, push, InetAddress.getLocalHost()));

    return requests;
  }

  public List<ApiRequest> createApiRequests(ApiClient apiClient) throws Exception {
    List<ApiRequest> requests = new ArrayList<>();

    requests.addAll(createApiRequests_Emails(apiClient));

    return requests;
  }

  public PluginContext pluginContext(TestFactory testFactory) {
    return new PluginContext() {
      @Override public AccountStore getAccountStore() {
        return testFactory.getAccountStore();
      }

      @Override
      public InetAddress getRemoteAddress() {
        try {
          return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
          throw new RuntimeException(e);
        }
      }

      @Override
      public ApiRequestStore getApiRequestStore() {
        return testFactory.getApiRequestStore();
      }

      @Override
      public CpObjectMapper getObjectMapper() {
        return testFactory.getObjectMapper();
      }

      @Override
      public CpCouchServer getCouchServer() {
        return testFactory.getCouchServer();
      }

      @Override
      public String getServerRoot() {
        return "http://localhost:8080/push-server";
      }

      @Override
      public PushProcessor getPushProcessor() {
        return null;
      }
    };
  }
}
