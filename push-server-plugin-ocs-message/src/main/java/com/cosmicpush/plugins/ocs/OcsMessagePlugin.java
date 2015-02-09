package com.cosmicpush.plugins.ocs;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.Plugin;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.common.system.AppContext;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.push.OcsPush;
import org.crazyyak.dev.common.Formats;
import org.crazyyak.dev.common.IoUtils;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;

import static org.crazyyak.dev.common.StringUtils.isBlank;
import static org.crazyyak.dev.common.StringUtils.isNotBlank;
import static org.crazyyak.dev.common.StringUtils.nullToString;

public class OcsMessagePlugin implements Plugin {

  private OcsMessageConfigStore _configStore;

  public OcsMessagePlugin() {}

  public OcsMessageConfigStore getConfigStore(CpCouchServer couchServer) {
    if (_configStore == null) {
      _configStore = new OcsMessageConfigStore(couchServer);
    }
    return _configStore;
  }

  @Override
  public OcsMessageConfig getConfig(CpCouchServer couchServer, Domain domain) {
    String docId = OcsMessageConfigStore.toDocumentId(domain);
    return getConfigStore(couchServer).getByDocumentId(docId);
  }

  @Override
  public PushType getPushType() {
    return OcsPush.PUSH_TYPE;
  }

  @Override
  public OcsMessageDelegate newDelegate(PluginContext context, Account account, Domain domain, PushRequest pushRequest, Push push) {
    OcsMessageConfig config = getConfig(context.getCouchServer(), domain);
    return new OcsMessageDelegate(context, account, domain, pushRequest, (OcsPush)push, config);
  }

  @Override
  public void deleteConfig(PluginContext pluginContext, Account account, Domain domain) {

    OcsMessageConfig config = getConfig(pluginContext.getCouchServer(), domain);

    if (config != null) {
      getConfigStore(pluginContext.getCouchServer()).delete(config);
      pluginContext.setLastMessage("OCS (Office Communicator Server) configuration deleted.");
    } else {
      pluginContext.setLastMessage("OCS (Office Communicator Server) configuration doesn't exist.");
    }

    pluginContext.getAccountStore().update(account);
  }

  @Override
  public void updateConfig(PluginContext context, Account account, Domain domain, MultivaluedMap<String, String> formParams) {
    // do nothing...
  }

  @Override
  public void test(PluginContext pluginContext, Account account, Domain domain) throws Exception {

    OcsMessageConfig config = getConfig(pluginContext.getCouchServer(), domain);

    if (config == null) {
      String msg = "The OCS (Office Communicator Server) config has not been specified.";
      pluginContext.setLastMessage(msg);
      pluginContext.getAccountStore().update(account);
      return;
    }

    String recipient = config.getTestAddress();

    if (isBlank((recipient))) {
      String msg = "Test message cannot be sent with out specifying the test address.";
      pluginContext.setLastMessage(msg);
      pluginContext.getAccountStore().update(account);
      return;
    }

    String override = config.getRecipientOverride();
    if (isNotBlank(override)) {
      recipient = override;
    }

    String when = Formats.defaultStamp(new java.util.Date());
    String msg = String.format("This is a test message from Cosmic Push sent at %s.", when);
    OcsPush push = OcsPush.newPush(recipient, msg, null);

    PushRequest pushRequest = new PushRequest(AppContext.CURRENT_API_VERSION, domain, push);
    pluginContext.getPushRequestStore().create(pushRequest);

    new OcsMessageDelegate(pluginContext, account, domain, pushRequest, push, config).run();

    msg = String.format("Test message sent to %s:\n%s", recipient, msg);
    pluginContext.setLastMessage(msg);
    pluginContext.getAccountStore().update(account);
  }

  @Override
  public byte[] getIcon() throws IOException {
    InputStream stream = getClass().getResourceAsStream("/com/cosmicpush/plugins/ocs/message/icon.png");
    return IoUtils.toBytes(stream);
  }

  @Override
  public String getAdminUi(PluginContext context, Account account, Domain domain) throws IOException {

    OcsMessageConfig config = getConfig(context.getCouchServer(), domain);

    InputStream stream = getClass().getResourceAsStream("/com/cosmicpush/plugins/ocs/message/admin.html");
    String content = IoUtils.toString(stream);

    content = content.replace("${domain-name}",   nullToString(domain.getDomainKey()));
    content = content.replace("${push-server-base}",  nullToString(context.getBaseURI()));

    content = content.replace("${config-user-name}",  nullToString(config == null ? null : config.getUserName()));
    content = content.replace("${config-password}",   nullToString(config == null ? null : config.getPassword()));

    content = content.replace("${config-test-address}",       nullToString(config == null ? null : config.getTestAddress()));
    content = content.replace("${config-recipient-override}", nullToString(config == null ? null : config.getRecipientOverride()));

    if (content.contains("${")) {
      String msg = String.format("The OCS (Office Communicator Server) admin UI still contains un-parsed elements.");
      throw new IllegalStateException(msg);
    }

    return content;
  }
}
