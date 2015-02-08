package com.cosmicpush.app.jaxrs;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.accounts.AccountStore;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.plugins.PushProcessor;
import com.cosmicpush.common.requests.ApiRequestStore;
import com.cosmicpush.common.system.AppContext;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.common.system.Session;
import com.cosmicpush.common.system.SessionStore;
import com.cosmicpush.jackson.CpObjectMapper;
import org.crazyyak.dev.common.StringUtils;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ExecutionContext implements PluginContext {

  private final PushProcessor pushProcessor;

  private URI baseURI;
  private Session session;
  private Account account;
  private ApiClient apiClient;

  private UriInfo uriInfo;
  private HttpHeaders headers;

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

    String uri = uriInfo.getBaseUri().toASCIIString();
    this.baseURI = URI.create(StringUtils.substring(uri, 0, -1));
  }

  @Override
  public URI getBaseURI() {
    return baseURI;
  }

  public Application getApplication() {
    return application;
  }

  public void setApplication(Application application) {
    this.application = application;
  }

  @Override
  public AppContext getAppContext() {
    return AppContext.from(getApplication());
  }

  public SessionStore getSessionStore() {
    return getAppContext().getSessionStore();
  }

  public HttpHeaders getHeaders() {
    return headers;
  }

  public void setHeaders(HttpHeaders headers) {
    this.headers = headers;
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
