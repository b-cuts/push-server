/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.clients.actions;

import com.cosmicpush.TestFactory;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.actions.CreateClientAction;
import com.cosmicpush.common.requests.*;
import com.cosmicpush.jackson.CpObjectMapper;
import com.cosmicpush.pub.common.*;
import com.cosmicpush.pub.push.*;
import java.net.InetAddress;
import java.util.Map;
import org.crazyyak.dev.common.*;
import org.crazyyak.dev.jackson.YakJacksonTranslator;
import org.testng.annotations.*;

import static org.testng.Assert.assertEquals;

@Test
public class ApiRequestTest {

  String ipAddress;
  String hostName;

  // The test is really of the object mapper. We will use
  // a translator here just to make the work a little easier.
  private CpObjectMapper objectMapper = new CpObjectMapper();
  private YakJacksonTranslator translator = new YakJacksonTranslator(objectMapper);

  private ApiRequestStore apiRequestStore;
  private ApiClient apiClient = new ApiClient().create(new CreateClientAction("api-client", "some-password"));

  @BeforeClass
  public void beforeClass() throws Exception {
    this.apiRequestStore = TestFactory.get().getApiRequestStore();
    this.ipAddress = InetAddress.getLocalHost().getHostAddress();
    this.hostName = InetAddress.getLocalHost().getCanonicalHostName();
  }

  public void testCreate() throws Exception {

    CreateClientAction createAction = new CreateClientAction("test", "password");
    ApiClient apiClient = new ApiClient().create(createAction);

    InetAddress inetAddress = InetAddress.getLocalHost();

    SmtpEmailPush smtpEmailPush = new SmtpEmailPush(
        "from", "to",
        "subject", "the HTML content",
        null, BeanUtils.toMap("unit-test:true"));
    ApiRequest request = new ApiRequest(apiClient, smtpEmailPush, inetAddress);
    apiRequestStore.create(request);

    SesEmailPush sesEmailPush = new SesEmailPush(
        "from", "to",
        "subject", "the HTML content",
        null, BeanUtils.toMap("unit-test:true"));
    request = new ApiRequest(apiClient, sesEmailPush, inetAddress);
    apiRequestStore.create(request);

    GoogleTalkPush imPush = GoogleTalkPush.googleTalk("recipient", "some message", null);
    request = new ApiRequest(apiClient, imPush, inetAddress);
    apiRequestStore.create(request);

    NotificationPush notificationPush = new NotificationPush("message", null, BeanUtils.toMap("test:true"));
    request = new ApiRequest(apiClient, notificationPush, inetAddress);
    apiRequestStore.create(request);

  }

  public void testTranslateSmtpEmailPush() throws Exception {
    Push push = new SmtpEmailPush("mickey.mouse@disney.com", "donald.duck@disney.com", "This is the subject", "<html><body><h1>Hello World</h1>So, how's it going?</body></html>", "http://callback.com/api.sent", "test:true", "type:email");

    ApiRequest oldApiRequest = new ApiRequest(apiClient, push, InetAddress.getLocalHost());
    String json = translator.toJson(oldApiRequest);

    String expected = String.format("{\n" +
        "  \"apiClientId\" : \"%s\",\n" +
        "  \"createdAt\" : \"%s\",\n" +
        "  \"requestStatus\" : \"pending\",\n" +
        "  \"ipAddress\" : \"%s\",\n" +
        "  \"remoteHost\" : \"%s\",\n" +
        "  \"pushType\" : \"smtp-email\",\n" +
        "  \"notes\" : [ ],\n" +
        "  \"push\" : {\n" +
        "    \"pushType\" : \"smtp-email\",\n" +
        "    \"toAddress\" : \"mickey.mouse@disney.com\",\n" +
        "    \"fromAddress\" : \"donald.duck@disney.com\",\n" +
        "    \"emailSubject\" : \"This is the subject\",\n" +
        "    \"htmlContent\" : \"<h1>Hello World</h1>So, how's it going?\",\n" +
        "    \"callbackUrl\" : \"http://callback.com/api.sent\",\n" +
        "    \"traits\" : {\n" +
        "      \"test\" : \"true\",\n" +
        "      \"type\" : \"email\"\n" +
        "    }\n" +
        "  },\n" +
        "  \"apiRequestId\" : \"%s\",\n" +
        "  \"revision\" : null\n" +
        "}", apiClient.getApiClientId(), oldApiRequest.getCreatedAt(), ipAddress, hostName, oldApiRequest.getApiRequestId());

    assertEquals(json, expected);

    ApiRequest newApiRequest = translator.fromJson(ApiRequest.class, json);
    ComparisonResults results = EqualsUtils.compare(newApiRequest, oldApiRequest);
    results.assertValidationComplete();
  }

