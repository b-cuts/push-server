package com.cosmicpush.app.security;

import com.amazonaws.services.cognitoidentity.model.NotAuthorizedException;
import com.cosmicpush.common.DiyBeanFactory;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import org.crazyyak.dev.common.EqualsUtils;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

@ApiAuthentication
@Priority(Priorities.AUTHENTICATION)
public class ApiAuthenticationFilter implements ContainerRequestFilter {

  public ApiAuthenticationFilter() {
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

    DiyBeanFactory factory = DiyBeanFactory.get();
    Account account = factory.getAccountStore().getByClientName(clientName);
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
    requestContext.setSecurityContext(new SecurityContext() {
      @Override public boolean isUserInRole(String role) {
        return false;
      }
      @Override public boolean isSecure() {
        return false;
      }
      @Override public String getAuthenticationScheme() {
        return null;
      }
      @Override public Principal getUserPrincipal() {
        return new Principal() {
          @Override
          public String getName() {
            return clientName;
          }
        };
      }
    });
  }
}
