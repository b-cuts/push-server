package com.cosmicpush.app.jaxrs.security;

import com.cosmicpush.app.resources.RootResource;
import com.cosmicpush.app.system.*;
import com.cosmicpush.common.accounts.Account;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import org.crazyyak.dev.common.exceptions.ApiException;

@Provider
@MngtAuthentication
@Priority(Priorities.AUTHENTICATION + 1)
public class MngtAuthenticationFilter implements ContainerRequestFilter {

  private final AppContext appContext;

  public MngtAuthenticationFilter(@Context Application application) {
    this.appContext = AppContext.from(application);
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {

    try {
      Session session = appContext.getSessionStore().getSession(requestContext);
      if (session == null) {
        throw ApiException.unauthorized();
      }

      String username = session.getUsername();
      Account account = appContext.getAccountStore().getByUsername(username);

      if (account == null) {
        throw ApiException.unauthorized();
      }

      final SecurityContext securityContext = requestContext.getSecurityContext();
      requestContext.setSecurityContext(new MngtSecurityContext(securityContext, account));
      CpApplication.getExecutionContext().setAccount(account);

    } catch (ApiException e) {
      URI uri = requestContext.getUriInfo().getBaseUriBuilder().queryParam("r", RootResource.REASON_CODE_UNAUTHORIZED).build();
      Response response = Response.seeOther(uri).build();
      requestContext.abortWith(response);
    }
  }

  private static class MngtSecurityContext implements SecurityContext {
    private final boolean secure;
    private final Account account;
    public MngtSecurityContext(SecurityContext securityContext, Account account) {
      this.account = account;
      this.secure = securityContext.isSecure();
    }
    @Override public boolean isUserInRole(String role) {
      return false;
    }
    @Override public boolean isSecure() {
      return secure;
    }
    @Override public String getAuthenticationScheme() {
      return "FORM_AUTH";
    }
    @Override public Principal getUserPrincipal() {
      return account::getUserName;
    }
  }

  public static class MngtThreadLocal extends ThreadLocal {

  }
}
