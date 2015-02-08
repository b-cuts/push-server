package com.cosmicpush.common.system;

import com.cosmicpush.common.accounts.AccountStore;
import com.cosmicpush.common.requests.ApiRequestStore;
import com.cosmicpush.jackson.CpObjectMapper;
import org.crazyyak.apis.bitly.BitlyApis;

import javax.ws.rs.core.Application;

public class AppContext {

  private final BitlyApis bitlyApis;
  private final SessionStore sessionStore;
  private final CpObjectMapper objectMapper;
  private final CpCouchServer cpCouchServer;
  private final AccountStore accountStore;
  private final ApiRequestStore apiRequestStore;

  public AppContext(SessionStore sessionStore, CpObjectMapper objectMapper, CpCouchServer cpCouchServer, BitlyApis bitlyApis) {
    this.bitlyApis = bitlyApis;
    this.sessionStore = sessionStore;
    this.objectMapper = objectMapper;
    this.cpCouchServer = cpCouchServer;
    this.accountStore = new AccountStore(cpCouchServer);
    this.apiRequestStore = new ApiRequestStore(cpCouchServer);
  }

  public BitlyApis getBitlyApi() {
    return bitlyApis;
  }

  public CpObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public SessionStore getSessionStore() {
    return sessionStore;
  }

  public AccountStore getAccountStore() {
    return accountStore;
  }

  public ApiRequestStore getApiRequestStore() {
    return apiRequestStore;
  }

  public CpCouchServer getCouchServer() {
    return cpCouchServer;
  }

  public static AppContext from(Application application) {
    Object object = application.getProperties().get(AppContext.class.getName());
    if (object == null) {
      throw new IllegalStateException("The application context has yet to be initialized.");
    } else if (AppContext.class.isInstance(object) == false) {
      String msg = String.format("The application object is not an instance of %s but rather %s.", AppContext.class.getName(), object.getClass().getName());
      throw new IllegalStateException(msg);
    }
    return (AppContext)object;
  }
}
