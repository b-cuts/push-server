package com.cosmicpush.plugins.ses;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.plugins.Plugin;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.push.SesEmailPush;
import org.crazyyak.dev.common.BeanUtils;
import org.crazyyak.dev.common.Formats;
import org.crazyyak.dev.common.IoUtils;
import org.crazyyak.dev.common.StringUtils;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;

import static org.crazyyak.dev.common.StringUtils.nullToString;

public class SesEmailPlugin implements Plugin {

  private SesEmailConfigStore _configStore;

  public SesEmailPlugin() {
  }

  public SesEmailConfigStore getConfigStore(CpCouchServer couchServer) {
    if (_configStore == null) {
      _configStore = new SesEmailConfigStore(couchServer);
    }
    return _configStore;
  }

  @Override
  public SesEmailConfig getConfig(CpCouchServer couchServer, ApiClient apiClient) {
    String docId = SesEmailConfigStore.toDocumentId(apiClient);
    return getConfigStore(couchServer).getByDocumentId(docId);
  }

  @Override
  public PushType getPushType() {
    return SesEmailPush.PUSH_TYPE;
  }

  @Override
  public SesEmailDelegate newDelegate(PluginContext context, Account account, ApiClient apiClient, ApiRequest apiRequest, Push push) {
    SesEmailConfig config = getConfig(context.getCouchServer(), apiClient);
    return new SesEmailDelegate(context, account, apiClient, apiRequest, (SesEmailPush)push, config);
  }

  @Override
  public void deleteConfig(PluginContext context, Account account, ApiClient apiClient) {

    SesEmailConfig config = getConfig(context.getCouchServer(), apiClient);

    if (config != null) {
      getConfigStore(context.getCouchServer()).delete(config);
      apiClient.setLastMessage("SES email configuration deleted.");
    } else {
      apiClient.setLastMessage("SES email configuration doesn't exist.");
    }

    context.getAccountStore().update(account);
  }

  @Override
  public void updateConfig(PluginContext context, Account account, ApiClient apiClient, MultivaluedMap<String, String> formParams) {

    UpdateSesEmailConfigAction action = new UpdateSesEmailConfigAction(apiClient, formParams);

    SesEmailConfig sesEmailConfig = getConfig(context.getCouchServer(), apiClient);
    if (sesEmailConfig == null) {
      sesEmailConfig = new SesEmailConfig();
    }

    sesEmailConfig.apply(action);
    getConfigStore(context.getCouchServer()).update(sesEmailConfig);

    apiClient.setLastMessage("SES Email configuration updated.");
    context.getAccountStore().update(account);
  }

  @Override
  public void test(PluginContext context, Account account, ApiClient apiClient) throws Exception {

    SesEmailConfig config = getConfig(context.getCouchServer(), apiClient);

    if (config == null) {
      String msg = "The SES Email config has not been specified.";
      apiClient.setLastMessage(msg);
      context.getAccountStore().update(account);
      return;
    }

    String toAddress = config.getTestToAddress();
    String fromAddress = config.getTestFromAddress();

    if (StringUtils.isBlank((toAddress))) {
      String msg = "A test message cannot be sent with out specifying the config's test-to-address.";
      apiClient.setLastMessage(msg);
      context.getAccountStore().update(account);
      return;
    }

    if (StringUtils.isBlank((fromAddress))) {
      String msg = "A test message cannot be sent with out specifying the config's test-from-address.";
      apiClient.setLastMessage(msg);
      context.getAccountStore().update(account);
      return;
    }

    String override = config.getRecipientOverride();
    if (StringUtils.isNotBlank(override)) {
      toAddress = override;
    }

    String when = Formats.defaultStamp(new java.util.Date());
    String msg = String.format("<html><head><title>Some Email</title></head><body style='background-color:red'><div style='background-color:#c0c0ff'><h1>Testing 123</h1>This is a test message from Cosmic Push sent at %s.</div></body>", when);
    String subject = "ASES test message from Cosmic Push";
    SesEmailPush push = SesEmailPush.newPush(toAddress, fromAddress, subject, msg, null, BeanUtils.toMap("ases-test:true"));

    ApiRequest apiRequest = new ApiRequest(apiClient, push);
    context.getApiRequestStore().create(apiRequest);

    new SesEmailDelegate(context, account, apiClient, apiRequest, push, config).run();

    msg = String.format("Test message sent from %s to %s", fromAddress, toAddress);
    apiClient.setLastMessage(msg);
    context.getAccountStore().update(account);
  }

  @Override
  public byte[] getIcon() throws IOException {
    InputStream stream = getClass().getResourceAsStream("/com/cosmicpush/plugins/ses/icon.png");
    return IoUtils.toBytes(stream);
  }

  @Override
  public String getAdminUi(PluginContext context, Account account, ApiClient apiClient) throws IOException {

    SesEmailConfig config = getConfig(context.getCouchServer(), apiClient);

    InputStream stream = getClass().getResourceAsStream("/com/cosmicpush/plugins/ses/admin.html");
    String content = IoUtils.toString(stream);

    content = content.replace("${api-client-name}",           nullToString(apiClient.getClientName()));
    content = content.replace("${push-server-base}",          nullToString(context.getBaseURI()));
    content = content.replace("${config-access-key-id}",      nullToString(config == null ? null : config.getAccessKeyId()));
    content = content.replace("${config-secret-key}",         nullToString(config == null ? null : config.getSecretKey()));
    content = content.replace("${config-test-to-address}",    nullToString(config == null ? null : config.getTestToAddress()));
    content = content.replace("${config-test-from-address}",  nullToString(config == null ? null : config.getTestFromAddress()));
    content = content.replace("${config-recipient-override}", nullToString(config == null ? null : config.getRecipientOverride()));

    if (content.contains("${")) {
      String msg = String.format("The SES admin UI still contains un-parsed elements.");
      throw new IllegalStateException(msg);
    }

    return content;
  }
}
