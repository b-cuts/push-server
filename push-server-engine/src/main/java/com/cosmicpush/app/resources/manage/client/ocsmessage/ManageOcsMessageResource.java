package com.cosmicpush.app.resources.manage.client.ocsmessage;

public class ManageOcsMessageResource {
//
//  private final Account account;
//  private final Domain domain;
//  private final UserRequestConfig config;
//
//  public ManageOcsMessageResource(UserRequestConfig config, Account account, Domain domain) {
//    this.account = account;
//    this.domain = domain;
//    this.config = config;
//  }
//
//  @GET
//  @Produces(MediaType.TEXT_HTML)
//  public Viewable viewMessages() throws Exception {
//
//    List<ApiRequest> requests = new ArrayList<>();
//    requests.addAll(config.getApiRequestStore().getByClientAndType(domain, PushType.ocs));
//
//    Collections.sort(requests);
//    Collections.reverse(requests);
//
//    DomainRequestsModel model = new DomainRequestsModel(account, domain, requests);
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
//    DomainNotificationModel model = new DomainNotificationModel(account, domain, request, notification);
//    return new Viewable("/manage/api-notification.jsp", model);
//  }
}
