package com.cosmicpush.app.view;

import java.net.URL;

public class ThymeleafViewFactory {
  public static final String TAIL = ".html";
  public static final String ROOT = "/push-server-app/view/";

  public static final String WELCOME = validate("welcome");

  public static final String MANAGE_ACCOUNT =           validate("manage/account");
  public static final String MANAGE_API_CLIENT =        validate("manage/api-client");

  public static final String MANAGE_API_REQUESTS =      validate("manage/api-requests");

  public static final String MANAGE_API_EMAIL =         validate("manage/api-email");
  public static final String MANAGE_API_EMAILS =        validate("manage/api-emails");

  public static final String MANAGE_API_TWILIO =  validate("manage/api-twilio");
  public static final String MANAGE_API_TWILIOS = validate("manage/api-twilios");

  public static final String MANAGE_API_NOTIFICATION =  validate("manage/api-notification");
  public static final String MANAGE_API_NOTIFICATIONS = validate("manage/api-notifications");

  public static final String MANAGE_API_EVENT =        validate("manage/api-user-event");
  public static final String MANAGE_API_EVENTS =        validate("manage/api-user-events");

  private static String validate(String view) {
    String resource = ROOT+view+TAIL;
    URL url = ThymeleafViewFactory.class.getResource(resource);
    if (url == null) {
      String msg = String.format("The resource \"%s\" does not exist.", resource);
      throw new IllegalArgumentException(msg);
    }
    return view;
  }
}
