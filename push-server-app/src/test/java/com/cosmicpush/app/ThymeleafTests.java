package com.cosmicpush.app;

import com.cosmicpush.TestFactory;
import com.cosmicpush.app.resources.RootResource;
import com.cosmicpush.app.resources.manage.account.ManageAccountModel;
import com.cosmicpush.app.resources.manage.client.ApiClientRequestsModel;
import com.cosmicpush.app.resources.manage.client.ManageApiClientModel;
import com.cosmicpush.app.resources.manage.client.emails.EmailModel;
import com.cosmicpush.app.resources.manage.client.emails.EmailsModel;
import com.cosmicpush.app.view.Thymeleaf;
import com.cosmicpush.app.view.ThymeleafMessageBodyWriter;
import com.cosmicpush.app.view.ThymeleafViewFactory;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.push.EmailPush;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.StringWriter;
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
    ApiClient apiClient = testFactory.createApiClient(account);
    List<ApiRequest> requests = testFactory.createApiRequests(apiClient);
    ApiClientRequestsModel model = new ApiClientRequestsModel(account, apiClient, requests);

    Thymeleaf leaf = new Thymeleaf(ThymeleafViewFactory.MANAGE_API_REQUESTS, model);
    msgBodyWriter.writeTo(leaf, writer);
    String content = writer.toString();
    assertNotNull(content);
  }

  public void testManageApiClient() throws Exception {

    Account account = testFactory.createAccount();
    ApiClient apiClient = testFactory.createApiClient(account);
    PluginContext pluginContext = testFactory.pluginContext(testFactory);
    ManageApiClientModel model = new ManageApiClientModel(pluginContext, account, apiClient, "This was the last message.");

    Thymeleaf leaf = new Thymeleaf(ThymeleafViewFactory.MANAGE_API_CLIENT, model);
    msgBodyWriter.writeTo(leaf, writer);
    String content = writer.toString();
    assertNotNull(content);
  }

  public void testManageAccount() throws Exception {

    Account account = testFactory.createAccount();
    ManageAccountModel model = new ManageAccountModel(account);

    Thymeleaf leaf = new Thymeleaf(ThymeleafViewFactory.MANAGE_ACCOUNT, model);
    msgBodyWriter.writeTo(leaf, writer);
    String content = writer.toString();
    assertNotNull(content);
  }

  public void testManageApiEmails() throws Exception {

    Account account = testFactory.createAccount();
    ApiClient apiClient = testFactory.createApiClient(account);
    List<ApiRequest> requests = testFactory.createApiRequests_Emails(apiClient);
    EmailsModel model = new EmailsModel(account, apiClient, requests);

    Thymeleaf leaf = new Thymeleaf(ThymeleafViewFactory.MANAGE_API_EMAILS, model);
    msgBodyWriter.writeTo(leaf, writer);
    String content = writer.toString();
    assertNotNull(content);
  }

  public void testManageApiEmail() throws Exception {

    Account account = testFactory.createAccount();
    ApiClient apiClient = testFactory.createApiClient(account);
    ApiRequest request = testFactory.createApiRequests_Emails(apiClient).get(0);
    EmailPush email = request.getEmailPush();
    EmailModel model = new EmailModel(account, apiClient, request, email);

    Thymeleaf leaf = new Thymeleaf(ThymeleafViewFactory.MANAGE_API_EMAIL, model);
    msgBodyWriter.writeTo(leaf, writer);
    String content = writer.toString();
    assertNotNull(content);
  }
}
