/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources;

import com.cosmicpush.common.accounts.AccountStore;
import com.cosmicpush.common.plugins.*;
import com.cosmicpush.common.requests.ApiRequestStore;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.jackson.CpObjectMapper;
import java.net.*;
import java.security.Principal;
import javax.servlet.http.*;
import javax.ws.rs.core.*;
import org.crazyyak.dev.common.json.JsonTranslator;
import org.crazyyak.dev.jackson.YakJacksonTranslator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class RequestConfig implements PluginContext {

  public PushProcessor pushProcessor;

  private HttpServletRequest request;
  private HttpServletResponse response;

  private UriInfo uriInfo;
  private HttpHeaders headers;
  protected UserDetails userDetails;

  private final AuthenticationManager authenticationManager;
  private final CpObjectMapper objectMapper;
  private final AccountStore accountStore;
  private final ApiRequestStore apiRequestStore;
  private final CpCouchServer couchServer;

  private final JsonTranslator jsonTranslator;

  public RequestConfig(AuthenticationManager authenticationManager, CpObjectMapper objectMapper, CpCouchServer couchServer, AccountStore accountStore, ApiRequestStore apiRequestStore) {
    this.authenticationManager = authenticationManager;
    this.couchServer = couchServer;
    this.objectMapper = objectMapper;
    this.accountStore = accountStore;
    this.apiRequestStore = apiRequestStore;
    this.jsonTranslator = new YakJacksonTranslator(objectMapper);
    this.pushProcessor = new PushProcessor(this);
  }

  public RequestConfig initialize(HttpServletRequest request, HttpServletResponse response, UriInfo uriInfo, HttpHeaders headers, SecurityContext securityContext) {
    this.request = request;
    this.response = response;

    this.uriInfo = uriInfo;
    this.headers = headers;

    String content = headers.getHeaderString("Authorization");

    if (securityContext != null) {
      Principal userPrincipal = securityContext.getUserPrincipal();
      if (userPrincipal instanceof Authentication) {
        Authentication authentication = (Authentication)securityContext.getUserPrincipal();
        if (authentication.getPrincipal() instanceof UserDetails) {
          userDetails = (UserDetails)authentication.getPrincipal();
        }
      }
    }

    return this;
  }

  public UriInfo getUriInfo() {
    return uriInfo;
  }

  public HttpHeaders getHeaders() {
    return headers;
  }

  public boolean isLocalHost() {
    String serverName = getRequest().getServerName();
    return "localhost".equalsIgnoreCase(serverName) || "www.localhost".equalsIgnoreCase(serverName);
  }

  public boolean isNotLocalHost() {
    return !isLocalHost();
  }

  public HttpServletRequest getRequest() {
    return request;
  }

  public HttpServletResponse getResponse() {
    return response;
  }

  public AuthenticationManager getAuthenticationManager() {
    return authenticationManager;
  }

  @Override
  public CpObjectMapper getObjectMapper() {
    return objectMapper;
  }

  @Override
  public AccountStore getAccountStore() {
    return accountStore;
  }

  @Override
  public ApiRequestStore getApiRequestStore() {
    return apiRequestStore;
  }

  public JsonTranslator getTranslator() {
    return jsonTranslator;
  }

  @Override
  public InetAddress getRemoteAddress() {
    try {
      String remoteAddress = getRequest().getRemoteAddr();
      return InetAddress.getByName(remoteAddress);

    } catch (UnknownHostException e) {
      return null;
    }
  }

  @Override
  public CpCouchServer getCouchServer() {
    return couchServer;
  }

  @Override
  public String getServerRoot() {
    String url = request.getRequestURL().toString();
    String contextPath = request.getContextPath();
    int pos = url.indexOf(contextPath) + contextPath.length();
    return url.substring(0, pos);
  }

  @Override
  public PushProcessor getPushProcessor() {
    return pushProcessor;
  }
}
