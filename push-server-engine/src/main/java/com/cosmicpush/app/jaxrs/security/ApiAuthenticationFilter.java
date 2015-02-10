package com.cosmicpush.app.jaxrs.security;

import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
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

    String domainKey;
    String password;

    if (pos < 0) {
      domainKey = basicAuth;
      password = null;

    } else {
      domainKey = basicAuth.substring(0, pos);
      password = basicAuth.substring(pos+1);
    }

    Domain domain = appContext.getDomainStore().getByDomainKey(domainKey);
    if (domain == null) {
      throw new NotAuthorizedException("API");
    }

    if (EqualsUtils.objectsNotEqual(password, domain.getDomainPassword())) {
      throw new NotAuthorizedException("API");
    }

    final SecurityContext securityContext = requestContext.getSecurityContext();
    requestContext.setSecurityContext(new ApiSecurityContext(securityContext, domain));

    CpApplication.getExecutionContext().setDomain(domain);
  }

  private class ApiSecurityContext implements SecurityContext {
    private final boolean secure;
    private final Domain domain;
    public ApiSecurityContext(SecurityContext securityContext, Domain domain) {
      this.domain = domain;
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
      return domain::getDomainKey;
    }
  }
}
