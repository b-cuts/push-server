/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.app.resources;

import com.cosmicpush.app.jaxrs.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.Session;
import com.cosmicpush.app.jaxrs.security.SessionStore;
import com.cosmicpush.app.resources.api.ApiResourceV1;
import com.cosmicpush.app.resources.api.ApiResourceV2;
import com.cosmicpush.app.resources.manage.ManageResource;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.app.view.Thymeleaf;
import com.cosmicpush.app.view.ThymeleafViewFactory;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.common.system.PluginManager;
import com.cosmicpush.pub.push.NotificationPush;
import com.cosmicpush.pub.push.UserEventPush;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.crazyyak.dev.common.EqualsUtils;
import org.crazyyak.dev.common.exceptions.ApiException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/")
public class RootResource extends RootResourceSupport {

  public static final int REASON_CODE_INVALID_USERNAME_OR_PASSWORD = -1;
  public static final int REASON_CODE_UNAUTHORIZED = -2;

  private static final Log log = LogFactory.getLog(RootResource.class);

  private final ExecutionContext context = CpApplication.getExecutionContext();

  public RootResource(@Context UriInfo uriInfo) {
    log.info("Created ");

    // Force initialization.
    PluginManager.getPlugins();

    context.setUriInfo(uriInfo);
  }

  @Override
  public UriInfo getUriInfo() {
    return context.getUriInfo();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf getWelcome(@QueryParam("r") int reasonCode, @QueryParam("username") String username, @QueryParam("password") String password) throws IOException {

    String message = "";
    if (REASON_CODE_INVALID_USERNAME_OR_PASSWORD == reasonCode) {
      message = "Invalid username or password.";
    } else if (REASON_CODE_UNAUTHORIZED == reasonCode) {
      message = "Your session has expired.";
    }
    return new Thymeleaf(ThymeleafViewFactory.WELCOME, new WelcomeModel(context.getAccount(), message, username, password));
  }

  public static class WelcomeModel {
    private final Account account;
    private final String message;
    private final String username;
    private final String password;
    public WelcomeModel(Account account, String message, String username, String password) {
      this.account = account;
      this.message = message;
      this.username = username;
      this.password = password;
    }
    public Account getAccount() { return account; }
    public String getMessage() { return message; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
  }

  @POST
  @Path("/sign-in")
  @Produces(MediaType.TEXT_HTML)
  public Response signIn(@FormParam("username") String username, @FormParam("password") String password, @CookieParam(SessionStore.SESSION_COOKIE_NAME) String sessionId) throws Exception {

    Account account = context.getAccountStore().getByUsername(username);

    if (account == null || EqualsUtils.objectsNotEqual(account.getPassword(), password)) {
      context.getSessionStore().remove(sessionId);

      NewCookie sessionCookie = SessionStore.toCookie(getUriInfo(), null);
      URI other = getUriInfo().getBaseUriBuilder().queryParam("r", -1).build();
      return Response.seeOther(other).cookie(sessionCookie).build();
    }

    Session session = context.getSessionStore().newSession(username);

    NewCookie sessionCookie = SessionStore.toCookie(getUriInfo(), session);
    URI other = getUriInfo().getBaseUriBuilder().path("manage").build();
    return Response.seeOther(other).cookie(sessionCookie).build();
  }

  @Path("/api")
  public ApiResourceV1 getApiResourceV1() throws Exception {
    return new ApiResourceV1();
  }

  @Path("/api/v2")
  public ApiResourceV2 getApiResourceV2() throws Exception {
    return new ApiResourceV2();
  }

  @Path("/manage")
  public ManageResource getManageResource() {
    return new ManageResource();
  }

  @GET @Path("/q/{id}")
  public Response resolveCallback(@PathParam("id") String id) throws URISyntaxException {

    if (id.startsWith("api-request:")) {
      id = id.substring(12);
      return resolveCallback(id);

    } else if (id.contains(":") == false) {
      ApiRequest request = context.getApiRequestStore().getByApiRequestId(id);
      if (request == null) {
        throw ApiException.notFound("API request not found for " + id);
      }

      Account account = context.getAccountStore().getByClientId(request.getApiClientId());
      if (account == null) {
        throw ApiException.notFound("Account not found given client id " + request.getApiClientId());
      }

      ApiClient apiClient = account.getApiClientById(request.getApiClientId());
      if (apiClient == null) {
        throw ApiException.notFound("API client not found for " + request.getApiClientId());
      }

      if (NotificationPush.PUSH_TYPE.equals(request.getPushType())) {
        String path = String.format("manage/api-client/%s/notifications/%s", apiClient.getClientName(), id);
        return Response.seeOther(new URI(path)).build();

      } else if (UserEventPush.PUSH_TYPE.equals(request.getPushType())) {
        String deviceId = request.getUserEventPush().getDeviceId();
        String path = String.format("manage/api-client/%s/user-events/%s", apiClient.getClientName(), deviceId);
        return Response.seeOther(new URI(path)).build();
      }

    }

    return Response.seeOther(new URI("")).build();
  }

  @GET @Path("/health-check")
  @Produces(MediaType.TEXT_HTML)
  public Response healthCheck$GET() {
    return Response.status(Response.Status.OK).build();
  }

  @GET @Path("/privacy-policy")
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf privacyPolicy() {
    return new Thymeleaf("/mun-mon/general/privacy-policy.jsp");
  }

  @GET @Path("/terms-of-service")
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf termsOfService() {
    return new Thymeleaf("/mun-mon/general/terms-of-service.jsp");
  }

  @GET // TODO - implement the faq.jsp page.
  @Path("{resource: (faq\\.).* }")
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf getFaq() {
    return new Thymeleaf("/mun-mon/faq.jsp");
  }

  @GET // TODO - implement the contact.jsp page.
  @Path("{resource: (contact\\.).* }")
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf getContact() {
    return new Thymeleaf("/mun-mon/contact.jsp");
  }
}