  public void testTranslateEmailPush() throws Exception {
    Push push = new SesEmailPush("mickey.mouse@disney.com", "donald.duck@disney.com", "This is the subject", "<html><body><h1>Hello World</h1>So, how's it going?</body></html>", "http://callback.com/api.sent", "test:true", "type:email");

    ApiRequest oldApiRequest = new ApiRequest(apiClient, push, InetAddress.getLocalHost());
    String json = translator.toJson(oldApiRequest);

    String expected = String.format("{\n" +
        "  \"apiClientId\" : \"%s\",\n" +
        "  \"createdAt\" : \"%s\",\n" +
        "  \"requestStatus\" : \"pending\",\n" +
        "  \"ipAddress\" : \"%s\",\n" +
        "  \"remoteHost\" : \"%s\",\n" +
        "  \"pushType\" : \"ses-email\",\n" +
        "  \"notes\" : [ ],\n" +
        "  \"push\" : {\n" +
        "    \"pushType\" : \"ses-email\",\n" +
        "    \"toAddress\" : \"mickey.mouse@disney.com\",\n" +
        "    \"fromAddress\" : \"donald.duck@disney.com\",\n" +
        "    \"emailSubject\" : \"This is the subject\",\n" +
        "    \"htmlContent\" : \"<h1>Hello World</h1>So, how's it going?\",\n" +
        "    \"callbackUrl\" : \"http://callback.com/api.sent\",\n" +
        "    \"traits\" : {\n" +
        "      \"test\" : \"true\",\n" +
        "      \"type\" : \"email\"\n" +
        "    }\n" +
        "  },\n" +
        "  \"apiRequestId\" : \"%s\",\n" +
        "  \"revision\" : null\n" +
        "}", apiClient.getApiClientId(), oldApiRequest.getCreatedAt(), ipAddress, hostName, oldApiRequest.getApiRequestId());

    assertEquals(json, expected);

    ApiRequest newApiRequest = translator.fromJson(ApiRequest.class, json);
    ComparisonResults results = EqualsUtils.compare(newApiRequest, oldApiRequest);
    results.assertValidationComplete();
  }

  public void testTranslateImPush() throws Exception {
    Push push = new GoogleTalkPush("mickey.mouse@disney.com", "Just calling to say hello", "http://callback.com/api.sent");

    ApiRequest oldApiRequest = new ApiRequest(apiClient, push, InetAddress.getLocalHost());
    String json = translator.toJson(oldApiRequest);

    String expected = String.format("{\n" +
        "  \"apiClientId\" : \"%s\",\n" +
        "  \"createdAt\" : \"%s\",\n" +
        "  \"requestStatus\" : \"pending\",\n" +
        "  \"ipAddress\" : \"%s\",\n" +
        "  \"remoteHost\" : \"%s\",\n" +
        "  \"pushType\" : \"google-talk\",\n" +
        "  \"notes\" : [ ],\n" +
        "  \"push\" : {\n" +
        "    \"pushType\" : \"google-talk\",\n" +
        "    \"recipient\" : \"mickey.mouse@disney.com\",\n" +
        "    \"message\" : \"Just calling to say hello\",\n" +
        "    \"callbackUrl\" : \"http://callback.com/api.sent\"\n" +
        "  },\n" +
        "  \"apiRequestId\" : \"%s\",\n" +
        "  \"revision\" : null\n" +
        "}", apiClient.getApiClientId(), oldApiRequest.getCreatedAt(), ipAddress, hostName, oldApiRequest.getApiRequestId());

    assertEquals(json, expected);

    ApiRequest newApiRequest = translator.fromJson(ApiRequest.class, json);
    ComparisonResults results = EqualsUtils.compare(newApiRequest, oldApiRequest);
    results.assertValidationComplete();
  }

