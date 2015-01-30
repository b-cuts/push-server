/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.api;

import com.cosmicpush.app.deprecated.SmsToEmailPush;
import com.cosmicpush.app.security.ApiAuthentication;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.pub.common.PushResponse;
import com.cosmicpush.pub.push.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@ApiAuthentication
public class ApiResourceV1 {

  private final Account account;
  private final ApiClient apiClient;
  private final ApiRequestConfig config;

  public ApiResourceV1(ApiRequestConfig config) throws Exception {
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
  @Path("/pushes/notification")
  public Response sendNotification(NotificationPush push) throws Exception {
    return new ApiResourceV2(config).postPush(push);
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/pushes/user-event")
  public Response sendUserEvent(UserEventPush push) throws Exception {
    return new ApiResourceV2(config).postPush(push);
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/pushes/im")
  public Response sendIm(GoogleTalkPush push) {
    PushResponse response = config.getPushProcessor().execute(account, apiClient, push);
    return Response.ok(response, MediaType.APPLICATION_JSON).build();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/pushes/email")
  public Response sendEmail(SmtpEmailPush push) {
    PushResponse response = config.getPushProcessor().execute(account, apiClient, push);
    return Response.ok(response, MediaType.APPLICATION_JSON).build();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/pushes/email-to-sms")
  public Response sendSmsViaEmail(SmsToEmailPush smsPush) {
    SmtpEmailPush push = new SmtpEmailPush(
        smsPush.getToAddress(), smsPush.getFromAddress(),
        smsPush.getEmailSubject(), smsPush.getHtmlContent(),
        smsPush.getCallbackUrl(), smsPush.getTraits());

    PushResponse response = config.getPushProcessor().execute(account, apiClient, push);
    return Response.ok(response, MediaType.APPLICATION_JSON).build();
  }
}
