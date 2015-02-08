package com.cosmicpush.app.jaxrs.security;

import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.system.AppContext;
import org.crazyyak.dev.common.EqualsUtils;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

@ApiAuthentication
@Priority(Priorities.AUTHENTICATION + 1)
public class ApiAuthenticationFilter implements ContainerRequestFilter {

  private final AppContext appContext;

  public ApiAuthenticationFilter(@Context Application application) {
    this.appContext = AppContext.from(application);
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String authHeader = requestContext.getHeaderString("Authorization");

    if (authHeader == null) {
      throw new NotAuthorizedException("API");
    } else if (authHeader.startsWith("Basic ") == false) {
      throw new NotAuthorizedException("API");
    } else {
      authHeader = authHeader.substring(6);
    }

    byte[] bytes = DatatypeConverter.parseBase64Binary(authHeader);
    String basicAuth = new String(bytes, StandardCharsets.UTF_8);

    int pos = basicAuth.indexOf(":");

    String clientName;
    String password;

    if (pos < 0) {
      clientName = basicAuth;
      password = null;

    } else {
      clientName = basicAuth.substring(0, pos);
      password = basicAuth.substring(pos+1);
    }

    Account account = appContext.getAccountStore().getByClientName(clientName);
    if (account == null) {
      throw new NotAuthorizedException("API");
    }

    ApiClient apiClient = account.getApiClientByName(clientName);
    if (apiClient == null) {
      throw new NotAuthorizedException("API");
    }

    if (EqualsUtils.objectsNotEqual(password, apiClient.getClientPassword())) {
      throw new NotAuthorizedException("API");
    }

    final SecurityContext securityContext = requestContext.getSecurityContext();
    requestContext.setSecurityContext(new ApiSecurityContext(securityContext, apiClient));

    CpApplication.getExecutionContext().setAccount(account);
    CpApplication.getExecutionContext().setApiClient(apiClient);
  }

  private class ApiSecurityContext implements SecurityContext {
    private final boolean secure;
    private final ApiClient apiClient;
    public ApiSecurityContext(SecurityContext securityContext, ApiClient apiClient) {
      this.apiClient = apiClient;
      this.secure = securityContext.isSecure();
    }
    @Override public boolean isUserInRole(String role) {
      return false;
    }
    @Override public boolean isSecure() {
      return secure;
    }
    @Override public String getAuthenticationScheme() {
      return "BASIC_AUTH";
    }
    @Override public Principal getUserPrincipal() {
      return apiClient::getClientName;
    }
  }
}
