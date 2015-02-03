/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client;

import com.cosmicpush.app.jaxrs.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.MngtAuthentication;
import com.cosmicpush.app.resources.manage.client.emails.ManageEmailsResource;
import com.cosmicpush.app.resources.manage.client.notifications.ManageNotificationsResource;
import com.cosmicpush.app.resources.manage.client.userevents.ManageUserEventsResource;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.app.view.Thymeleaf;
import com.cosmicpush.app.view.ThymeleafViewFactory;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.actions.UpdateClientAction;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.*;
import com.cosmicpush.pub.common.PushType;
import java.net.URI;
import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.crazyyak.dev.common.exceptions.*;

@MngtAuthentication
public class ManageApiClientResource {

  private final ExecutionContext context = CpApplication.getExecutionContext();
  private final Account account = context.getAccount();
  private final ApiClient apiClient;

  public ManageApiClientResource(Account account, String clientName) {
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
  public Thymeleaf viewApiClient() throws Exception {
    String lastMessage = apiClient.getLastMessage();
    apiClient.setLastMessage(null);
    context.getAccountStore().update(account);

    ManageApiClientModel model = new ManageApiClientModel(context, account, apiClient, lastMessage);
    return new Thymeleaf(ThymeleafViewFactory.MANAGE_API_CLIENT, model);
  }

  @GET
  @Path("/requests")
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf viewEvents() throws Exception {

    QueryResult<ApiRequest> queryResult = context.getApiRequestStore().getByClient(apiClient, 500);
    List<ApiRequest> requests = new ArrayList<ApiRequest>(queryResult.getEntityList());

    Collections.sort(requests);
    Collections.reverse(requests);

    ApiClientRequestsModel model = new ApiClientRequestsModel(account, apiClient, requests);
    return new Thymeleaf(ThymeleafViewFactory.MANAGE_API_REQUESTS, model);
  }

  @Path("/emails")
  public ManageEmailsResource getManageEmailsResource() throws Exception {
    return new ManageEmailsResource(account, apiClient);
  }

  @Path("/user-events")
  public ManageUserEventsResource getManageUserEventsResource() throws Exception {
    return new ManageUserEventsResource(account, apiClient);
  }

  @Path("/notifications")
  public ManageNotificationsResource getManageNotificationsResource() throws Exception {
    return new ManageNotificationsResource(account, apiClient);
  }

  @POST
  @Path("/client")
  public Response updateClient(@FormParam("clientName") String clientName, @FormParam("clientPassword") String clientPassword) throws Exception {
    if (apiClient.getClientName().equals(clientName) == false &&
      context.getAccountStore().getByClientName(clientName) != null) {
      // The specified name is not the same as the current
      // value but it is already in use by another account.
      throw ApiException.badRequest(String.format("The client name %s already exists.", clientName));
    }

    UpdateClientAction action = new UpdateClientAction(clientName, clientPassword);

    apiClient.apply(action);
    context.getAccountStore().update(account);

    return redirect();
  }

  @POST
  @Path("/client/delete")
  public Response deleteClient() throws Exception {

    account.remove(apiClient);
    context.getAccountStore().update(account);

    return Response.seeOther(new URI("manage/account")).build();
  }

  @Path("/{pushType}")
  public ManagePluginApi getManagePluginApi(@PathParam("pushType") PushType pushType) throws Exception {
    return new ManagePluginApi(account, apiClient, pushType);
  }
}
