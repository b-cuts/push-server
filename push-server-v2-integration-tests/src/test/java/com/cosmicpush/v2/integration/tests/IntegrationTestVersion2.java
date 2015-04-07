package com.cosmicpush.v2.integration.tests;

import com.cosmicpush.gateway.LiveCosmicPushGateway;
import com.cosmicpush.pub.common.PingPush;
import com.cosmicpush.pub.common.PushResponse;
import com.cosmicpush.pub.common.RequestStatus;
import com.cosmicpush.pub.push.*;
import com.cosmicpush.test.TestFactory;
import org.crazyyak.dev.common.BeanUtils;
import org.crazyyak.dev.common.EnvUtils;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Test
public class IntegrationTestVersion2 {

  private TestFactory testFactory;
  private LiveCosmicPushGateway gateway;
  private String callbackUrl = null;

  @BeforeClass
  public void beforeClass() throws Exception {
    testFactory = TestFactory.get();

//    String url = "http://www.localhost:9010/push-server/api/v2";
    String url = "http://www.cosmicpush.com/api/v2";

    String username = EnvUtils.findProperty("TIOGA_TEST_DOMAIN_NAME");
    ExceptionUtils.assertNotNull(username, "TIOGA_TEST_DOMAIN_NAME");

    String password = EnvUtils.findProperty("TIOGA_TEST_DOMAIN_PASS");
    ExceptionUtils.assertNotNull(password, "TIOGA_TEST_DOMAIN_PASS");

    gateway = new LiveCosmicPushGateway(url, username, password);
  }

  public void testNotificationPush() throws Exception {
    LqNotificationPush push = LqNotificationPush.newPush("integration-test", "Notice what I'm doing?", "tracking-id", callbackUrl, BeanUtils.toMap("unit-test:true"));
    PushResponse response = gateway.send(push);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);

    push = LqNotificationPush.newPush("integration-test", "Now I want to share some info", "tracking-id", callbackUrl, BeanUtils.toMap("day:Sunday", "size:Large"));
    response = gateway.send(push);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);

    String msg = ExceptionUtils.toString(new IllegalArgumentException("I think I might have broken it!"));
    push = LqNotificationPush.newPush("integration-test", "Something really bad happened here!", "tracking-id", callbackUrl, BeanUtils.toMap("priority:Urgent", "exception:" + msg));
    response = gateway.send(push);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);
  }

  public void testXmppPush() throws Exception {
    XmppPush push = XmppPush.newPush("jacob.parr@gmail.com", "Are you there?", callbackUrl, BeanUtils.toMap("color:red", "test:yes"));
    PushResponse response = gateway.send(push);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);
  }

  public void testTwilioSmsPush() throws Exception {
    TwilioSmsPush push = TwilioSmsPush.newPush("7745677277", "5596407277", "test message", "http://example.com/callback");
    PushResponse response = gateway.send(push);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);
  }

  public void testSesEmailPush() throws Exception {
    SesEmailPush push = SesEmailPush.newPush(
        "Test Parr <test@jacobparr.com>",
        "Bot Parr <bot@jacobparr.com>",
        "This is a test", "<html><body>Are you there?</body></html>",
        callbackUrl, BeanUtils.toMap("unit-test:true"));

    PushResponse response = gateway.send(push);
    assertNotNull(response);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);
  }

  public void testSmtpEmailPush() throws Exception {
    SmtpEmailPush push = SmtpEmailPush.newPush(
        "Test Parr <test@jacobparr.com>",
        "Bot Parr <bot@jacobparr.com>",
        "This is a test",
        "Are you there?",
        callbackUrl, BeanUtils.toMap("unit-test:true"));

    PushResponse response = gateway.send(push);
    assertNotNull(response);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);
  }

  public void testEmailPush() throws Exception {
    EmailPush push = EmailPush.newPush(
        "Test Parr <test@jacobparr.com>",
        "Bot Parr <bot@jacobparr.com>",
        "This is a test",
        "Are you there?",
        callbackUrl, BeanUtils.toMap("unit-test:true"));

    PushResponse response = gateway.send(push);
    assertNotNull(response);
    assertEquals(response.getRequestStatus(), RequestStatus.pending);
  }

  public void testPingPush() throws Exception {
    long duration = gateway.ping();
    assertTrue(duration > 0);

    PushResponse response = gateway.send(PingPush.newPush());
    assertNotNull(response.getCreatedAt());
    assertNotNull(response.getDomainId());
    assertTrue(response.getNotes().isEmpty());
    assertNotNull(response.getPushRequestId());
    assertEquals(response.getRequestStatus(), RequestStatus.processed);
  }
}
