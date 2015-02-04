/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client;

import com.cosmicpush.app.resources.manage.UserRequestConfig;
import com.cosmicpush.app.resources.manage.client.emails.ManageEmailsResource;
import com.cosmicpush.app.resources.manage.client.notifications.ManageNotificationsResource;
import com.cosmicpush.app.resources.manage.client.ocsmessage.ManageOcsMessageResource;
import com.cosmicpush.app.resources.manage.client.userevents.ManageUserEventsResource;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.actions.UpdateClientAction;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.*;
import com.cosmicpush.pub.common.PushType;
import java.net.URI;
import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.crazyyak.dev.common.exceptions.*;
import org.glassfish.jersey.server.mvc.Viewable;

public class ManageApiClientResource {

  private final Account account;
  private final ApiClient apiClient;
  private final UserRequestConfig config;

  public ManageApiClientResource(UserRequestConfig config, Account account, String clientName) {
    this.config = ExceptionUtils.assertNotNull(config, "config");
    this.account = ExceptionUtils.assertNotNull(account, "account");
    apiClient = account.getApiClientByName(clientName);

    if (apiClient == null) {
      throw ApiException.notFound();
    }
  }

  public Response redirect() throws Exception {
    String path = String.format("manage/api-client/%s", apiClient.getClientName());
    return Response.seeOther(new URI(path)).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Viewable viewApiClient() throws Exception {
    String lastMessage = apiClient.getLastMessage();
    apiClient.setLastMessage(null);
    config.getAccountStore().update(account);

    ManageApiClientModel model = new ManageApiClientModel(config, account, apiClient, lastMessage);
    return new Viewable("/manage/api-client.jsp", model);
  }

  @GET
  @Path("/requests")
  @Produces(MediaType.TEXT_HTML)
  public Viewable viewEvents() throws Exception {

    QueryResult<ApiRequest> queryResult = config.getApiRequestStore().getByClient(apiClient, 500);
    List<ApiRequest> requests = new ArrayList<ApiRequest>(queryResult.getEntityList());

    Collections.sort(requests);
    Collections.reverse(requests);

    ApiClientRequestsModel model = new ApiClientRequestsModel(account, apiClient, requests);
    return new Viewable("/manage/api-requests.jsp", model);
  }

  @Path("/emails")
  public ManageEmailsResource getManageEmailsResource() throws Exception {
    return new ManageEmailsResource(config, account, apiClient);
  }

  @Path("/user-events")
  public ManageUserEventsResource getManageUserEventsResource() throws Exception {
    return new ManageUserEventsResource(config, account, apiClient);
  }

  @Path("/notifications")
  public ManageNotificationsResource getManageNotificationsResource() throws Exception {
    return new ManageNotificationsResource(config, account, apiClient);
  }

  @Path("/ocs-message")
  public ManageOcsMessageResource getManageOcsMessagesResource() throws Exception {
    return new ManageOcsMessageResource(config, account, apiClient);
  }

  @POST
  @Path("/client")
  public Response updateClient(@FormParam("clientName") String clientName, @FormParam("clientPassword") String clientPassword) throws Exception {
    if (apiClient.getClientName().equals(clientName) == false &&
        config.getAccountStore().getByClientName(clientName) != null) {
      // The specified name is not the same as the current
      // value but it is already in use by another account.
      throw ApiException.badRequest(String.format("The client name %s already exists.", clientName));
    }

    UpdateClientAction action = new UpdateClientAction(clientName, clientPassword);

    apiClient.apply(action);
    config.getAccountStore().update(account);

    return redirect();
  }

  @POST
  @Path("/client/delete")
  public Response deleteClient() throws Exception {

    account.remove(apiClient);
    config.getAccountStore().update(account);

    return Response.seeOther(new URI("manage/account")).build();
  }

  @Path("/{pushType}")
  public ManagePluginApi getManagePluginApi(@PathParam("pushType") PushType pushType) throws Exception {
    return new ManagePluginApi(config, account, apiClient, pushType);
  }
}
