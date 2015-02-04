package com.cosmicpush.app.jaxrs;

import com.cosmicpush.app.jaxrs.security.Session;
import com.cosmicpush.app.jaxrs.security.SessionStore;
import com.cosmicpush.app.system.AppContext;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.accounts.AccountStore;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.plugins.PushProcessor;
import com.cosmicpush.common.requests.ApiRequestStore;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.jackson.CpObjectMapper;
import org.crazyyak.dev.common.StringUtils;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ExecutionContext implements PluginContext {

  private final PushProcessor pushProcessor;

  private Session session;
  private Account account;
  private ApiClient apiClient;

  private UriInfo uriInfo;

  private Application application;

  public ExecutionContext() {
    this.pushProcessor = new PushProcessor(this);
  }

  public void setSession(Session session) {
    this.session = session;
  }

  public Session getSession() {
    return session;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public Account getAccount() {
    return account;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public UriInfo getUriInfo() {
    return uriInfo;
  }

  public void setUriInfo(UriInfo uriInfo) {
    this.uriInfo = uriInfo;
  }

  @Override
  public URI getBaseURI() {
    String uri = uriInfo.getBaseUri().toASCIIString();
    return URI.create(StringUtils.substring(uri, 0, -1));
  }

  public Application getApplication() {
    return application;
  }

  public void setApplication(Application application) {
    this.application = application;
  }

  public AppContext getAppContext() {
    return AppContext.from(getApplication());
  }

  public SessionStore getSessionStore() {
    return getAppContext().getSessionStore();
  }

  @Override
  public PushProcessor getPushProcessor() {
    return pushProcessor;
  }

  @Override
  public CpObjectMapper getObjectMapper() {
    return getAppContext().getObjectMapper();
  }

  @Override
  public AccountStore getAccountStore() {
    return getAppContext().getAccountStore();
  }

  @Override
  public ApiRequestStore getApiRequestStore() {
    return getAppContext().getApiRequestStore();
  }

  @Override
  public CpCouchServer getCouchServer() {
    return getAppContext().getCouchServer();
  }
}
