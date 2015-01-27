package com.cosmicpush.v1.integration.tests;

import com.cosmicpush.gateway.LiveCosmicPushGateway;
import com.cosmicpush.pub.common.*;
import com.cosmicpush.pub.internal.CpIdGenerator;
import com.cosmicpush.pub.push.*;
import java.io.*;
import java.util.*;
import org.crazyyak.dev.common.*;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;
import org.joda.time.LocalDateTime;
import org.testng.Assert;
import org.testng.annotations.*;

import static org.junit.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Test
public class IntegrationTestVersion1 {

  private LiveCosmicPushGateway gateway;

  @BeforeClass
  public void beforeClass() throws Exception {
    String url = "http://localhost:9010/push-server/api";

    String username = EnvUtils.findProperty("PUSH_TEST_USERNAME");
    ExceptionUtils.assertNotNull(username, "PUSH_TEST_USERNAME");

    String password = EnvUtils.findProperty("PUSH_TEST_PASSWORD");
    ExceptionUtils.assertNotNull(password, "PUSH_TEST_PASSWORD");

    gateway = new LiveCosmicPushGateway(url, username, password);
  }

  public void testNotificationPush() throws Exception {
    NotificationPush action = new NotificationPush("Notice what I'm doing?", null, BeanUtils.toMap("unit-test:true"));
    PushResponse response = gateway.push(action);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);

    action = new NotificationPush("Now I want to share some info", null, BeanUtils.toMap("day:Sunday", "size:Large"));
    response = gateway.push(action);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);

    String msg = ExceptionUtils.toString(new IllegalArgumentException("I think I might have broken it!"));
    action = new NotificationPush("Something really bad happened here!", null, BeanUtils.toMap("priority:Urgent", "exception:"+msg));
    response = gateway.push(action);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);
  }

  public void testImPush() throws Exception {
    ImPush push = ImPush.googleTalk("jacob.parr@gmail.com", "Are you there?", null);
    PushResponse response = gateway.push(push);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);
  }

  public void testEmailToSmsPush() throws Exception {
    EmailToSmsPush action = new EmailToSmsPush(
      "Test Parr <test@jacobparr.com>", "Munchie Monster Bot <bot@munchiemonster.com>",
      "This is a test", null);

    PushResponse response = gateway.push(action);
    assertNotNull(response);
    Assert.assertEquals(response.getRequestStatus(), RequestStatus.pending);

    List<String> notes = response.getNotes();
    assertNotNull(notes);
    Assert.assertEquals(notes.size(), 0);
  }

  public void testEmailPush() throws Exception {
    EmailPush action = new EmailPush(
      "Test Parr <test@jacobparr.com>", "Munchie Monster Bot <bot@munchiemonster.com>",
      "This is a test", "Are you there?",
      null,
      BeanUtils.toMap("unit-test:true"));

    PushResponse response = gateway.push(action);
    assertNotNull(response);
    Assert.assertEquals(response.getRequestStatus(), RequestStatus.pending);
  }

  public void testUserEventPush() throws Exception {

    String deviceId = CpIdGenerator.newId();
    String sessionId = CpIdGenerator.newId();

    int posA = sessionId.indexOf("-");
    int posB = sessionId.indexOf("-", posA+1);
    String userName = "Test" + sessionId.substring(posA, posB);

    TestRemoteClient remoteClient = new TestRemoteClient(
        userName,
        deviceId,
        sessionId,
        "192.168.1.1");

    // Event #0
    LocalDateTime createdAt = DateUtils.currentDateTime();
    UserEventPush push = new UserEventPush(remoteClient,
        createdAt, "I'm just looking at the moment.", null, null);
    gateway.push(push);

    // Event #1
    createdAt = DateUtils.currentDateTime();
    push = new UserEventPush(remoteClient, createdAt, "I just logged in.", null, null);
    gateway.push(push);

    // Event #2
    LinkedHashMap<String,String> map = new LinkedHashMap<>();
    map.put("day", "Sunday");
    map.put("size", "Large");
    map.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_6 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11B651 Safari/9537.53");

    createdAt = DateUtils.currentDateTime();
    push = new UserEventPush(remoteClient, createdAt, "This one has a user-agent.", map, null, null);
    gateway.push(push);

    // Event #3
    map = new LinkedHashMap<>();
    map.put("priority", "Urgent");
    map.put("JSON", "{\n\t\"test\":\"true\",\n\t\"cat\":\"dog\",\n\t\"test\":\"true\",\n\t\"cat\":\"dog\",\n\t\"test\":\"true\",\n\t\"cat\":\"dog\",\n\t\"test\":\"true\",\n\t\"cat\":\"dog\"\n}");
    createdAt = DateUtils.currentDateTime();
    push = new UserEventPush(remoteClient, createdAt, "And now I have to go.", map, null, null);
    gateway.push(push);

    // Event #4
    StringWriter writer = new StringWriter();
    new Exception("It went boom!").printStackTrace(new PrintWriter(writer));
    map = new LinkedHashMap<>();
    map.put("exception", writer.toString());
    push = new UserEventPush(remoteClient, createdAt, "Something really bad happened here.", map, null, null);
    gateway.push(push);

    // Event #5
    gateway.push(UserEventPush.sendStory(sessionId, createdAt, null));
  }
}
