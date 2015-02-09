/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client.notifications;

import com.cosmicpush.common.system.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.MngtAuthentication;
import com.cosmicpush.app.resources.manage.client.DomainRequestsModel;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.app.view.Thymeleaf;
import com.cosmicpush.app.view.ThymeleafViewFactory;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.pub.push.LqNotificationPush;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@MngtAuthentication
public class ManageNotificationsResource {

  private final Account account;
  private final Domain domain;
  private final ExecutionContext context = CpApplication.getExecutionContext();

  public ManageNotificationsResource(Account account, Domain domain) {
    this.account = account;
    this.domain = domain;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf viewNotifications() throws Exception {

    List<PushRequest> requests = new ArrayList<>();
    requests.addAll(context.getPushRequestStore().getByClientAndType(domain, LqNotificationPush.PUSH_TYPE));

    Collections.sort(requests);
    Collections.reverse(requests);

    DomainRequestsModel model = new DomainRequestsModel(account, domain, requests);
    return new Thymeleaf(ThymeleafViewFactory.MANAGE_API_NOTIFICATIONS, model);
  }

  @GET
  @Path("/{pushRequestId}")
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf viewNotifications(@PathParam("pushRequestId") String pushRequestId) throws Exception {

    PushRequest request = context.getPushRequestStore().getByPushRequestId(pushRequestId);
    LqNotificationPush notification = request.getNotificationPush();

    DomainNotificationModel model = new DomainNotificationModel(account, domain, request, notification);
    return new Thymeleaf(ThymeleafViewFactory.MANAGE_API_NOTIFICATION, model);
  }
}
