/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.app.resources.manage.client.userevents;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.app.resources.manage.UserRequestConfig;
import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.push.UserEventPush;
import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Viewable;

public class ManageUserEventsResource {

  private final Account account;
  private final ApiClient apiClient;
  private final UserRequestConfig config;

  public ManageUserEventsResource(UserRequestConfig config, Account account, ApiClient apiClient) {
    this.account = account;
    this.apiClient = apiClient;
    this.config = config;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Viewable viewUserEvents() throws Exception {

    List<ApiRequest> requests = new ArrayList<>();

    List<ApiRequest> apiRequests = config.getApiRequestStore().getByClientAndType(apiClient, PushType.userEvent);
    for (ApiRequest request : apiRequests) {
      if (request.getUserEventPush().isSendStory() == false) {
        requests.add(request);
      }
    }

    List<UserEventGroup> groups = toGroups(requests);

    UserEventGroupsModel model = new UserEventGroupsModel(account, apiClient, groups);
    return new Viewable("/manage/api-user-events.jsp", model);
  }

  @GET
  @Path("/{deviceId}")
  @Produces(MediaType.TEXT_HTML)
  public Viewable viewSession(@PathParam("deviceId") String deviceId) throws Exception {

    List<ApiRequest> requests = config.getApiRequestStore().getByClientAndDevice(apiClient, deviceId);
    List<UserEventGroup> groups = toGroups(requests);
    List<UserEventSession> sessions = groups.get(0).getSessions();

    UserEventSessionsModel model = new UserEventSessionsModel(account, apiClient, deviceId, sessions);
    return new Viewable("/manage/api-user-event.jsp", model);
  }

  private List<UserEventGroup> toGroups(List<ApiRequest> requests) {
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
