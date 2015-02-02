/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.app.resources.api;

import com.cosmicpush.app.jaxrs.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.ApiAuthentication;
import com.cosmicpush.app.resources.api.userevent.UserEventDelegate;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.common.*;
import com.cosmicpush.pub.push.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@ApiAuthentication
public class ApiResourceV2 {

  private final ExecutionContext context = CpApplication.getExecutionContext();

  public ApiResourceV2() throws Exception {
  }

  private Account getAccount() {
    return CpApplication.getExecutionContext().getAccount();
  }

  private ApiClient getApiClient() {
    return CpApplication.getExecutionContext().getApiClient();
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

    SecurityContext securityContext = context.getSecurityContext();

    if (push instanceof UserEventPush) {
      UserEventPush userEventPush = (UserEventPush)push;
      ApiRequest apiRequest = new ApiRequest(getApiClient(), push, context.getRemoteAddress());
      context.getApiRequestStore().create(apiRequest);

      new UserEventDelegate(context, getAccount(), getApiClient(), apiRequest, userEventPush).start();
      return buildResponse(apiRequest);

    } else if (push instanceof NotificationPush) {
      NotificationPush notificationPush = (NotificationPush)push;
      ApiRequest apiRequest = new ApiRequest(getApiClient(), push, context.getRemoteAddress());
      context.getApiRequestStore().create(apiRequest);

      new NotificationDelegate(context, getAccount(), getApiClient(), apiRequest, notificationPush).start();
      return buildResponse(apiRequest);
    }

    PushResponse response = context.getPushProcessor().execute(getAccount(), getApiClient(), push);
    return Response.ok(response, MediaType.APPLICATION_JSON).build();
  }

  private Response buildResponse(ApiRequest apiRequest) throws Exception {
    PushResponse response = new PushResponse(
      getAccount().getAccountId(),
      getApiClient().getApiClientId(),
      apiRequest.getApiRequestId(),
      apiRequest.getCreatedAt(),
      apiRequest.getRequestStatus(),
      apiRequest.getNotes()
    );
    return Response.ok(response, MediaType.APPLICATION_JSON).build();
  }
}
