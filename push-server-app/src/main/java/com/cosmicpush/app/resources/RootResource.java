/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources;

import com.cosmicpush.app.resources.api.*;
import com.cosmicpush.app.resources.manage.*;
import com.cosmicpush.common.accounts.*;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.*;
import com.cosmicpush.common.system.*;
import com.cosmicpush.jackson.CpObjectMapper;
import com.cosmicpush.pub.common.PushType;
import java.net.*;
import javax.servlet.http.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.apache.commons.logging.*;
import org.crazyyak.dev.common.exceptions.ApiException;
import org.glassfish.jersey.server.mvc.Viewable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

@Path("/")
@Component
public class RootResource {

  private static final Log log = LogFactory.getLog(RootResource.class);

  private HttpServletRequest servletRequest;
  private HttpServletResponse servletResponse;
  private SecurityContext securityContext;
  private UriInfo uriInfo;
  private HttpHeaders headers;

  private AuthenticationManager authenticationManager;
  private CpObjectMapper objectMapper;
  private CpCouchServer couchServer;
  private AccountStore accountStore;
  private ApiRequestStore apiRequestStore;

  RequestConfig requestConfig;

  @Autowired
  public RootResource(AuthenticationManager authenticationManager,
                      CpObjectMapper objectMapper,
                      CpCouchServer couchServer,
                      AccountStore accountStore,
                      ApiRequestStore apiRequestStore) {

    this.authenticationManager = authenticationManager;
    this.objectMapper = objectMapper;
    this.couchServer = couchServer;
    this.accountStore = accountStore;
    this.apiRequestStore = apiRequestStore;

    log.info("Created " + getClass().getName());
    // Force initialization.
    PluginManager.getPlugins();
  }

