/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.api;

import com.cosmicpush.app.deprecated.SmsToEmailPush;
import com.cosmicpush.app.jaxrs.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.ApiAuthentication;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.pub.common.PushResponse;
import com.cosmicpush.pub.push.GoogleTalkPush;
import com.cosmicpush.pub.push.NotificationPush;
import com.cosmicpush.pub.push.SmtpEmailPush;
import com.cosmicpush.pub.push.UserEventPush;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.UnknownHostException;

@ApiAuthentication
public class ApiResourceV1 {

  private final ExecutionContext context = CpApplication.getExecutionContext();

  public ApiResourceV1() throws Exception {
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
  @Path("/pushes/notification")
  public Response sendNotification(NotificationPush push) throws Exception {
    return new ApiResourceV2().postPush(push);
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/pushes/user-event")
  public Response sendUserEvent(UserEventPush push) throws Exception {
    return new ApiResourceV2().postPush(push);
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/pushes/im")
  public Response sendIm(GoogleTalkPush push) {
    PushResponse response = context.getPushProcessor().execute(getAccount(), getApiClient(), push);
    return Response.ok(response, MediaType.APPLICATION_JSON).build();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/pushes/email")
  public Response sendEmail(SmtpEmailPush push) {
    PushResponse response = context.getPushProcessor().execute(getAccount(), getApiClient(), push);
    return Response.ok(response, MediaType.APPLICATION_JSON).build();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/pushes/email-to-sms")
  public Response sendSmsViaEmail(SmsToEmailPush smsPush) throws UnknownHostException {
    SmtpEmailPush push = SmtpEmailPush.newPush(
        smsPush.getToAddress(), smsPush.getFromAddress(),
        smsPush.getEmailSubject(), smsPush.getHtmlContent(),
        smsPush.getCallbackUrl(),
        smsPush.getTraits());

    PushResponse response = context.getPushProcessor().execute(getAccount(), getApiClient(), push);
    return Response.ok(response, MediaType.APPLICATION_JSON).build();
  }
}
