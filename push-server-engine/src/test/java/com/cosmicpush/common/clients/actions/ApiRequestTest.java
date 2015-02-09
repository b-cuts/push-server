/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.clients.actions;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.common.requests.ApiRequestStore;
import com.cosmicpush.common.system.AppContext;
import com.cosmicpush.jackson.CpObjectMapper;
import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.common.UserAgent;
import com.cosmicpush.pub.internal.CpRemoteClient;
import com.cosmicpush.pub.push.*;
import com.cosmicpush.test.TestFactory;
import org.crazyyak.dev.common.BeanUtils;
import org.crazyyak.dev.common.ComparisonResults;
import org.crazyyak.dev.common.DateUtils;
import org.crazyyak.dev.common.EqualsUtils;
import org.crazyyak.dev.jackson.YakJacksonTranslator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.InetAddress;

import static org.testng.Assert.assertEquals;

@Test
public class ApiRequestTest {

  private TestFactory testFactory;

  private String callbackUrl = "http://www.example.com/callback";

  // The test is really of the object mapper. We will use
  // a translator here just to make the work a little easier.
  private CpObjectMapper objectMapper = new CpObjectMapper();
  private YakJacksonTranslator translator = new YakJacksonTranslator(objectMapper);

  private ApiRequestStore apiRequestStore;

  @BeforeClass
  public void beforeClass() throws Exception {
    testFactory = TestFactory.get();
    this.apiRequestStore = testFactory.getApiRequestStore();
  }

  public void testCreate() throws Exception {

    Account account = testFactory.createAccount();
    Domain domain = testFactory.createDomain(account);

    SmtpEmailPush smtpEmailPush = SmtpEmailPush.newPush(
        "from", "to", "subject", "the HTML content",
        callbackUrl, BeanUtils.toMap("unit-test:true"));
    ApiRequest request = new ApiRequest(AppContext.CURRENT_API_VERSION, domain, smtpEmailPush);
    apiRequestStore.create(request);

    SesEmailPush sesEmailPush = SesEmailPush.newPush(
        "from", "to", "subject", "the HTML content",
        callbackUrl, BeanUtils.toMap("unit-test:true"));
    request = new ApiRequest(AppContext.CURRENT_API_VERSION, domain, sesEmailPush);
    apiRequestStore.create(request);

    GoogleTalkPush imPush = GoogleTalkPush.newPush("recipient", "some message", callbackUrl, "color:red");
    request = new ApiRequest(AppContext.CURRENT_API_VERSION, domain, imPush);
    apiRequestStore.create(request);

    NotificationPush notificationPush = NotificationPush.newPush("message", callbackUrl, BeanUtils.toMap("test:true"));
    request = new ApiRequest(AppContext.CURRENT_API_VERSION, domain, notificationPush);
    apiRequestStore.create(request);

  }

