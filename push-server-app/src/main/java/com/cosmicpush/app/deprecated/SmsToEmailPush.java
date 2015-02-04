package com.cosmicpush.app.deprecated;

import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.push.SmtpEmailPush;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.crazyyak.dev.common.BeanUtils;

import java.net.InetAddress;
import java.util.Map;

public class SmsToEmailPush extends SmtpEmailPush {

  public static final PushType PUSH_TYPE = new PushType(SmsToEmailPush.class, "emailToSms", "Deprecated SMS Push");

  private SmsToEmailPush(@JsonProperty("toAddress") String toAddress,
                        @JsonProperty("fromAddress") String fromAddress,
                        @JsonProperty("message") String message,
                        @JsonProperty("callbackUrl") String callbackUrl,
                        @JsonProperty("remoteHost") String remoteHost,
                        @JsonProperty("remoteAddress") String remoteAddress,
                        @JsonProperty("traits") Map<String, String> traits) {

    super(toAddress, fromAddress, message, null, callbackUrl, remoteHost, remoteAddress, traits);
  }

  @Override
  public PushType getPushType() {
    return SmsToEmailPush.PUSH_TYPE;
  }

  public static SmsToEmailPush newPush(String toAddress, String fromAddress, String emailSubject, String callbackUrl, InetAddress remoteAddress, String... traits) {
    return new SmsToEmailPush(toAddress, fromAddress, emailSubject, callbackUrl, remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(), BeanUtils.toMap(traits));
  }

  public static SmsToEmailPush newPush(String toAddress, String fromAddress, String emailSubject, String callbackUrl, InetAddress remoteAddress, Map<String,String> traits) {
    return new SmsToEmailPush(toAddress, fromAddress, emailSubject, callbackUrl, remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(), traits);
  }
}
