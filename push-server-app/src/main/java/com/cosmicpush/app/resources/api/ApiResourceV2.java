/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.api;

import com.cosmicpush.app.resources.api.userevent.UserEventDelegate;
import com.cosmicpush.app.security.ApiAuthentication;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.common.*;
import com.cosmicpush.pub.push.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@ApiAuthentication
public class ApiResourceV2 {

  private final Account account;
  private final ApiClient apiClient;
  private final ApiRequestConfig config;

  public ApiResourceV2(ApiRequestConfig config) throws Exception {
    this.config = config;

    String accountId = config.getApiClientUser().getAccountId();
    this.account = config.getAccountStore().getByAccountId(accountId);

    String clientName = config.getApiClientUser().getClientName();
    this.apiClient = account.getApiClientByName(clientName);
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

    SecurityContext securityContext = config.getSecurityContext();
    String scheme = securityContext.getAuthenticationScheme();
    String username = securityContext.getUserPrincipal().getName();

    if (push instanceof UserEventPush) {
      UserEventPush userEventPush = (UserEventPush)push;
      ApiRequest apiRequest = new ApiRequest(apiClient, push, config.getRemoteAddress());
      config.getApiRequestStore().create(apiRequest);

      new UserEventDelegate(config, account, apiClient, apiRequest, userEventPush).start();
      return buildResponse(apiRequest);

    } else if (push instanceof NotificationPush) {
      NotificationPush notificationPush = (NotificationPush)push;
      ApiRequest apiRequest = new ApiRequest(apiClient, push, config.getRemoteAddress());
      config.getApiRequestStore().create(apiRequest);

      new NotificationDelegate(config, account, apiClient, apiRequest, notificationPush).start();
      return buildResponse(apiRequest);

    }

    PushResponse response = config.getPushProcessor().execute(account, apiClient, push);
    return Response.ok(response, MediaType.APPLICATION_JSON).build();
  }

  private Response buildResponse(ApiRequest apiRequest) throws Exception {
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