  public void testNotificationPush() throws Exception {
    Push push = new NotificationPush("Hey, you need to check this out.", "http://callback.com/api.sent", "test:true", "type:warning");

    ApiRequest oldApiRequest = new ApiRequest(apiClient, push, InetAddress.getLocalHost());
    String json = translator.toJson(oldApiRequest);

    String expected = String.format("{\n" +
        "  \"apiClientId\" : \"%s\",\n" +
        "  \"createdAt\" : \"%s\",\n" +
        "  \"requestStatus\" : \"pending\",\n" +
        "  \"ipAddress\" : \"%s\",\n" +
        "  \"remoteHost\" : \"%s\",\n" +
        "  \"pushType\" : \"notification\",\n" +
        "  \"notes\" : [ ],\n" +
        "  \"push\" : {\n" +
        "    \"pushType\" : \"notification\",\n" +
        "    \"message\" : \"Hey, you need to check this out.\",\n" +
        "    \"callbackUrl\" : \"http://callback.com/api.sent\",\n" +
        "    \"traits\" : {\n" +
        "      \"test\" : \"true\",\n" +
        "      \"type\" : \"warning\"\n" +
        "    }\n" +
        "  },\n" +
        "  \"apiRequestId\" : \"%s\",\n" +
        "  \"revision\" : null\n" +
        "}", apiClient.getApiClientId(), oldApiRequest.getCreatedAt(), ipAddress, hostName, oldApiRequest.getApiRequestId());

    assertEquals(json, expected);

    ApiRequest newApiRequest = translator.fromJson(ApiRequest.class, json);
    ComparisonResults results = EqualsUtils.compare(newApiRequest, oldApiRequest);
    results.assertValidationComplete();
  }

  public void testUserEventPush() throws Exception {
    Map<String,String> map = BeanUtils.toMap("user:mickeym");
    UserAgent userAgent = new UserAgent(
        "agent-type", "agent-name", "agent-version", "agent-language", "agent-lang-tag",
        "os-type", "os-name", "os=produceer", "osproducer-url", "os-version-name", "os-version-number",
        "linux-distro"
    );
    UserEventPush push = new UserEventPush(
        "some-deviceId", "some-sessionId", "mickey", "192.168.1.36",
        DateUtils.toLocalDateTime("2014-05-06T09:34"), "You logged in.",
        map, userAgent, "http://callback.com/api.sent");

    ApiRequest oldApiRequest = new ApiRequest(apiClient, push, InetAddress.getLocalHost());
    String json = translator.toJson(oldApiRequest);

    String expected = String.format("{\n" +
        "  \"apiClientId\" : \"%s\",\n" +
        "  \"createdAt\" : \"%s\",\n" +
        "  \"requestStatus\" : \"pending\",\n" +
        "  \"ipAddress\" : \"%s\",\n" +
        "  \"remoteHost\" : \"%s\",\n" +
        "  \"pushType\" : \"userEvent\",\n" +
        "  \"notes\" : [ ],\n" +
        "  \"push\" : {\n" +
        "    \"pushType\" : \"userEvent\",\n" +
        "    \"deviceId\" : \"some-deviceId\",\n" +
        "    \"sessionId\" : \"some-sessionId\",\n" +
        "    \"userName\" : \"mickey\",\n" +
        "    \"ipAddress\" : \"192.168.1.36\",\n" +
        "    \"createdAt\" : \"2014-05-06T09:34\",\n" +
        "    \"message\" : \"You logged in.\",\n" +
        "    \"traits\" : {\n" +
        "      \"user\" : \"mickeym\"\n" +
        "    },\n" +
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
        "    \"callbackUrl\" : \"http://callback.com/api.sent\",\n" +
        "    \"sendStory\" : false\n" +
        "  },\n" +
        "  \"apiRequestId\" : \"%s\",\n" +
        "  \"revision\" : null\n" +
        "}", apiClient.getApiClientId(), oldApiRequest.getCreatedAt(), ipAddress, hostName, oldApiRequest.getApiRequestId());

    assertEquals(json, expected);

    ApiRequest newApiRequest = translator.fromJson(ApiRequest.class, json);
    ComparisonResults results = EqualsUtils.compare(newApiRequest, oldApiRequest);
    results.assertValidationComplete();
  }
}
