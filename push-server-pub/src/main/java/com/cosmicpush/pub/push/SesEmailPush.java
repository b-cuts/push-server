package com.cosmicpush.pub.push;

import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.internal.PushUtils;
import com.fasterxml.jackson.annotation.*;
import org.crazyyak.dev.common.BeanUtils;

import java.net.InetAddress;
import java.util.Map;

public class SesEmailPush extends EmailPush {

  public static final PushType PUSH_TYPE = new PushType(SesEmailPush.class, "ses-email", "ASES E-Mail");

  private SesEmailPush(@JsonProperty("toAddress") String toAddress,
                       @JsonProperty("fromAddress") String fromAddress,
                       @JsonProperty("emailSubject") String emailSubject,
                       @JsonProperty("htmlContent") String htmlContent,
                       @JsonProperty("callbackUrl") String callbackUrl,
                       @JsonProperty("remoteHost") String remoteHost,
                       @JsonProperty("remoteAddress") String remoteAddress,
                       @JsonProperty("traits") Map<String, String> traits) {

    super(toAddress, fromAddress, emailSubject, htmlContent, callbackUrl, remoteHost, remoteAddress, traits);
  }

  @Override
  public PushType getPushType() {
    return PUSH_TYPE;
  }

  public static SesEmailPush newPush(String toAddress, String fromAddress,
                                     String emailSubject, String htmlContent,
                                     String callbackUrl, String... traits) {

    InetAddress remoteAddress = PushUtils.getLocalHost();
    return new SesEmailPush(toAddress, fromAddress, emailSubject, htmlContent, callbackUrl, remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(), BeanUtils.toMap(traits));
  }

  public static SesEmailPush newPush(String toAddress, String fromAddress,
                                     String emailSubject, String htmlContent,
                                     String callbackUrl, Map<String, String> traits) {

    InetAddress remoteAddress = PushUtils.getLocalHost();
    return new SesEmailPush(toAddress, fromAddress, emailSubject, htmlContent, callbackUrl, remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(), traits);
  }
}