  @Context
  public void setContext(javax.ws.rs.ext.Providers providers,
                         HttpServletRequest servletRequest,
                         HttpServletResponse servletResponse,
                         SecurityContext securityContext,
                         UriInfo uriInfo,
                         HttpHeaders headers) {
    
    this.servletRequest = servletRequest;
    this.servletResponse = servletResponse;
    this.securityContext = securityContext;
    this.uriInfo = uriInfo;
    this.headers = headers;

    this.requestConfig = new RequestConfig(
        authenticationManager,
        objectMapper,
        couchServer,
        accountStore,
        apiRequestStore,
        servletRequest,
        servletResponse,
        uriInfo,
        headers,
        securityContext
    );
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Viewable getPublicResource() {
    return new PublicResource(requestConfig).getWelcome();
  }

  @Path("/api")
  public ApiResourceV1 getApiResourceV1() throws Exception {
    ApiRequestConfig config = new ApiRequestConfig(authenticationManager, objectMapper, couchServer, accountStore, apiRequestStore, servletRequest, servletResponse,
      uriInfo, headers, securityContext
    );

    String accountId = config.getApiClientUser().getAccountId();
    Account account = config.getAccountStore().getByAccountId(accountId);

    String clientName = config.getApiClientUser().getClientName();
    ApiClient apiClient = account.getApiClientByName(clientName);

    return new ApiResourceV1(config, account, apiClient);
  }

  @Path("/api/v2")
  public ApiResourceV2 getApiResourceV2() throws Exception {
    ApiRequestConfig config = new ApiRequestConfig(
        authenticationManager,
        objectMapper,
        couchServer,
        accountStore,
        apiRequestStore,
        servletRequest,
        servletResponse,
        uriInfo,
        headers,
        securityContext
    );

    String accountId = config.getApiClientUser().getAccountId();
    Account account = config.getAccountStore().getByAccountId(accountId);

    String clientName = config.getApiClientUser().getClientName();
    ApiClient apiClient = account.getApiClientByName(clientName);

    return new ApiResourceV2(config, account, apiClient);
  }

  @Path("/manage")
  public ManageResource getManageResource() {
    UserRequestConfig config = new UserRequestConfig(
        authenticationManager,
        objectMapper,
        couchServer,
        accountStore,
        apiRequestStore,
        servletRequest,
        servletResponse,
        uriInfo,
        headers,
        securityContext
    );

    AccountUser accountUser = config.getCurrentUser();
    String accountId = accountUser.getAccountId();
    Account account = config.getAccountStore().getByAccountId(accountId);

    return new ManageResource(config, account);
  }

  @GET @Path("/q/{id}")
  public Response resolveCallback(@PathParam("id") String id) throws URISyntaxException {

    if (id.startsWith("api-request:")) {
      id = id.substring(12);
      return resolveCallback(id);

    } else if (id.contains(":") == false) {
      ApiRequest request = requestConfig.getApiRequestStore().getByApiRequestId(id);
      if (request == null) {
        throw ApiException.notFound("API request not found for " + id);
      }

      Account account = requestConfig.getAccountStore().getByClientId(request.getApiClientId());
      if (account == null) {
        throw ApiException.notFound("Account not found given client id " + request.getApiClientId());
      }

      ApiClient apiClient = account.getApiClientById(request.getApiClientId());
      if (apiClient == null) {
        throw ApiException.notFound("API client not found for " + request.getApiClientId());
      }

      if (PushType.notification.equals(request.getPushType())) {
        String path = String.format("manage/api-client/%s/notifications/%s", apiClient.getClientName(), id);
        return Response.seeOther(new URI(path)).build();

      } else if (PushType.userEvent.equals(request.getPushType())) {
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

  @GET
  @Produces("image/jpeg")
  @Path("{resource: ([^\\s]+(\\.(?i)(jpg|JPG|jpeg|JPEG))$) }")
  public Viewable renderJPGs() throws Exception {
    String path = "/" + requestConfig.getUriInfo().getPath();
    return new Viewable(path);
  }

  @GET
  @Produces("image/png")
  @Path("{resource: ([^\\s]+(\\.(?i)(png|PNG))$) }")
  public Viewable renderPNGs() throws Exception {
    String path = "/" + requestConfig.getUriInfo().getPath();
    return new Viewable(path);
  }

  @GET
  @Produces("image/gif")
  @Path("{resource: ([^\\s]+(\\.(?i)(gif|GIF))$) }")
  public Viewable renderGIFs() throws Exception {
    String path = "/" + requestConfig.getUriInfo().getPath();
    return new Viewable(path);
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("{resource: ([^\\s]+(\\.(?i)(txt|TXT|text|TEXT))$) }")
  public Viewable renderText() throws Exception {
    String path = "/" + requestConfig.getUriInfo().getPath();
    return new Viewable(path);
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("{resource: ([^\\s]+(\\.(?i)(html|HTML))$) }")
  public Viewable renderHtml() throws Exception {
    String path = "/" + requestConfig.getUriInfo().getPath();
    return new Viewable(path);
  }

  @GET
  @Produces("text/css")
  @Path("{resource: ([^\\s]+(\\.(?i)(css|CSS))$) }")
  public Viewable renderCSS() throws Exception {
    String path = "/" + requestConfig.getUriInfo().getPath();
    return new Viewable(path);
  }

  @GET
  @Produces("application/javascript")
  @Path("{resource: ([^\\s]+(\\.(?i)(js|JS))$) }")
  public Viewable renderJavaScript() throws Exception {
    String path = "/" + requestConfig.getUriInfo().getPath();
    return new Viewable(path);
  }

  @GET
  @Produces("image/icon")
  @Path("{resource: ([^\\s]+(\\.(?i)(ico|ICO))$) }")
  public Viewable renderICOs() throws Exception {
    String path = "/" + requestConfig.getUriInfo().getPath();
    return new Viewable(path);
  }

  @GET
  @Produces("application/pdf")
  @Path("{resource: ([^\\s]+(\\.(?i)(pdf|PDF))$) }")
  public Viewable renderPDFs() throws Exception {
    String path = "/" + requestConfig.getUriInfo().getPath();
    return new Viewable(path);
  }


  @GET @Path("/privacy-policy")
  @Produces(MediaType.TEXT_HTML)
  public Viewable privacyPolicy() {
    return new Viewable("/mun-mon/general/privacy-policy.jsp");
  }

  @GET @Path("/terms-of-service")
  @Produces(MediaType.TEXT_HTML)
  public Viewable termsOfService() {
    return new Viewable("/mun-mon/general/terms-of-service.jsp");
  }


  @GET @Path("/trafficbasedsspsitemap.xml")
  public Response trafficbasedsspsitemap_xml() { return Response.status(404).build(); }

  @GET @Path("/apple-touch-icon-precomposed.png")
  public Response apple_touch_icon_precomposed_png() { return Response.status(404).build(); }

  @GET @Path("/apple-touch-icon.png")
  public Response apple_touch_icon_png() { return Response.status(404).build(); }

  @GET @Path("/manager/status")
  public Response managerStatus() throws Exception { return Response.status(404).build(); }

  @GET @Path("{resource: ([^\\s]+(\\.(?i)(php|PHP))$) }")
  public Response renderTXTs() throws Exception { return Response.status(404).build(); }


  @GET // TODO - implement the faq.jsp page.
  @Path("{resource: (faq\\.).* }")
  @Produces(MediaType.TEXT_HTML)
  public Viewable getFaq() {
    return new Viewable("/mun-mon/faq.jsp");
  }

  @GET // TODO - implement the contact.jsp page.
  @Path("{resource: (contact\\.).* }")
  @Produces(MediaType.TEXT_HTML)
  public Viewable getContact() {
    return new Viewable("/mun-mon/contact.jsp");
  }
}