  public void testTranslateSmtpEmailPush() throws Exception {

    Account account = testFactory.createAccount();
    Domain domain = testFactory.createDomain(account);

    Push push = SmtpEmailPush.newPush(
        "mickey.mouse@disney.com",
        "donald.duck@disney.com",
        "This is the subject",
        "<html><body><h1>Hello World</h1>So, how's it going?</body></html>",
        callbackUrl, "test:true", "type:email");

    InetAddress remoteAddress = InetAddress.getLocalHost();
    ApiRequest oldApiRequest = new ApiRequest(AppContext.CURRENT_API_VERSION, domain, push);
    String json = translator.toJson(oldApiRequest);

    String expected = String.format("{\n" +
        "  \"domainId\" : \"%s\",\n" +
        "  \"apiDomainKey\" : \"some-key\",\n" +
        "  \"createdAt\" : \"%s\",\n" +
        "  \"requestStatus\" : \"pending\",\n" +
        "  \"remoteHost\" : \"%s\",\n" +
        "  \"remoteAddress\" : \"%s\",\n" +
        "  \"pushType\" : \"smtp-email\",\n" +
        "  \"notes\" : [ ],\n" +
        "  \"push\" : {\n" +
        "    \"pushType\" : \"smtp-email\",\n" +
        "    \"toAddress\" : \"mickey.mouse@disney.com\",\n" +
        "    \"fromAddress\" : \"donald.duck@disney.com\",\n" +
        "    \"emailSubject\" : \"This is the subject\",\n" +
        "    \"htmlContent\" : \"<h1>Hello World</h1>So, how's it going?\",\n" +
        "    \"callbackUrl\" : \"http://www.example.com/callback\",\n" +
        "    \"remoteHost\" : \"%s\",\n" +
        "    \"remoteAddress\" : \"%s\",\n" +
        "    \"traits\" : {\n" +
        "      \"test\" : \"true\",\n" +
        "      \"type\" : \"email\"\n" +
        "    }\n" +
        "  },\n" +
        "  \"apiRequestId\" : \"%s\",\n" +
        "  \"revision\" : null\n" +
        "}",
        domain.getDomainId(), oldApiRequest.getCreatedAt(),
        remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(),
        remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(),
        oldApiRequest.getApiRequestId());

    assertEquals(json, expected);

    ApiRequest newApiRequest = translator.fromJson(ApiRequest.class, json);
    ComparisonResults results = EqualsUtils.compare(newApiRequest, oldApiRequest);
    results.assertValidationComplete();
  }

  public void testTranslateEmailPush() throws Exception {

    Account account = testFactory.createAccount();
    Domain domain = testFactory.createDomain(account);

    Push push = SesEmailPush.newPush(
        "mickey.mouse@disney.com",
        "donald.duck@disney.com",
        "This is the subject",
        "<html><body><h1>Hello World</h1>So, how's it going?</body></html>",
        callbackUrl, "test:true", "type:email");

    InetAddress remoteAddress = InetAddress.getLocalHost();
    ApiRequest oldApiRequest = new ApiRequest(AppContext.CURRENT_API_VERSION, domain, push);
    String json = translator.toJson(oldApiRequest);

    String expected = String.format("{\n" +
        "  \"domainId\" : \"%s\",\n" +
        "  \"apiDomainKey\" : \"some-key\",\n" +
        "  \"createdAt\" : \"%s\",\n" +
        "  \"requestStatus\" : \"pending\",\n" +
        "  \"remoteHost\" : \"%s\",\n" +
        "  \"remoteAddress\" : \"%s\",\n" +
        "  \"pushType\" : \"ses-email\",\n" +
        "  \"notes\" : [ ],\n" +
        "  \"push\" : {\n" +
        "    \"pushType\" : \"ses-email\",\n" +
        "    \"toAddress\" : \"mickey.mouse@disney.com\",\n" +
        "    \"fromAddress\" : \"donald.duck@disney.com\",\n" +
        "    \"emailSubject\" : \"This is the subject\",\n" +
        "    \"htmlContent\" : \"<h1>Hello World</h1>So, how's it going?\",\n" +
        "    \"callbackUrl\" : \"http://www.example.com/callback\",\n" +
        "    \"remoteHost\" : \"%s\",\n" +
        "    \"remoteAddress\" : \"%s\",\n" +
        "    \"traits\" : {\n" +
        "      \"test\" : \"true\",\n" +
        "      \"type\" : \"email\"\n" +
        "    }\n" +
        "  },\n" +
        "  \"apiRequestId\" : \"%s\",\n" +
        "  \"revision\" : null\n" +
        "}",
        domain.getDomainId(), oldApiRequest.getCreatedAt(),
        remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(),
        remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(),
        oldApiRequest.getApiRequestId());

    assertEquals(json, expected);

    ApiRequest newApiRequest = translator.fromJson(ApiRequest.class, json);
    ComparisonResults results = EqualsUtils.compare(newApiRequest, oldApiRequest);
    results.assertValidationComplete();
  }

