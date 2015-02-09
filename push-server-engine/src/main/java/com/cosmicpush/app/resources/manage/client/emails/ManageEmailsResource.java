/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.app.resources.manage.client.emails;

import com.cosmicpush.common.system.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.MngtAuthentication;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.app.view.Thymeleaf;
import com.cosmicpush.app.view.ThymeleafViewFactory;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.Plugin;
import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.common.system.PluginManager;
import com.cosmicpush.pub.push.EmailPush;
import com.cosmicpush.pub.push.SesEmailPush;
import com.cosmicpush.pub.push.SmtpEmailPush;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@MngtAuthentication
public class ManageEmailsResource {

  private final Account account;
  private final Domain domain;
  private final ExecutionContext context = CpApplication.getExecutionContext();

  public ManageEmailsResource(Account account, Domain domain) {
    this.account = account;
    this.domain = domain;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf viewEmailEvents() throws Exception {
    List<PushRequest> requests = new ArrayList<>();
    requests.addAll(context.getPushRequestStore().getByClientAndType(domain, SesEmailPush.PUSH_TYPE));
    requests.addAll(context.getPushRequestStore().getByClientAndType(domain, SmtpEmailPush.PUSH_TYPE));

    EmailsModel model = new EmailsModel(account, domain, requests);
    return new Thymeleaf(ThymeleafViewFactory.MANAGE_API_EMAILS, model);
  }

  @GET
  @Path("/{pushRequestId}")
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf viewEmailEvent(@PathParam("pushRequestId") String pushRequestId) throws Exception {

    PushRequest pushRequest = context.getPushRequestStore().getByPushRequestId(pushRequestId);
    EmailPush email = pushRequest.getEmailPush();

    EmailModel model = new EmailModel(account, domain, pushRequest, email);
    return new Thymeleaf(ThymeleafViewFactory.MANAGE_API_EMAIL, model);
  }

  @POST
  @Path("/{pushRequestId}/retry")
  public Response retryEmailMessage(@Context ServletContext servletContext, @PathParam("pushRequestId") String pushRequestId) throws Exception {

    PushRequest pushRequest = context.getPushRequestStore().getByPushRequestId(pushRequestId);
    EmailPush push = (EmailPush)pushRequest.getPush();

    if (SesEmailPush.PUSH_TYPE.equals(push.getPushType())) {
      Plugin plugin = PluginManager.getPlugin(push.getPushType());
      plugin.newDelegate(context, account, domain, pushRequest, push).retry();

    } else if (SmtpEmailPush.PUSH_TYPE.equals(push.getPushType())) {
      Plugin plugin = PluginManager.getPlugin(push.getPushType());
      plugin.newDelegate(context, account, domain, pushRequest, push).retry();

    } else {
      String msg = String.format("The retry operation is not supported for the push type \"%s\".", push.getPushType().getCode());
      throw new UnsupportedOperationException(msg);
    }

    String path = String.format("%s/manage/domain/%s/emails/%s", servletContext.getContextPath(), domain.getDomainKey(), pushRequest.getPushRequestId());
    return Response.seeOther(new URI(path)).build();
  }
}
