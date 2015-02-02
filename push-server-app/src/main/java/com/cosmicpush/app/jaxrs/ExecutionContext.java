package com.cosmicpush.app.jaxrs;

import com.cosmicpush.app.jaxrs.security.*;
import com.cosmicpush.app.system.AppContext;
import com.cosmicpush.common.accounts.*;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.plugins.*;
import com.cosmicpush.common.requests.ApiRequestStore;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.jackson.CpObjectMapper;
import java.net.*;
import javax.servlet.http.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Providers;
import org.crazyyak.dev.common.json.JsonTranslator;

public class ExecutionContext implements PluginContext {

  private Session session;
  private Account account;
  private ApiClient apiClient;

  public PushProcessor pushProcessor;

  private HttpServletRequest servletRequest;
  private HttpServletResponse servletResponse;

  private UriInfo uriInfo;
  private HttpHeaders headers;

  private SecurityContext securityContext;

  private JsonTranslator jsonTranslator;
  private Providers providers;
  private Request request;
  private Application application;

  public ExecutionContext() {
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

  public PushProcessor getPushProcessor() {
    return pushProcessor;
  }

  public void setPushProcessor(PushProcessor pushProcessor) {
    this.pushProcessor = pushProcessor;
  }

  public HttpServletRequest getServletRequest() {
    return servletRequest;
  }

  public void setServletRequest(HttpServletRequest servletRequest) {
    this.servletRequest = servletRequest;
  }

  public HttpServletResponse getServletResponse() {
    return servletResponse;
  }

  public void setServletResponse(HttpServletResponse servletResponse) {
    this.servletResponse = servletResponse;
  }

  public UriInfo getUriInfo() {
    return uriInfo;
  }

  public void setUriInfo(UriInfo uriInfo) {
    this.uriInfo = uriInfo;
  }

  public HttpHeaders getHeaders() {
    return headers;
  }

  public void setHeaders(HttpHeaders headers) {
    this.headers = headers;
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

  public boolean isLocalHost() {
    String serverName = getServletRequest().getServerName();
    return "localhost".equalsIgnoreCase(serverName) || "www.localhost".equalsIgnoreCase(serverName);
  }

  public boolean isNotLocalHost() {
    return !isLocalHost();
  }

  public InetAddress getRemoteAddress() {
    try {
      String remoteAddress = getServletRequest().getRemoteAddr();
      return InetAddress.getByName(remoteAddress);

    } catch (UnknownHostException e) {
      return null;
    }
  }

  public String getServerRoot() {
    String url = servletRequest.getRequestURL().toString();
    String contextPath = servletRequest.getContextPath();
    int pos = url.indexOf(contextPath) + contextPath.length();
    return url.substring(0, pos);
  }

  public AppContext getAppContext() {
    return AppContext.from(application);
  }

  public SessionStore getSessionStore() {
    return getAppContext().getSessionStore();
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
