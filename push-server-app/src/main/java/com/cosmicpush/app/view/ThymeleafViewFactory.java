package com.cosmicpush.app.view;

import java.net.URL;

public class ThymeleafViewFactory {
  public static final String ROOT = "/push-server-app/view";

  public static final String WELCOME = validate("/welcome.html");

  public static final String MANAGE_API_REQUESTS =  validate("/manage/api-requests.html");
  public static final String MANAGE_API_CLIENT =    validate("/manage/api-client.html");
  public static final String MANAGE_ACCOUNT =       validate("/manage/account.html");
  public static final String MANAGE_API_EMAIL =     validate("/manage/api-email.html");
  public static final String MANAGE_API_EMAILS =    validate("/manage/api-emails.html");

  private static String validate(String view) {
    String resource = ROOT+view;
    URL url = ThymeleafViewFactory.class.getResource(resource);
    if (url == null) {
      String msg = String.format("The resource \"%s\" does not exist.", resource);
      throw new IllegalArgumentException(msg);
    }
    return view;
  }
}