  public void testTranslateImPush() throws Exception {

    Account account = testFactory.createAccount();
    Domain domain = testFactory.createDomain(account);

    Push push = GoogleTalkPush.newPush(
        "mickey.mouse@disney.com",
        "Just calling to say hello",
        callbackUrl, BeanUtils.toMap("color:green"));

    InetAddress remoteAddress = InetAddress.getLocalHost();
    ApiRequest oldApiRequest = new ApiRequest(AppContext.CURRENT_API_VERSION, domain, push);
    String json = translator.toJson(oldApiRequest);

    String expected = String.format("{\n" +
        "  \"domainId\" : \"%s\",\n" +
        "  \"apiDomainKey\" : \"some-key\",\n" +
        "  \"createdAt\" : \"%s\",\n" +
        "  \"requestStatus\" : \"pending\",\n" +
        "  \"remoteHost\" : \"%s\",\n" +
        "  \"remoteAddress\" : \"%s\",\n" +
        "  \"pushType\" : \"google-talk\",\n" +
        "  \"notes\" : [ ],\n" +
        "  \"push\" : {\n" +
        "    \"pushType\" : \"google-talk\",\n" +
        "    \"recipient\" : \"mickey.mouse@disney.com\",\n" +
        "    \"message\" : \"Just calling to say hello\",\n" +
        "    \"callbackUrl\" : \"http://www.example.com/callback\",\n" +
        "    \"remoteHost\" : \"%s\",\n" +
        "    \"remoteAddress\" : \"%s\",\n" +
        "    \"traits\" : {\n" +
        "      \"color\" : \"green\"\n" +
        "    }\n"+
        "  },\n" +
        "  \"apiRequestId\" : \"%s\",\n" +
        "  \"revision\" : null\n" +
        "}",
        domain.getDomainId(), oldApiRequest.getCreatedAt(),
        remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(),
        remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(),
        oldApiRequest.getApiRequestId());

    assertEquals(json, expected);

    ApiRequest newApiRequest = translator.fromJson(ApiRequest.class, json);
    ComparisonResults results = EqualsUtils.compare(newApiRequest, oldApiRequest);
    results.assertValidationComplete();
  }

  public void testNotificationPush() throws Exception {

    Account account = testFactory.createAccount();
    Domain domain = testFactory.createDomain(account);

    Push push = NotificationPush.newPush(
        "Hey, you need to check this out.",
        callbackUrl, "test:true", "type:warning");

    InetAddress remoteAddress = InetAddress.getLocalHost();
    ApiRequest oldApiRequest = new ApiRequest(AppContext.CURRENT_API_VERSION, domain, push);
    String json = translator.toJson(oldApiRequest);

    String expected = String.format("{\n" +
        "  \"domainId\" : \"%s\",\n" +
        "  \"apiDomainKey\" : \"some-key\",\n" +
        "  \"createdAt\" : \"%s\",\n" +
        "  \"requestStatus\" : \"pending\",\n" +
        "  \"remoteHost\" : \"%s\",\n" +
        "  \"remoteAddress\" : \"%s\",\n" +
        "  \"pushType\" : \"notification\",\n" +
        "  \"notes\" : [ ],\n" +
        "  \"push\" : {\n" +
        "    \"pushType\" : \"notification\",\n" +
        "    \"message\" : \"Hey, you need to check this out.\",\n" +
        "    \"callbackUrl\" : \"http://www.example.com/callback\",\n" +
        "    \"remoteHost\" : \"%s\",\n" +
        "    \"remoteAddress\" : \"%s\",\n" +
        "    \"traits\" : {\n" +
        "      \"test\" : \"true\",\n" +
        "      \"type\" : \"warning\"\n" +
        "    }\n" +
        "  },\n" +
        "  \"apiRequestId\" : \"%s\",\n" +
        "  \"revision\" : null\n" +
        "}",
        domain.getDomainId(), oldApiRequest.getCreatedAt(),
        remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(),
        remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(),
        oldApiRequest.getApiRequestId());

    assertEquals(json, expected);

    ApiRequest newApiRequest = translator.fromJson(ApiRequest.class, json);
    ComparisonResults results = EqualsUtils.compare(newApiRequest, oldApiRequest);
    results.assertValidationComplete();
  }

