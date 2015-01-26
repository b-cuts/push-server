package com.cosmicpush.plugins.gtalk;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.plugins.*;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.pub.common.*;
import com.cosmicpush.pub.push.*;
import java.io.*;
import javax.ws.rs.core.MultivaluedMap;
import org.crazyyak.dev.common.*;

import static org.crazyyak.dev.common.StringUtils.*;

public class GoogleTalkPlugin implements Plugin {

  private GoogleTalkConfigStore _configStore;

  public GoogleTalkPlugin() {
  }

  public GoogleTalkConfigStore getConfigStore(CpCouchServer couchServer) {
    if (_configStore == null) {
      _configStore = new GoogleTalkConfigStore(couchServer);
    }
    return _configStore;
  }

  @Override
  public GoogleTalkConfig getConfig(CpCouchServer couchServer, ApiClient apiClient) {
    String docId = GoogleTalkConfigStore.toDocumentId(apiClient);
    return getConfigStore(couchServer).getByDocumentId(docId);
  }

  @Override
  public PushType getPushType() {
    return GoogleTalkPush.PUSH_TYPE;
  }

  @Override
  public GoogleTalkDelegate newDelegate(PluginContext context, Account account, ApiClient apiClient, ApiRequest apiRequest, Push push) {
    GoogleTalkConfig config = getConfig(context.getCouchServer(), apiClient);
    return new GoogleTalkDelegate(context, account, apiClient, apiRequest, (GoogleTalkPush)push, config);
  }

  @Override
  public void deleteConfig(PluginContext context, Account account, ApiClient apiClient) {

    GoogleTalkConfig config = getConfig(context.getCouchServer(), apiClient);

    if (config != null) {
      getConfigStore(context.getCouchServer()).delete(config);
      apiClient.setLastMessage("Google Talk email configuration deleted.");
    } else {
      apiClient.setLastMessage("Google Talk email configuration doesn't exist.");
    }

    context.getAccountStore().update(account);
  }

  @Override
  public void updateConfig(PluginContext context, Account account, ApiClient apiClient, MultivaluedMap<String, String> formParams) {

    UpdateGoogleTalkConfigAction action = new UpdateGoogleTalkConfigAction(apiClient, formParams);

    GoogleTalkConfig googleTalkConfig = getConfig(context.getCouchServer(), apiClient);
    if (googleTalkConfig == null) {
      googleTalkConfig = new GoogleTalkConfig();
    }

    googleTalkConfig.apply(action);
    getConfigStore(context.getCouchServer()).update(googleTalkConfig);

    apiClient.setLastMessage("Google Talk configuration updated.");
    context.getAccountStore().update(account);
  }

  @Override
  public void test(PluginContext context, Account account, ApiClient apiClient) throws Exception {

    GoogleTalkConfig config = getConfig(context.getCouchServer(), apiClient);

    if (config == null) {
      String msg = "The Google Talk config has not been specified.";
      apiClient.setLastMessage(msg);
      context.getAccountStore().update(account);
      return;
    }

    String recipient = config.getTestAddress();

    if (isBlank((recipient))) {
      String msg = "Test message cannot be sent with out specifying the test address.";
      apiClient.setLastMessage(msg);
      context.getAccountStore().update(account);
      return;
    }

    String override = config.getRecipientOverride();
    if (isNotBlank(override)) {
      recipient = override;
    }

    String when = Formats.defaultStamp(new java.util.Date());
    String msg = String.format("This is a test message from Cosmic Push sent at %s.", when);
    GoogleTalkPush push = GoogleTalkPush.googleTalk(recipient, msg, null);

    ApiRequest apiRequest = new ApiRequest(apiClient, push, context.getRemoteAddress());
    context.getApiRequestStore().create(apiRequest);

    new GoogleTalkDelegate(context, account, apiClient, apiRequest, push, config).run();

    msg = String.format("Test message sent to %s:\n%s", recipient, msg);
    apiClient.setLastMessage(msg);
    context.getAccountStore().update(account);
  }

  @Override
  public byte[] getIcon() throws IOException {
    InputStream stream = getClass().getResourceAsStream("/com/cosmicpush/plugins/gtalk/icon.png");
    return IoUtils.toBytes(stream);
  }

  @Override
  public String getAdminUi(PluginContext context, Account account, ApiClient apiClient) throws IOException {

    GoogleTalkConfig config = getConfig(context.getCouchServer(), apiClient);

    InputStream stream = getClass().getResourceAsStream("/com/cosmicpush/plugins/gtalk/admin.html");
    String content = IoUtils.toString(stream);

    content = content.replace("${api-client-name}",   nullToString(apiClient.getClientName()));
    content = content.replace("${push-server-base}",  nullToString(context.getServerRoot()));

    content = content.replace("${config-user-name}",  nullToString(config == null ? null : config.getUserName()));
    content = content.replace("${config-password}",   nullToString(config == null ? null : config.getPassword()));

    content = content.replace("${config-test-address}",       nullToString(config == null ? null : config.getTestAddress()));
    content = content.replace("${config-recipient-override}", nullToString(config == null ? null : config.getRecipientOverride()));

    if (content.contains("${")) {
      String msg = String.format("The Google Talk admin UI still contains un-parsed elements.");
      throw new IllegalStateException(msg);
    }

    return content;
  }
}
