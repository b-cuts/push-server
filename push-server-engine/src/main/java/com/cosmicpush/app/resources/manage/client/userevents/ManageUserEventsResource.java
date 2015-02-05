/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.app.resources.manage.client.userevents;

import com.cosmicpush.app.jaxrs.ExecutionContext;
import com.cosmicpush.app.jaxrs.security.MngtAuthentication;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.app.view.Thymeleaf;
import com.cosmicpush.app.view.ThymeleafViewFactory;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.push.UserEventPush;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;

@MngtAuthentication
public class ManageUserEventsResource {

  private final Account account;
  private final ApiClient apiClient;
  private final ExecutionContext context = CpApplication.getExecutionContext();

  public ManageUserEventsResource(Account account, ApiClient apiClient) {
    this.account = account;
    this.apiClient = apiClient;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf viewUserEvents() throws Exception {

    List<ApiRequest> requests = new ArrayList<>();

    List<ApiRequest> apiRequests = context.getApiRequestStore().getByClientAndType(apiClient, UserEventPush.PUSH_TYPE);
    for (ApiRequest request : apiRequests) {
      if (request.getUserEventPush().isSendStory() == false) {
        requests.add(request);
      }
    }

    List<UserEventGroup> groups = toGroups(requests);

    UserEventGroupsModel model = new UserEventGroupsModel(account, apiClient, groups);
    return new Thymeleaf(ThymeleafViewFactory.MANAGE_API_EVENTS, model);
  }

  @GET
  @Path("/{deviceId}")
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf viewSession(@PathParam("deviceId") String deviceId) throws Exception {

    List<ApiRequest> requests = context.getApiRequestStore().getByClientAndDevice(apiClient, deviceId);
    List<UserEventGroup> groups = toGroups(requests);
    List<UserEventSession> sessions = groups.get(0).getSessions();

    UserEventSessionsModel model = new UserEventSessionsModel(account, apiClient, deviceId, sessions);
    return new Thymeleaf(ThymeleafViewFactory.MANAGE_API_EVENT, model);
  }

  public static List<UserEventGroup> toGroups(List<ApiRequest> requests) {
    Map<String,UserEventGroup> sessionsMap = new HashMap<String,UserEventGroup>();
    for (ApiRequest request : requests) {
      UserEventPush userEvent = request.getUserEventPush();
      String deviceId = userEvent.getDeviceId();

      if (sessionsMap.containsKey(deviceId) == false) {
        UserEventGroup group = new UserEventGroup(userEvent);
        sessionsMap.put(deviceId, group);
      } else {
        System.out.printf("");
      }
      sessionsMap.get(deviceId).add(request);
    }

    List<UserEventGroup> sessions = new ArrayList<UserEventGroup>(sessionsMap.values());
    Collections.sort(sessions);
    Collections.reverse(sessions);

    return sessions;
  }
}
