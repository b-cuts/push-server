package com.cosmicpush.app;

import com.cosmicpush.app.resources.RootResource;
import com.cosmicpush.app.resources.manage.account.ManageAccountModel;
import com.cosmicpush.app.resources.manage.client.DomainRequestsModel;
import com.cosmicpush.app.resources.manage.client.ManageDomainModel;
import com.cosmicpush.app.resources.manage.client.emails.EmailModel;
import com.cosmicpush.app.resources.manage.client.emails.EmailsModel;
import com.cosmicpush.app.resources.manage.client.notifications.DomainNotificationModel;
import com.cosmicpush.app.resources.manage.client.userevents.*;
import com.cosmicpush.app.view.Thymeleaf;
import com.cosmicpush.app.view.ThymeleafMessageBodyWriter;
import com.cosmicpush.app.view.ThymeleafViewFactory;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.push.EmailPush;
import com.cosmicpush.pub.push.NotificationPush;
import com.cosmicpush.test.TestFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertNotNull;

@Test
public class ThymeleafTests {

  private TestFactory testFactory;
  private StringWriter writer;
  private ThymeleafMessageBodyWriter msgBodyWriter;

  @BeforeClass
  public void beforeClass() throws Exception {
    testFactory = new TestFactory();
    msgBodyWriter = new ThymeleafMessageBodyWriter(null) {
      @Override public String getBaseUri() { return "http://example.com/unit-tests/"; }
    };
  }

  @BeforeMethod
  public void beforeMethod() {
    writer = new StringWriter();
  }

  public void testWelcome() throws Exception {
    RootResource.WelcomeModel model = new RootResource.WelcomeModel(null, "This is a test", "some-username", "some-password");
    Thymeleaf leaf = new Thymeleaf(ThymeleafViewFactory.WELCOME, model);
    msgBodyWriter.writeTo(leaf, writer);
    String content = writer.toString();
    assertNotNull(content);
  }

  public void testManageApiRequests() throws Exception {

    Account account = testFactory.createAccount();
    Domain domain = testFactory.createDomain(account);
    List<ApiRequest> requests = testFactory.createApiRequests(domain);
    DomainRequestsModel model = new DomainRequestsModel(account, domain, requests);

    Thymeleaf leaf = new Thymeleaf(ThymeleafViewFactory.MANAGE_API_REQUESTS, model);
    msgBodyWriter.writeTo(leaf, writer);
    String content = writer.toString();
    assertNotNull(content);
  }

  public void testManageDomain() throws Exception {

    Account account = testFactory.createAccount();
    Domain domain = testFactory.createDomain(account);
    PluginContext pluginContext = testFactory.pluginContext(testFactory);
    ManageDomainModel model = new ManageDomainModel(pluginContext, account, domain, "This was the last message.");

    Thymeleaf leaf = new Thymeleaf(ThymeleafViewFactory.MANAGE_API_CLIENT, model);
    msgBodyWriter.writeTo(leaf, writer);
    String content = writer.toString();
    assertNotNull(content);
  }

  public void testManageAccount() throws Exception {

    Account account = testFactory.createAccount();
    Domain domain = testFactory.createDomain(account);
    ManageAccountModel model = new ManageAccountModel(account, Arrays.asList(domain));

    Thymeleaf leaf = new Thymeleaf(ThymeleafViewFactory.MANAGE_ACCOUNT, model);
    msgBodyWriter.writeTo(leaf, writer);
    String content = writer.toString();
    assertNotNull(content);
  }

  public void testManageApiEmails() throws Exception {

    Account account = testFactory.createAccount();
    Domain domain = testFactory.createDomain(account);
    List<ApiRequest> requests = testFactory.createApiRequests_Emails(domain);
    EmailsModel model = new EmailsModel(account, domain, requests);

    Thymeleaf leaf = new Thymeleaf(ThymeleafViewFactory.MANAGE_API_EMAILS, model);
    msgBodyWriter.writeTo(leaf, writer);
    String content = writer.toString();
    assertNotNull(content);
  }

  public void testManageApiEmail() throws Exception {

    Account account = testFactory.createAccount();
    Domain domain = testFactory.createDomain(account);
    ApiRequest request = testFactory.createApiRequests_Emails(domain).get(0);
    EmailPush email = request.getEmailPush();
    EmailModel model = new EmailModel(account, domain, request, email);

    Thymeleaf leaf = new Thymeleaf(ThymeleafViewFactory.MANAGE_API_EMAIL, model);
    msgBodyWriter.writeTo(leaf, writer);
    String content = writer.toString();
    assertNotNull(content);
  }

  public void testManageApiNotification() throws Exception {

    Account account = testFactory.createAccount();
    Domain domain = testFactory.createDomain(account);
    ApiRequest request = testFactory.createApiRequests_Notifications(domain).get(0);
    NotificationPush notification = request.getNotificationPush();
    DomainNotificationModel model = new DomainNotificationModel(account, domain, request, notification);

    Thymeleaf leaf = new Thymeleaf(ThymeleafViewFactory.MANAGE_API_NOTIFICATION, model);
    msgBodyWriter.writeTo(leaf, writer);
    String content = writer.toString();
    assertNotNull(content);
  }

  public void testManageApiNotifications() throws Exception {

    Account account = testFactory.createAccount();
    Domain domain = testFactory.createDomain(account);
    List<ApiRequest> requests = testFactory.createApiRequests_Notifications(domain);
    DomainRequestsModel model = new DomainRequestsModel(account, domain, requests);

    Thymeleaf leaf = new Thymeleaf(ThymeleafViewFactory.MANAGE_API_NOTIFICATIONS, model);
    msgBodyWriter.writeTo(leaf, writer);
    String content = writer.toString();
    assertNotNull(content);
  }

  public void testManageUserEvent() throws Exception {

    Account account = testFactory.createAccount();
    Domain domain = testFactory.createDomain(account);
    List<ApiRequest> requests = testFactory.createApiRequests_UserEvents(domain);
    List<UserEventGroup> groups = ManageUserEventsResource.toGroups(requests);
    List<UserEventSession> sessions = groups.get(0).getSessions();

    UserEventSessionsModel model = new UserEventSessionsModel(account, domain, "whatever", sessions);

    Thymeleaf leaf = new Thymeleaf(ThymeleafViewFactory.MANAGE_API_EVENT, model);
    msgBodyWriter.writeTo(leaf, writer);
    String content = writer.toString();
    assertNotNull(content);
  }

  public void testManageUserEvents() throws Exception {

    Account account = testFactory.createAccount();
    Domain domain = testFactory.createDomain(account);
    List<ApiRequest> requests = testFactory.createApiRequests_UserEvents(domain);
    List<UserEventGroup> groups = ManageUserEventsResource.toGroups(requests);

    UserEventGroupsModel model = new UserEventGroupsModel(account, domain, groups);

    Thymeleaf leaf = new Thymeleaf(ThymeleafViewFactory.MANAGE_API_EVENTS, model);
    msgBodyWriter.writeTo(leaf, writer);
    String content = writer.toString();
    assertNotNull(content);
  }
}
