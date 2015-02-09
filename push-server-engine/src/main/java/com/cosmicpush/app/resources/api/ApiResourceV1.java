/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.app.resources.api;

import com.cosmicpush.app.deprecated.EmailToSmsPush;
import com.cosmicpush.common.system.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.ApiAuthentication;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.pub.common.PushResponse;
import com.cosmicpush.pub.common.RequestStatus;
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
import java.io.Serializable;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApiAuthentication
public class ApiResourceV1 {

  private final ExecutionContext context = CpApplication.getExecutionContext();

  public ApiResourceV1() throws Exception {
  }

  private Account getAccount() {
    return CpApplication.getExecutionContext().getAccount();
  }

  private Domain getDomain() {
    return CpApplication.getExecutionContext().getDomain();
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
    Response response = new ApiResourceV2().postPushV1(push);
    PushResponse pushResponse = (PushResponse)response.getEntity();
    return buildResponse(pushResponse);
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/pushes/user-event")
  public Response sendUserEvent(UserEventPush push) throws Exception {
    Response response = new ApiResourceV2().postPushV1(push);
    PushResponse pushResponse = (PushResponse)response.getEntity();
    return buildResponse(pushResponse);
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/pushes/im")
  public Response sendIm(GoogleTalkPush push) {
    PushResponse pushResponse = context.getPushProcessor().execute(1, getAccount(), getDomain(), push);
    return buildResponse(pushResponse);
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/pushes/email")
  public Response sendEmail(SmtpEmailPush push) {
    PushResponse pushResponse = context.getPushProcessor().execute(1, getAccount(), getDomain(), push);
    return buildResponse(pushResponse);
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/pushes/email-to-sms")
  public Response sendSmsViaEmail(EmailToSmsPush smsPush) throws UnknownHostException {
    SmtpEmailPush push = SmtpEmailPush.newPush(
        smsPush.getToAddress(), smsPush.getFromAddress(),
        smsPush.getEmailSubject(), smsPush.getHtmlContent(),
        smsPush.getCallbackUrl(),
        smsPush.getTraits());

    PushResponse pushResponse = context.getPushProcessor().execute(1, getAccount(), getDomain(), push);
    return buildResponse(pushResponse);
  }

  private Response buildResponse(PushResponse pushResponse) {
    PushResponseV1 responseV1 = new PushResponseV1(pushResponse);
    return Response.ok(responseV1, MediaType.APPLICATION_JSON).build();
  }

  public static class PushResponseV1 implements Serializable {
    private final String accountId;
    private final String apiRequestId;
    private final LocalDateTime createdAt;
    private final RequestStatus requestStatus;
    private final List<String> notes = new ArrayList<>();
    public PushResponseV1(PushResponse pushResponse) {
      this.accountId = pushResponse.getAccountId();
      this.apiRequestId = pushResponse.getApiRequestId();
      this.createdAt = pushResponse.getCreatedAt();
      this.requestStatus = pushResponse.getRequestStatus();
    }
    public String getAccountId() { return accountId; }
    public String getApiRequestId() { return apiRequestId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public RequestStatus getRequestStatus() { return requestStatus; }
    public List<String> getNotes() { return notes; }
  }
}
