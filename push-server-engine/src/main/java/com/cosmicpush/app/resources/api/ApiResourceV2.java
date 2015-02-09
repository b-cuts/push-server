/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.app.resources.api;

import com.cosmicpush.common.system.AppContext;
import com.cosmicpush.common.system.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.ApiAuthentication;
import com.cosmicpush.app.resources.api.deprecated.NotificationDelegate;
import com.cosmicpush.app.resources.api.deprecated.UserEventDelegate;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.common.PushResponse;
import com.cosmicpush.pub.push.NotificationPush;
import com.cosmicpush.pub.push.UserEventPush;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
  @Path("/pushes")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response postPushV2(Push push) throws Exception {
    return postPush(push, AppContext.CURRENT_API_VERSION);
  }

  public Response postPushV1(Push push) throws Exception {
    return postPush(push, 1);
  }

  private Response postPush(Push push, int apiVersion) throws Exception {
    Account account = context.getAccount();
    Domain domain = context.getDomain();

    if (push instanceof UserEventPush) {
      UserEventPush userEventPush = (UserEventPush)push;
      ApiRequest apiRequest = new ApiRequest(apiVersion, domain, push);
      context.getApiRequestStore().create(apiRequest);

      new UserEventDelegate(context, account, domain, apiRequest, userEventPush).start();
      return buildResponse(apiRequest, account, domain);

    } else if (push instanceof NotificationPush) {
      NotificationPush notificationPush = (NotificationPush)push;
      ApiRequest apiRequest = new ApiRequest(apiVersion, domain, push);
      context.getApiRequestStore().create(apiRequest);

      new NotificationDelegate(context, account, domain, apiRequest, notificationPush).start();
      return buildResponse(apiRequest, account, domain);
    }

    PushResponse response = context.getPushProcessor().execute(apiVersion, account, domain, push);
    return Response.ok(response, MediaType.APPLICATION_JSON).build();
  }

  private Response buildResponse(ApiRequest apiRequest, Account account, Domain domain) throws Exception {
    PushResponse response = new PushResponse(
      account.getAccountId(),
      domain.getDomainId(),
      apiRequest.getApiRequestId(),
      apiRequest.getCreatedAt(),
      apiRequest.getRequestStatus(),
      apiRequest.getNotes()
    );
    return Response.ok(response, MediaType.APPLICATION_JSON).build();
  }
}
