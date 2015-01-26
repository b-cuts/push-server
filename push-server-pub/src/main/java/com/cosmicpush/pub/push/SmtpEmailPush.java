package com.cosmicpush.pub.push;

import com.cosmicpush.pub.common.PushType;
import com.fasterxml.jackson.annotation.*;
import java.util.Map;

public class SmtpEmailPush extends EmailPush {

  public static final PushType PUSH_TYPE = new PushType(SmtpEmailPush.class, "smtp-email", "SMTP E-Mail");

  public SmtpEmailPush(String toAddress, String fromAddress, String emailSubject, String htmlContent, String callbackUrl, String... traits) {
    super(toAddress, fromAddress, emailSubject, htmlContent, callbackUrl, traits);
  }

  public SmtpEmailPush(@JsonProperty("toAddress") String toAddress,
                       @JsonProperty("fromAddress") String fromAddress,
                       @JsonProperty("emailSubject") String emailSubject,
                       @JsonProperty("htmlContent") String htmlContent,
                       @JsonProperty("callbackUrl") String callbackUrl,
                       @JsonProperty("traits") Map<String, String> traits) {
    super(toAddress, fromAddress, emailSubject, htmlContent, callbackUrl, traits);
  }

  @Override
  public PushType getPushType() {
    return PUSH_TYPE;
  }
}
