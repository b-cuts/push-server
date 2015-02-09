package com.cosmicpush.app.deprecated;

import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.internal.PushUtils;
import com.cosmicpush.pub.push.EmailPush;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.crazyyak.dev.common.BeanUtils;

import java.net.InetAddress;
import java.util.Map;

public class EmailToSmsPushV1 extends EmailPush {

  public static final PushType PUSH_TYPE = new PushType(EmailToSmsPushV1.class, "emailToSms", "Deprecated SMS Push");

  private EmailToSmsPushV1(@JsonProperty("toAddress") String toAddress,
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
    return PUSH_TYPE;
  }

  public static EmailToSmsPushV1 newPush(String toAddress, String fromAddress, String emailSubject, String callbackUrl, String... traits) {
    InetAddress remoteAddress = PushUtils.getLocalHost();
    return new EmailToSmsPushV1(toAddress, fromAddress, emailSubject, callbackUrl, remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(), BeanUtils.toMap(traits));
  }

  public static EmailToSmsPushV1 newPush(String toAddress, String fromAddress, String emailSubject, String callbackUrl, Map<String,String> traits) {
    InetAddress remoteAddress = PushUtils.getLocalHost();
    return new EmailToSmsPushV1(toAddress, fromAddress, emailSubject, callbackUrl, remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(), traits);
  }
}
