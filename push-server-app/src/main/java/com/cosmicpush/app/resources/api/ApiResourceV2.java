/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.app.resources.api;

import com.cosmicpush.app.jaxrs.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.ApiAuthentication;
import com.cosmicpush.app.resources.api.deprecated.NotificationDelegate;
import com.cosmicpush.pub.push.NotificationPush;
import com.cosmicpush.app.resources.api.deprecated.UserEventDelegate;
import com.cosmicpush.pub.push.UserEventPush;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.common.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@ApiAuthentication
public class ApiResourceV2 {

  private final ExecutionContext context = CpApplication.getExecutionContext();

  public ApiResourceV2() throws Exception {
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/callback")
  public Response callback(String msg) throws Exception {
    return Response.ok().build();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/pushes")
  public Response postPush(Push push) throws Exception {

    Account account = context.getAccount();
    ApiClient apiClient = context.getApiClient();

    if (push instanceof UserEventPush) {
      UserEventPush userEventPush = (UserEventPush)push;
      ApiRequest apiRequest = new ApiRequest(apiClient, push);
      context.getApiRequestStore().create(apiRequest);

      new UserEventDelegate(context, account, apiClient, apiRequest, userEventPush).start();
      return buildResponse(apiRequest, account, apiClient);

    } else if (push instanceof NotificationPush) {
      NotificationPush notificationPush = (NotificationPush)push;
      ApiRequest apiRequest = new ApiRequest(apiClient, push);
      context.getApiRequestStore().create(apiRequest);

      new NotificationDelegate(context, account, apiClient, apiRequest, notificationPush).start();
      return buildResponse(apiRequest, account, apiClient);
    }

    PushResponse response = context.getPushProcessor().execute(account, apiClient, push);
    return Response.ok(response, MediaType.APPLICATION_JSON).build();
  }

  private Response buildResponse(ApiRequest apiRequest, Account account, ApiClient apiClient) throws Exception {
    PushResponse response = new PushResponse(
      account.getAccountId(),
      apiClient.getApiClientId(),
      apiRequest.getApiRequestId(),
      apiRequest.getCreatedAt(),
      apiRequest.getRequestStatus(),
      apiRequest.getNotes()
    );
    return Response.ok(response, MediaType.APPLICATION_JSON).build();
  }
}
