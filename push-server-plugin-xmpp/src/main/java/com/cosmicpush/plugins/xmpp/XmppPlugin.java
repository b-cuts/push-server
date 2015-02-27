package com.cosmicpush.plugins.xmpp;

import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.plugins.PluginSupport;
import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.common.system.AppContext;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.push.XmppPush;
import org.crazyyak.dev.common.Formats;
import org.crazyyak.dev.common.IoUtils;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;

import static org.crazyyak.dev.common.StringUtils.*;

public class XmppPlugin extends PluginSupport {

  private XmppConfigStore _configStore;

  public XmppPlugin() {
    super(XmppPush.PUSH_TYPE);
  }

  public XmppConfigStore getConfigStore(CpCouchServer couchServer) {
    if (_configStore == null) {
      _configStore = new XmppConfigStore(couchServer);
    }
    return _configStore;
  }

  @Override
  public XmppConfig getConfig(CpCouchServer couchServer, Domain domain) {
    String docId = XmppConfigStore.toDocumentId(domain);
    return getConfigStore(couchServer).getByDocumentId(docId);
  }

  @Override
  public XmppDelegate newDelegate(PluginContext context, Domain domain, PushRequest pushRequest, Push push) {
    XmppConfig config = getConfig(context.getCouchServer(), domain);
    return new XmppDelegate(context, domain, pushRequest, (XmppPush)push, config);
  }

  @Override
  public void deleteConfig(PluginContext pluginContext, Domain domain) {

    XmppConfig config = getConfig(pluginContext.getCouchServer(), domain);

    if (config != null) {
      getConfigStore(pluginContext.getCouchServer()).delete(config);
      pluginContext.setLastMessage("XMPP configuration deleted.");
    } else {
      pluginContext.setLastMessage("XMPP configuration doesn't exist.");
    }
  }

  @Override
  public void updateConfig(PluginContext pluginContext, Domain domain, MultivaluedMap<String, String> formParams) {

    UpdateXmppConfigAction action = new UpdateXmppConfigAction(domain, formParams);

    XmppConfig xmppConfig = getConfig(pluginContext.getCouchServer(), domain);
    if (xmppConfig == null) {
      xmppConfig = new XmppConfig();
    }

    xmppConfig.apply(action);
    getConfigStore(pluginContext.getCouchServer()).update(xmppConfig);

    pluginContext.setLastMessage("XMPP configuration updated.");
  }

  @Override
  public void test(PluginContext pluginContext, Domain domain) throws Exception {

    XmppConfig config = getConfig(pluginContext.getCouchServer(), domain);

    if (config == null) {
      String msg = "The XMPP config has not been specified.";
      pluginContext.setLastMessage(msg);
      return;
    }

    String recipient = config.getTestAddress();

    if (isBlank((recipient))) {
      String msg = "Test message cannot be sent with out specifying the test address.";
      pluginContext.setLastMessage(msg);
      return;
    }

    String override = config.getRecipientOverride();
    if (isNotBlank(override)) {
      recipient = override;
    }

    String when = Formats.defaultStamp(new java.util.Date());
    String msg = String.format("This is a test message from Cosmic Push sent at %s.", when);
    XmppPush push = XmppPush.newPush(recipient, msg, null, "xmpp-test:true");

    PushRequest pushRequest = new PushRequest(AppContext.CURRENT_API_VERSION, domain, push);
    pluginContext.getPushRequestStore().create(pushRequest);

    new XmppDelegate(pluginContext, domain, pushRequest, push, config).run();

    msg = String.format("Test message sent to %s:\n%s", recipient, msg);
    pluginContext.setLastMessage(msg);
  }

  @Override
  public String getAdminUi(PluginContext context, Domain domain) throws IOException {

    XmppConfig config = getConfig(context.getCouchServer(), domain);

    InputStream stream = getClass().getResourceAsStream("/com/cosmicpush/plugins/xmpp/admin.html");
    String content = IoUtils.toString(stream);

    content = content.replace("${legend-class}",              nullToString(config == null ? "no-config" : ""));
    content = content.replace("${push-type}",                 nullToString(getPushType().getCode()));
    content = content.replace("${plugin-name}",               nullToString(getPluginName()));
    content = content.replace("${domain-key}",                nullToString(domain.getDomainKey()));
    content = content.replace("${push-server-base}",          nullToString(context.getBaseURI()));

    content = content.replace("${config-user-name}",          nullToString(config == null ? null : config.getUsername()));
    content = content.replace("${config-password}",           nullToString(config == null ? null : config.getPassword()));

    content = content.replace("${config-host}",               nullToString(config == null ? null : config.getHost()));
    content = content.replace("${config-port}",               nullToString(config == null ? null : config.getPort()));
    content = content.replace("${config-service-name}",       nullToString(config == null ? null : config.getServiceName()));


    content = content.replace("${config-test-address}",       nullToString(config == null ? null : config.getTestAddress()));
    content = content.replace("${config-recipient-override}", nullToString(config == null ? null : config.getRecipientOverride()));

    if (content.contains("${")) {
      String msg = String.format("The XMPP admin UI still contains un-parsed elements.");
      throw new IllegalStateException(msg);
    }

    return content;
  }
}
