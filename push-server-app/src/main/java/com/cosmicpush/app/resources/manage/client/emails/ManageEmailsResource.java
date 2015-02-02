/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.app.resources.manage.client.emails;

import com.cosmicpush.app.jaxrs.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.MngtAuthentication;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.app.view.Thymeleaf;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.plugins.Plugin;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.common.system.PluginManager;
import com.cosmicpush.pub.push.*;
import java.net.URI;
import java.util.*;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@MngtAuthentication
public class ManageEmailsResource {

  private final Account account;
  private final ApiClient apiClient;
  private final ExecutionContext context = CpApplication.getExecutionContext();

  public ManageEmailsResource(Account account, ApiClient apiClient) {
    this.account = account;
    this.apiClient = apiClient;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf viewEmailEvents() throws Exception {
    List<ApiRequest> requests = new ArrayList<>();
    requests.addAll(context.getApiRequestStore().getByClientAndType(apiClient, SesEmailPush.PUSH_TYPE));
    requests.addAll(context.getApiRequestStore().getByClientAndType(apiClient, SmtpEmailPush.PUSH_TYPE));

    EmailsModel model = new EmailsModel(account, apiClient, requests);
    return new Thymeleaf("/manage/api-emails.html", model);
  }

  @GET
  @Path("/{apiRequestId}")
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf viewEmailEvent(@PathParam("apiRequestId") String apiRequestId) throws Exception {

    ApiRequest apiRequest = context.getApiRequestStore().getByApiRequestId(apiRequestId);
    EmailPush email = apiRequest.getEmailPush();

    EmailModel model = new EmailModel(account, apiClient, apiRequest, email);
    return new Thymeleaf("/manage/api-email.html", model);
  }

  @POST
  @Path("/{apiRequestId}/retry")
  public Response retryEmailMessage(@Context ServletContext servletContext, @PathParam("apiRequestId") String apiRequestId) throws Exception {

    ApiRequest apiRequest = context.getApiRequestStore().getByApiRequestId(apiRequestId);
    EmailPush push = (EmailPush)apiRequest.getPush();

    if (SesEmailPush.PUSH_TYPE.equals(push.getPushType())) {
      Plugin plugin = PluginManager.getPlugin(push.getPushType());
      plugin.newDelegate(context, account, apiClient, apiRequest, push).retry();

    } else if (SmtpEmailPush.PUSH_TYPE.equals(push.getPushType())) {
      Plugin plugin = PluginManager.getPlugin(push.getPushType());
      plugin.newDelegate(context, account, apiClient, apiRequest, push).retry();

    } else {
      String msg = String.format("The retry operation is not supported for the push type \"%s\".", push.getPushType().getCode());
      throw new UnsupportedOperationException(msg);
    }

    String path = String.format("%s/manage/api-client/%s/emails/%s", servletContext.getContextPath(), apiClient.getClientName(), apiRequest.getApiRequestId());
    return Response.seeOther(new URI(path)).build();
  }
}
