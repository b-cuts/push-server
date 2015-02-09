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
//    List<PushRequest> requests = new ArrayList<>();
//    requests.addAll(config.getPushRequestStore().getByClientAndType(domain, PushType.ocs));
//
//    Collections.sort(requests);
//    Collections.reverse(requests);
//
//    DomainRequestsModel model = new DomainRequestsModel(account, domain, requests);
//    return new Viewable("/manage/push-notifications.jsp", model);
//  }
//
//  @GET
//  @Path("/{pushRequestId}")
//  @Produces(MediaType.TEXT_HTML)
//  public Viewable viewNotifications(@PathParam("pushRequestId") String pushRequestId) throws Exception {
//
//    PushRequest request = config.getPushRequestStore().getByPushRequestId(pushRequestId);
//    NotificationPush notification = request.getNotificationPush();
//
//    DomainNotificationModel model = new DomainNotificationModel(account, domain, request, notification);
//    return new Viewable("/manage/push-notification.jsp", model);
//  }
}
