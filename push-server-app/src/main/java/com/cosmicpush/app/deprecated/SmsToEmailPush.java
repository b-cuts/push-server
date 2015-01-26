package com.cosmicpush.app.deprecated;

import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.push.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class SmsToEmailPush extends SmtpEmailPush {

  public static final PushType PUSH_TYPE = new PushType(SmsToEmailPush.class, "emailToSms", "Deprecated SMS Push");

  public SmsToEmailPush(String toAddress, String fromAddress, String emailSubject, String htmlContent, String callbackUrl, String... traits) {
    super(toAddress, fromAddress, emailSubject, htmlContent, callbackUrl, traits);
  }

  public SmsToEmailPush(@JsonProperty("toAddress") String toAddress,
                        @JsonProperty("fromAddress") String fromAddress,
                        @JsonProperty("message") String message,
                        @JsonProperty("callbackUrl") String callbackUrl,
                        @JsonProperty("traits") Map<String, String> traits) {
    super(toAddress, fromAddress, message, null, callbackUrl, traits);
  }

  @Override
  public PushType getPushType() {
    return SmsToEmailPush.PUSH_TYPE;
  }
}
