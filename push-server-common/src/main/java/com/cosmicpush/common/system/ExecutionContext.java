package com.cosmicpush.common.system;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.accounts.AccountStore;
import com.cosmicpush.common.accounts.DomainStore;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.plugins.PushProcessor;
import com.cosmicpush.common.requests.ApiRequestStore;
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
  private Domain domain;

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
    return (session != null) ? session : new Session(-1, "dummy-session");
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public Account getAccount() {
    return account;
  }

  public void setDomain(Domain domain) {
    this.domain = domain;
  }

  public Domain getDomain() {
    return domain;
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

  public SessionStore getSessionStore() {
    return getAppContext().getSessionStore();
  }

  @Override
  public DomainStore getDomainStore() {
    return getAppContext().getDomainStore();
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

  @Override
  public void setLastMessage(String message) {
    if (session != null) {
      session.setLastMessage(message);
    }
  }
}
