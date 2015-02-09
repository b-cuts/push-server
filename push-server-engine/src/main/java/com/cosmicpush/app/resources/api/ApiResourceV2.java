/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.app.resources.api;

import com.cosmicpush.app.deprecated.NotificationPushV1;
import com.cosmicpush.app.jaxrs.security.ApiAuthentication;
import com.cosmicpush.app.resources.api.deprecated.LqNotificationDelegate;
import com.cosmicpush.app.resources.api.deprecated.NotificationDelegateV1;
import com.cosmicpush.app.resources.api.deprecated.UserEventDelegate;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.common.system.AppContext;
import com.cosmicpush.common.system.ExecutionContext;
import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.common.PushResponse;
import com.cosmicpush.pub.push.LqNotificationPush;
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
      PushRequest pushRequest = new PushRequest(apiVersion, domain, push);
      context.getPushRequestStore().create(pushRequest);

      new UserEventDelegate(context, account, domain, pushRequest, userEventPush).start();
      return buildResponse(pushRequest, account, domain);

    } else if (push instanceof NotificationPushV1) {
      NotificationPushV1 notificationPushV1 = (NotificationPushV1)push;
      PushRequest pushRequest = new PushRequest(apiVersion, domain, push);
      context.getPushRequestStore().create(pushRequest);

      new NotificationDelegateV1(context, account, domain, pushRequest, notificationPushV1).start();
      return buildResponse(pushRequest, account, domain);

    } else if (push instanceof LqNotificationPush) {
      LqNotificationPush lqNotificationPush = (LqNotificationPush)push;
      PushRequest pushRequest = new PushRequest(apiVersion, domain, push);
      context.getPushRequestStore().create(pushRequest);

      new LqNotificationDelegate(context, account, domain, pushRequest, lqNotificationPush).start();
      return buildResponse(pushRequest, account, domain);
    }

    PushResponse response = context.getPushProcessor().execute(apiVersion, account, domain, push);
    return Response.ok(response, MediaType.APPLICATION_JSON).build();
  }

  private Response buildResponse(PushRequest pushRequest, Account account, Domain domain) throws Exception {
    PushResponse response = new PushResponse(
      account.getAccountId(),
      domain.getDomainId(),
      pushRequest.getPushRequestId(),
      pushRequest.getCreatedAt(),
      pushRequest.getRequestStatus(),
      pushRequest.getNotes()
    );
    return Response.ok(response, MediaType.APPLICATION_JSON).build();
  }
}
