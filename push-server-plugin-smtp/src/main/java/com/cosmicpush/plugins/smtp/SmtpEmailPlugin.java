package com.cosmicpush.plugins.smtp;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.Plugin;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.common.system.AppContext;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.push.SmtpEmailPush;
import org.crazyyak.dev.common.BeanUtils;
import org.crazyyak.dev.common.Formats;
import org.crazyyak.dev.common.IoUtils;
import org.crazyyak.dev.common.StringUtils;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;

import static org.crazyyak.dev.common.StringUtils.nullToString;

public class SmtpEmailPlugin implements Plugin {

  private SmtpEmailConfigStore _configStore;

  public SmtpEmailPlugin() {
  }

  public SmtpEmailConfigStore getConfigStore(CpCouchServer couchServer) {
    if (_configStore == null) {
      _configStore = new SmtpEmailConfigStore(couchServer);
    }
    return _configStore;
  }

  @Override
  public SmtpEmailConfig getConfig(CpCouchServer couchServer, Domain domain) {
    String docId = SmtpEmailConfigStore.toDocumentId(domain);
    return getConfigStore(couchServer).getByDocumentId(docId);
  }

  @Override
  public PushType getPushType() {
    return SmtpEmailPush.PUSH_TYPE;
  }

  @Override
  public SmtpEmailDelegate newDelegate(PluginContext pluginContext, Account account, Domain domain, PushRequest pushRequest, Push push) {
    SmtpEmailConfig config = getConfig(pluginContext.getCouchServer(), domain);
    return new SmtpEmailDelegate(pluginContext, account, domain, pushRequest, (SmtpEmailPush)push, config);
  }

  @Override
  public void updateConfig(PluginContext pluginContext, Account account, Domain domain, MultivaluedMap<String, String> formParams) {

    UpdateSmtpEmailConfigAction action = new UpdateSmtpEmailConfigAction(domain, formParams);

    SmtpEmailConfig smtpEmailConfig = getConfig(pluginContext.getCouchServer(), domain);
    if (smtpEmailConfig == null) {
      smtpEmailConfig = new SmtpEmailConfig();
    }

    smtpEmailConfig.apply(action);
    getConfigStore(pluginContext.getCouchServer()).update(smtpEmailConfig);

    pluginContext.setLastMessage("SES Email configuration updated.");
    pluginContext.getAccountStore().update(account);
  }

  @Override
  public void deleteConfig(PluginContext pluginContext, Account account, Domain domain) {

    SmtpEmailConfig config = getConfig(pluginContext.getCouchServer(), domain);

    if (config != null) {
      getConfigStore(pluginContext.getCouchServer()).delete(config);
      pluginContext.setLastMessage("SES email configuration deleted.");
    } else {
      pluginContext.setLastMessage("SES email configuration doesn't exist.");
    }

    pluginContext.getAccountStore().update(account);
  }

  @Override
  public void test(PluginContext pluginContext, Account account, Domain domain) throws Exception {

    SmtpEmailConfig config = getConfig(pluginContext.getCouchServer(), domain);

    if (config == null) {
      String msg = "The SMTP email config has not been specified.";
      pluginContext.setLastMessage(msg);
      pluginContext.getAccountStore().update(account);
      return;
    }

    String toAddress = config.getTestToAddress();
    String fromAddress = config.getTestFromAddress();

    if (StringUtils.isBlank((toAddress))) {
      String msg = "A test message cannot be sent with out specifying the config's test-to-address.";
      pluginContext.setLastMessage(msg);
      pluginContext.getAccountStore().update(account);
      return;
    }

    if (StringUtils.isBlank((fromAddress))) {
      String msg = "A test message cannot be sent with out specifying the config's test-from-address.";
      pluginContext.setLastMessage(msg);
      pluginContext.getAccountStore().update(account);
      return;
    }

    String override = config.getRecipientOverride();
    if (StringUtils.isNotBlank(override)) {
      toAddress = override;
    }

    String when = Formats.defaultStamp(new java.util.Date());
    String msg = String.format("<html><head><title>Some Email</title></head><body style='background-color:red'><div style='background-color:#c0c0ff'><h1>Testing 123</h1>This is a test message from Cosmic Push sent at %s.</div></body>", when);
    String subject = "SMTP Test message from Cosmic Push";
    SmtpEmailPush push = SmtpEmailPush.newPush(
        toAddress, fromAddress,
        subject, msg,
        null, BeanUtils.toMap("smtp-test:true"));

    PushRequest pushRequest = new PushRequest(AppContext.CURRENT_API_VERSION, domain, push);
    pluginContext.getPushRequestStore().create(pushRequest);

    new SmtpEmailDelegate(pluginContext, account, domain, pushRequest, push, config).run();

    msg = String.format("Test message sent from %s to %s", fromAddress, toAddress);
    pluginContext.setLastMessage(msg);
    pluginContext.getAccountStore().update(account);
  }

  @Override
  public byte[] getIcon() throws IOException {
    InputStream stream = getClass().getResourceAsStream("/com/cosmicpush/plugins/smtp/icon.png");
    return IoUtils.toBytes(stream);
  }

  @Override
  public String getAdminUi(PluginContext pluginContext, Account account, Domain domain) throws IOException {

    SmtpEmailConfig config = getConfig(pluginContext.getCouchServer(), domain);

    InputStream stream = getClass().getResourceAsStream("/com/cosmicpush/plugins/smtp/admin.html");
    String content = IoUtils.toString(stream);

    content = content.replace("${domain-name}",               nullToString(domain.getDomainKey()));
    content = content.replace("${push-server-base}",          nullToString(pluginContext.getBaseURI()));
    content = content.replace("${config-user-name}",          nullToString(config == null ? null : config.getUserName()));
    content = content.replace("${config-password}",           nullToString(config == null ? null : config.getPassword()));
    content = content.replace("${config-auth-type}",          nullToString(config == null ? null : config.getAuthType()));
    content = content.replace("${config-port-number}",        nullToString(config == null ? null : config.getPortNumber()));
    content = content.replace("${config-server-name}",        nullToString(config == null ? null : config.getServerName()));
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
