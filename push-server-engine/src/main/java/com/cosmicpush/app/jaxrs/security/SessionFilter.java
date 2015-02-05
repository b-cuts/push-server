package com.cosmicpush.app.jaxrs.security;

import com.cosmicpush.app.jaxrs.ExecutionContext;
import com.cosmicpush.app.system.AppContext;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.accounts.AccountStore;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Arrays;

@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class SessionFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private final AppContext appContext;
  private final Application application;

  public SessionFilter(@Context Application application) {
    this.application = application;
    this.appContext = AppContext.from(application);
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    ExecutionContext execContext = CpApplication.getExecutionContext();

    // Before anything, make sure the execution
    // context has a reference to the application.
    execContext.setApplication(application);

    Session session = appContext.getSessionStore().getSession(requestContext);
    CpApplication.getExecutionContext().setSession(session);

    AccountStore accountStore = execContext.getAccountStore();

    if (session != null) {
      Account account = accountStore.getByUsername(session.getUsername());
      execContext.setAccount(account);
    }
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    Session session = CpApplication.getExecutionContext().getSession();
    boolean valid = appContext.getSessionStore().isValid(session);

    if (session != null && valid) {
      session.renew();
      NewCookie cookie = SessionStore.toCookie(requestContext.getUriInfo(), session);
      responseContext.getHeaders().put(HttpHeaders.SET_COOKIE, Arrays.asList(cookie));
    }

    // Clear everything when we are all done.
    CpApplication.removeExecutionContext();
  }
}
