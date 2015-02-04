package com.cosmicpush.app.resources.manage.client.ocsmessage;

public class ManageOcsMessageResource {
//
//  private final Account account;
//  private final ApiClient apiClient;
//  private final UserRequestConfig config;
//
//  public ManageOcsMessageResource(UserRequestConfig config, Account account, ApiClient apiClient) {
//    this.account = account;
//    this.apiClient = apiClient;
//    this.config = config;
//  }
//
//  @GET
//  @Produces(MediaType.TEXT_HTML)
//  public Viewable viewMessages() throws Exception {
//
//    List<ApiRequest> requests = new ArrayList<>();
//    requests.addAll(config.getApiRequestStore().getByClientAndType(apiClient, PushType.ocs));
//
//    Collections.sort(requests);
//    Collections.reverse(requests);
//
//    ApiClientRequestsModel model = new ApiClientRequestsModel(account, apiClient, requests);
//    return new Viewable("/manage/api-notifications.jsp", model);
//  }
//
//  @GET
//  @Path("/{apiRequestId}")
//  @Produces(MediaType.TEXT_HTML)
//  public Viewable viewNotifications(@PathParam("apiRequestId") String apiRequestId) throws Exception {
//
//    ApiRequest request = config.getApiRequestStore().getByApiRequestId(apiRequestId);
//    NotificationPush notification = request.getNotificationPush();
//
//    ApiClientNotificationModel model = new ApiClientNotificationModel(account, apiClient, request, notification);
//    return new Viewable("/manage/api-notification.jsp", model);
//  }
}
