package com.cosmicpush.plugins.notifier;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.Plugin;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.push.GoogleTalkPush;
import org.crazyyak.dev.common.IoUtils;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;

import static org.crazyyak.dev.common.StringUtils.nullToString;

public class NotifierPlugin implements Plugin {

  private NotifierConfigStore _configStore;

  public NotifierPlugin() {
  }

  public NotifierConfigStore getConfigStore(CpCouchServer couchServer) {
    if (_configStore == null) {
      _configStore = new NotifierConfigStore(couchServer);
    }
    return _configStore;
  }

  @Override
  public NotifierConfig getConfig(CpCouchServer couchServer, Domain domain) {
    String docId = NotifierConfigStore.toDocumentId(domain);
    return getConfigStore(couchServer).getByDocumentId(docId);
  }

  @Override
  public PushType getPushType() {
    return GoogleTalkPush.PUSH_TYPE;
  }

  @Override
  public NotifierDelegate newDelegate(PluginContext context, Account account, Domain domain, ApiRequest apiRequest, Push push) {
    NotifierConfig config = getConfig(context.getCouchServer(), domain);
    return new NotifierDelegate(context, account, domain, apiRequest, (GoogleTalkPush)push, config);
  }

  @Override
  public void deleteConfig(PluginContext pluginContext, Account account, Domain domain) {

    NotifierConfig config = getConfig(pluginContext.getCouchServer(), domain);

    if (config != null) {
      getConfigStore(pluginContext.getCouchServer()).delete(config);
      pluginContext.setLastMessage("Google Talk email configuration deleted.");
    } else {
      pluginContext.setLastMessage("Google Talk email configuration doesn't exist.");
    }

    pluginContext.getAccountStore().update(account);
  }

  @Override
  public void updateConfig(PluginContext context, Account account, Domain domain, MultivaluedMap<String, String> formParams) {
    // do nothing...
  }

  @Override
  public void test(PluginContext context, Account account, Domain domain) throws Exception {
//
//    NotifierConfig config = getConfig(context.getCouchServer(), domain);
//
//    if (config == null) {
//      String msg = "The Google Talk config has not been specified.";
//      domain.setLastMessage(msg);
//      context.getAccountStore().update(account);
//      return;
//    }
//
//    String recipient = config.getTestAddress();
//
//    if (isBlank((recipient))) {
//      String msg = "Test message cannot be sent with out specifying the test address.";
//      domain.setLastMessage(msg);
//      context.getAccountStore().update(account);
//      return;
//    }
//
//    String override = config.getRecipientOverride();
//    if (isNotBlank(override)) {
//      recipient = override;
//    }
//
//    String when = Formats.defaultStamp(new java.util.Date());
//    String msg = String.format("This is a test message from Cosmic Push sent at %s.", when);
//    GoogleTalkPush push = GoogleTalkPush.newPush(recipient, msg, null, InetAddress.getLocalHost());
//
//    ApiRequest apiRequest = new ApiRequest(domain, push);
//    context.getApiRequestStore().create(apiRequest);
//
//    new NotifierDelegate(context, account, domain, apiRequest, push, config).run();
//
//    msg = String.format("Test message sent to %s:\n%s", recipient, msg);
//    domain.setLastMessage(msg);
//    context.getAccountStore().update(account);
  }

  @Override
  public byte[] getIcon() throws IOException {
    InputStream stream = getClass().getResourceAsStream("/com/cosmicpush/plugins/gtalk/icon.png");
    return IoUtils.toBytes(stream);
  }

  @Override
  public String getAdminUi(PluginContext context, Account account, Domain domain) throws IOException {

    NotifierConfig config = getConfig(context.getCouchServer(), domain);

    InputStream stream = getClass().getResourceAsStream("/com/cosmicpush/plugins/gtalk/admin.html");
    String content = IoUtils.toString(stream);

    content = content.replace("${domain-name}",   nullToString(domain.getDomainKey()));
    content = content.replace("${push-server-base}",  nullToString(context.getBaseURI()));

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
