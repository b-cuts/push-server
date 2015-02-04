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
import org.crazyyak.dev.common.json.JsonTranslator;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.Providers;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

public class ExecutionContext implements PluginContext {

  private final PushProcessor pushProcessor;

  private Session session;
  private Account account;
  private ApiClient apiClient;

  InetAddress remoteAddress;

  private UriInfo uriInfo;
  private HttpHeaders headers;

  private SecurityContext securityContext;

  private JsonTranslator jsonTranslator;
  private Providers providers;
  private Request request;
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

  public HttpHeaders getHeaders() {
    return headers;
  }

  public void setHeaders(HttpHeaders headers) {
    this.headers = headers;

    if (remoteAddress == null) {
      remoteAddress = resolveFromHeaders(headers);
    }
  }

  private InetAddress resolveFromHeaders(HttpHeaders headers) {
    MultivaluedMap<String, String> values = headers.getRequestHeaders();
    try {
      return InetAddress.getByName("0.0.0.0");

    } catch (UnknownHostException e) {
      return null;
    }
  }

  public SecurityContext getSecurityContext() {
    return securityContext;
  }

  public void setSecurityContext(SecurityContext securityContext) {
    this.securityContext = securityContext;
  }

  public JsonTranslator getJsonTranslator() {
    return jsonTranslator;
  }

  public void setJsonTranslator(JsonTranslator jsonTranslator) {
    this.jsonTranslator = jsonTranslator;
  }

  public Providers getProviders() {
    return providers;
  }

  public void setProviders(Providers providers) {
    this.providers = providers;
  }

  public Request getRequest() {
    return request;
  }

  public void setRequest(Request request) {
    this.request = request;
  }

  public Application getApplication() {
    return application;
  }

  public void setApplication(Application application) {
    this.application = application;
  }

  @Override
  public InetAddress getRemoteAddress() {
    return remoteAddress;
  }

  public AppContext getAppContext() {
    return AppContext.from(application);
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