  public void testUserEventPush() throws Exception {

    Account account = testFactory.createAccount();
    Domain domain = testFactory.createDomain(account);
    UserAgent userAgent = testFactory.createUserAgent();

    CpRemoteClient remoteClient = new CpRemoteClient() {
      @Override public String getUserName() { return "mickey"; }
      @Override public String getIpAddress() { return "192.168.1.36"; }
      @Override public String getSessionId() { return "some-sessionId"; }
      @Override public String getDeviceId() { return "some-deviceId"; }
    };

    UserEventPush push = UserEventPush.newPush(
        remoteClient,
        DateUtils.toLocalDateTime("2014-05-06T09:34"),
        "You logged in.",
        userAgent, callbackUrl, "color:green");

    InetAddress remoteAddress = InetAddress.getLocalHost();
    ApiRequest oldApiRequest = new ApiRequest(AppContext.CURRENT_API_VERSION, domain, push);
    String json = translator.toJson(oldApiRequest);

    String expected = String.format("{\n" +
        "  \"domainId\" : \"%s\",\n" +
        "  \"apiDomainKey\" : \"some-key\",\n" +
        "  \"createdAt\" : \"%s\",\n" +
        "  \"requestStatus\" : \"pending\",\n" +
        "  \"remoteHost\" : \"%s\",\n" +
        "  \"remoteAddress\" : \"%s\",\n" +
        "  \"pushType\" : \"userEvent\",\n" +
        "  \"notes\" : [ ],\n" +
        "  \"push\" : {\n" +
        "    \"pushType\" : \"userEvent\",\n" +
        "    \"sendStory\" : false,\n" +
        "    \"deviceId\" : \"some-deviceId\",\n" +
        "    \"sessionId\" : \"some-sessionId\",\n" +
        "    \"userName\" : \"mickey\",\n" +
        "    \"ipAddress\" : \"192.168.1.36\",\n" +
        "    \"createdAt\" : \"2014-05-06T09:34\",\n" +
        "    \"message\" : \"You logged in.\",\n" +
        "    \"userAgent\" : {\n" +
        "      \"agentType\" : \"agent-type\",\n" +
        "      \"agentName\" : \"agent-name\",\n" +
        "      \"agentVersion\" : \"agent-version\",\n" +
        "      \"agentLanguage\" : \"agent-language\",\n" +
        "      \"agentLanguageTag\" : \"agent-lang-tag\",\n" +
        "      \"osType\" : \"os-type\",\n" +
        "      \"osName\" : \"os-name\",\n" +
        "      \"osProducer\" : \"os=produceer\",\n" +
        "      \"osProducerUrl\" : \"osproducer-url\",\n" +
        "      \"osVersionName\" : \"os-version-name\",\n" +
        "      \"osVersionNumber\" : \"os-version-number\",\n" +
        "      \"linuxDistribution\" : \"linux-distro\"\n" +
        "    },\n" +
        "    \"callbackUrl\" : \"http://www.example.com/callback\",\n" +
        "    \"remoteHost\" : \"%s\",\n" +
        "    \"remoteAddress\" : \"%s\",\n" +
        "    \"traits\" : {\n" +
        "      \"color\" : \"green\"\n" +
        "    }\n" +
        "  },\n" +
        "  \"apiRequestId\" : \"%s\",\n" +
        "  \"revision\" : null\n" +
        "}",
        domain.getDomainId(), oldApiRequest.getCreatedAt(),
        remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(),
        remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress(),
        oldApiRequest.getApiRequestId());

    assertEquals(json, expected);

    ApiRequest newApiRequest = translator.fromJson(ApiRequest.class, json);
    ComparisonResults results = EqualsUtils.compare(newApiRequest, oldApiRequest);
    results.assertValidationComplete();
  }
}
