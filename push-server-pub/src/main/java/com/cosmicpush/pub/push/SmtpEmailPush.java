package com.cosmicpush.pub.push;

import com.cosmicpush.pub.common.PushType;
import com.fasterxml.jackson.annotation.*;
import org.crazyyak.dev.common.BeanUtils;

import java.net.InetAddress;
import java.util.Map;

public class SmtpEmailPush extends EmailPush {

  public static final PushType PUSH_TYPE = new PushType(SmtpEmailPush.class, "smtp-email", "SMTP E-Mail");

  protected SmtpEmailPush(@JsonProperty("toAddress") String toAddress,
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

  public static SmtpEmailPush newPush(String toAddress, String fromAddress, String emailSubject, String htmlContent, String callbackUrl, InetAddress remoteAddress, String... traits) {
    return new SmtpEmailPush(toAddress, fromAddress, emailSubject, htmlContent, callbackUrl, remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(), BeanUtils.toMap(traits));
  }

  public static SmtpEmailPush newPush(String toAddress, String fromAddress, String emailSubject, String htmlContent, String callbackUrl, InetAddress remoteAddress, Map<String, String> traits) {
    return new SmtpEmailPush(toAddress, fromAddress, emailSubject, htmlContent, callbackUrl, remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(), traits);
  }
}
