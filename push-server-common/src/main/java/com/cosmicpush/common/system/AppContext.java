package com.cosmicpush.common.system;

import com.cosmicpush.common.accounts.AccountStore;
import com.cosmicpush.common.accounts.DomainStore;
import com.cosmicpush.common.requests.PushRequestStore;
import com.cosmicpush.jackson.CpObjectMapper;
import org.crazyyak.apis.bitly.BitlyApis;

import javax.ws.rs.core.Application;

public class AppContext {

  public static final int CURRENT_API_VERSION = 2;

  private final BitlyApis bitlyApis;
  private final SessionStore sessionStore;
  private final CpObjectMapper objectMapper;
  private final CpCouchServer cpCouchServer;
  private final AccountStore accountStore;
  private final PushRequestStore pushRequestStore;
  private final DomainStore domainStore;

  public AppContext(SessionStore sessionStore, CpObjectMapper objectMapper, CpCouchServer cpCouchServer, BitlyApis bitlyApis) {
    this.bitlyApis = bitlyApis;
    this.sessionStore = sessionStore;
    this.objectMapper = objectMapper;
    this.cpCouchServer = cpCouchServer;
    this.accountStore = new AccountStore(cpCouchServer);
    this.domainStore = new DomainStore(cpCouchServer);
    this.pushRequestStore = new PushRequestStore(cpCouchServer);
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

  public PushRequestStore getPushRequestStore() {
    return pushRequestStore;
  }

  public CpCouchServer getCouchServer() {
    return cpCouchServer;
  }

  public DomainStore getDomainStore() {
    return domainStore;
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
