package com.cosmicpush.pub.common;

import java.util.Map;

public interface CommonEmail {

  public PushType getPushType();

  public String getRemoteHost();
  public String getRemoteAddress();

  public String getFromAddress();
  public String getToAddress();

  public String getEmailSubject();
  public String getHtmlContent();

  public String getCallbackUrl();
  public Map<String, String> getTraits();
}
