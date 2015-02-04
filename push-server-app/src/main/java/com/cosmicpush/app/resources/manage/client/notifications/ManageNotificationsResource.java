/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client.notifications;

import com.cosmicpush.app.jaxrs.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.MngtAuthentication;
import com.cosmicpush.app.resources.manage.client.ApiClientRequestsModel;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.app.view.Thymeleaf;
import com.cosmicpush.app.view.ThymeleafViewFactory;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.push.NotificationPush;
import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@MngtAuthentication
public class ManageNotificationsResource {

  private final Account account;
  private final ApiClient apiClient;
  private final ExecutionContext context = CpApplication.getExecutionContext();

  public ManageNotificationsResource(Account account, ApiClient apiClient) {
    this.account = account;
    this.apiClient = apiClient;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf viewNotifications() throws Exception {

    List<ApiRequest> requests = new ArrayList<>();
    requests.addAll(context.getApiRequestStore().getByClientAndType(apiClient, PushType.notification));

    Collections.sort(requests);
    Collections.reverse(requests);

    ApiClientRequestsModel model = new ApiClientRequestsModel(account, apiClient, requests);
    return new Thymeleaf(ThymeleafViewFactory.MANAGE_API_NOTIFICATIONS, model);
  }

  @GET
  @Path("/{apiRequestId}")
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf viewNotifications(@PathParam("apiRequestId") String apiRequestId) throws Exception {

    ApiRequest request = context.getApiRequestStore().getByApiRequestId(apiRequestId);
    NotificationPush notification = request.getNotificationPush();

    ApiClientNotificationModel model = new ApiClientNotificationModel(account, apiClient, request, notification);
    return new Thymeleaf(ThymeleafViewFactory.MANAGE_API_NOTIFICATION, model);
  }
}
