package com.cosmicpush.v2.integration.tests;

import com.cosmicpush.gateway.LiveCosmicPushGateway;
import com.cosmicpush.pub.common.PushResponse;
import com.cosmicpush.pub.common.RequestStatus;
import com.cosmicpush.pub.common.UserAgent;
import com.cosmicpush.pub.internal.CpIdGenerator;
import com.cosmicpush.pub.push.*;
import org.crazyyak.dev.common.BeanUtils;
import org.crazyyak.dev.common.DateUtils;
import org.crazyyak.dev.common.EnvUtils;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Test
public class IntegrationTestVersion2 {

  private InetAddress remoteAddress;
  private LiveCosmicPushGateway gateway;
  private String callbackUrl = null;

  @BeforeClass
  public void beforeClass() throws Exception {
    remoteAddress = InetAddress.getLocalHost();

    String url = "http://localhost:9010/push-server/api/v2";
    String username = EnvUtils.requireProperty("PUSH_TEST_USERNAME");
    String password = EnvUtils.requireProperty("PUSH_TEST_PASSWORD");
    gateway = new LiveCosmicPushGateway(url, username, password);
  }

  public void testNotificationPush() throws Exception {
    NotificationPush action = NotificationPush.newPush("Notice what I'm doing?", callbackUrl, remoteAddress, BeanUtils.toMap("unit-test:true"));
    PushResponse response = gateway.push(action);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);

    action = NotificationPush.newPush("Now I want to share some info", callbackUrl, remoteAddress, BeanUtils.toMap("day:Sunday", "size:Large"));
    response = gateway.push(action);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);

    String msg = ExceptionUtils.toString(new IllegalArgumentException("I think I might have broken it!"));
    action = NotificationPush.newPush("Something really bad happened here!", callbackUrl, remoteAddress, BeanUtils.toMap("priority:Urgent", "exception:"+msg));
    response = gateway.push(action);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);
  }

  public void testGoogleTalkPush() throws Exception {
    GoogleTalkPush push = GoogleTalkPush.newPush("jacob.parr@gmail.com", "Are you there?", callbackUrl, remoteAddress, BeanUtils.toMap("color:red", "test:yes"));
    PushResponse response = gateway.push(push);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);
  }

  public void testSesEmailPush() throws Exception {
    SesEmailPush action = SesEmailPush.newPush(
        "Test Parr <test@jacobparr.com>",
        "Bot Parr <bot@jacobparr.com>",
        "This is a test", "<html><body>Are you there?</body></html>",
        callbackUrl, remoteAddress,
        BeanUtils.toMap("unit-test:true"));

    PushResponse response = gateway.push(action);
    assertNotNull(response);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);
  }

  public void testSmtpEmailPush() throws Exception {
    SmtpEmailPush action = SmtpEmailPush.newPush(
        "Test Parr <test@jacobparr.com>",
        "Bot Parr <bot@jacobparr.com>",
        "This is a test",
        "Are you there?",
        callbackUrl, remoteAddress,
        BeanUtils.toMap("unit-test:true"));

    PushResponse response = gateway.push(action);
    assertNotNull(response);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);
  }

  public void testUserEventPush() throws Exception {

    String deviceId = CpIdGenerator.newId();
    String sessionId = CpIdGenerator.newId();

    int posA = sessionId.indexOf("-");
    int posB = sessionId.indexOf("-", posA+1);
    String userName = "Test" + sessionId.substring(posA, posB);

    UserAgent userAgent = new UserAgent(
        "agent-type", "agent-name", "agent-version", "agent-language", "agent-lang-tag",
        "os-type", "os-name", "os=produceer", "osproducer-url", "os-version-name", "os-version-number",
        "linux-distro"
    );

    TestRemoteClient remoteClient = new TestRemoteClient(
        userName,
        deviceId,
        sessionId,
        "192.168.1.1");

    // Event #0
    LocalDateTime createdAt = DateUtils.currentLocalDateTime();
    UserEventPush push = UserEventPush.newPush(
        remoteClient, createdAt,
        "I'm just looking at the moment.",
        userAgent, callbackUrl, remoteAddress,
        BeanUtils.toMap("color:red"));
    gateway.push(push);

    // Event #1
    createdAt = DateUtils.currentLocalDateTime();
    push = UserEventPush.newPush(
        remoteClient, createdAt,
        "I just logged in.",
        userAgent, callbackUrl, remoteAddress,
        BeanUtils.toMap("color:green"));
    gateway.push(push);

    // Event #2
    LinkedHashMap<String,String> map = new LinkedHashMap<>();
    map.put("day", "Sunday");
    map.put("size", "Large");
    map.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_6 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11B651 Safari/9537.53");

    createdAt = DateUtils.currentLocalDateTime();
    push = UserEventPush.newPush(
        remoteClient, createdAt,
        "This one has a user-agent.",
        userAgent, callbackUrl, remoteAddress, map);
    gateway.push(push);

    // Event #3
    map = new LinkedHashMap<>();
    map.put("priority", "Urgent");
    map.put("JSON", "{\n\t\"test\":\"true\",\n\t\"cat\":\"dog\",\n\t\"test\":\"true\",\n\t\"cat\":\"dog\",\n\t\"test\":\"true\",\n\t\"cat\":\"dog\",\n\t\"test\":\"true\",\n\t\"cat\":\"dog\"\n}");
    createdAt = DateUtils.currentLocalDateTime();
    push = UserEventPush.newPush(
        remoteClient, createdAt,
        "And now I have to go.",
        userAgent, callbackUrl, remoteAddress, map);
    gateway.push(push);

    // Event #4
    StringWriter writer = new StringWriter();
    new Exception("It went boom!").printStackTrace(new PrintWriter(writer));
    map = new LinkedHashMap<>();
    map.put("exception", writer.toString());
    push = UserEventPush.newPush(
        remoteClient, createdAt,
        "Something really bad happened here.",
        userAgent, callbackUrl, remoteAddress, map);
    gateway.push(push);

    // Event #5
    gateway.push(UserEventPush.sendStory(sessionId, DateUtils.currentLocalDateTime(), callbackUrl, remoteAddress));
  }
}
