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
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.common.requests.QueryResult;
import com.cosmicpush.pub.common.PushType;
import org.crazyyak.dev.common.exceptions.ApiException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@MngtAuthentication
public class ManageApiClientResource {

  private final ExecutionContext context = CpApplication.getExecutionContext();
  private final String clientName;

  public ManageApiClientResource(String clientName) {
    this.clientName = clientName;
  }

  private Account getAccount() {
    return context.getAccount();
  }

  private ApiClient getApiClient() {
    ApiClient apiClient = getAccount().getApiClientByName(clientName);
    if (apiClient == null) throw ApiException.notFound(clientName);
    return apiClient;
  }

  public Response redirect() throws Exception {
    String path = String.format("manage/api-client/%s", getApiClient().getClientName());
    return Response.seeOther(new URI(path)).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf viewApiClient() throws Exception {
    String lastMessage = getApiClient().getLastMessage();
    getApiClient().setLastMessage(null);
    context.getAccountStore().update(getAccount());

    ManageApiClientModel model = new ManageApiClientModel(context, getAccount(), getApiClient(), lastMessage);
    return new Thymeleaf(ThymeleafViewFactory.MANAGE_API_CLIENT, model);
  }

  @GET
  @Path("/requests")
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf viewEvents() throws Exception {

    QueryResult<ApiRequest> queryResult = context.getApiRequestStore().getByClient(getApiClient(), 500);
    List<ApiRequest> requests = new ArrayList<ApiRequest>(queryResult.getEntityList());

    Collections.sort(requests);
    Collections.reverse(requests);

    ApiClientRequestsModel model = new ApiClientRequestsModel(getAccount(), getApiClient(), requests);
    return new Thymeleaf(ThymeleafViewFactory.MANAGE_API_REQUESTS, model);
  }

  @Path("/emails")
  public ManageEmailsResource getManageEmailsResource() throws Exception {
    return new ManageEmailsResource(getAccount(), getApiClient());
  }

  @Path("/user-events")
  public ManageUserEventsResource getManageUserEventsResource() throws Exception {
    return new ManageUserEventsResource(getAccount(), getApiClient());
  }

  @Path("/notifications")
  public ManageNotificationsResource getManageNotificationsResource() throws Exception {
    return new ManageNotificationsResource(getAccount(), getApiClient());
  }

  @POST
  @Path("/client")
  public Response updateClient(@FormParam("clientName") String clientName, @FormParam("clientPassword") String clientPassword) throws Exception {
    if (getApiClient().getClientName().equals(clientName) == false &&
      context.getAccountStore().getByClientName(clientName) != null) {
      // The specified name is not the same as the current
      // value but it is already in use by another account.
      throw ApiException.badRequest(String.format("The client name %s already exists.", clientName));
    }

    UpdateClientAction action = new UpdateClientAction(clientName, clientPassword);

    getApiClient().apply(action);
    context.getAccountStore().update(getAccount());

    return redirect();
  }

  @POST
  @Path("/client/delete")
  public Response deleteClient() throws Exception {

    getAccount().remove(getApiClient());
    context.getAccountStore().update(getAccount());

    return Response.seeOther(new URI("manage/account")).build();
  }

  @Path("/{pushType}")
  public ManagePluginApi getManagePluginApi(@PathParam("pushType") PushType pushType) throws Exception {
    return new ManagePluginApi(getAccount(), getApiClient(), pushType);
  }
}
