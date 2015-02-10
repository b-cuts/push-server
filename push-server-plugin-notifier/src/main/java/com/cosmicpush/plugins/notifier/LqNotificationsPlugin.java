package com.cosmicpush.plugins.notifier;

import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.Plugin;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.push.LqNotificationPush;
import org.crazyyak.dev.common.IoUtils;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;

import static org.crazyyak.dev.common.StringUtils.nullToString;

public class LqNotificationsPlugin implements Plugin {

  private LqNotificationsConfigStore _configStore;

  public LqNotificationsPlugin() {
    System.out.print("");
  }

  public LqNotificationsConfigStore getConfigStore(CpCouchServer couchServer) {
    if (_configStore == null) {
      _configStore = new LqNotificationsConfigStore(couchServer);
    }
    return _configStore;
  }

  @Override
  public LqNotificationsConfig getConfig(CpCouchServer couchServer, Domain domain) {
    String docId = LqNotificationsConfigStore.toDocumentId(domain);
    return getConfigStore(couchServer).getByDocumentId(docId);
  }

  @Override
  public PushType getPushType() {
    return LqNotificationPush.PUSH_TYPE;
  }

  @Override
  public LqNotificationsDelegate newDelegate(PluginContext context, Domain domain, PushRequest pushRequest, Push push) {
    LqNotificationsConfig config = getConfig(context.getCouchServer(), domain);
    return new LqNotificationsDelegate(context, domain, pushRequest, (LqNotificationPush)push, config);
  }

  @Override
  public void deleteConfig(PluginContext pluginContext, Domain domain) {

    LqNotificationsConfig config = getConfig(pluginContext.getCouchServer(), domain);

    if (config != null) {
      getConfigStore(pluginContext.getCouchServer()).delete(config);
      pluginContext.setLastMessage("Google Talk email configuration deleted.");
    } else {
      pluginContext.setLastMessage("Google Talk email configuration doesn't exist.");
    }
  }

  @Override
  public void updateConfig(PluginContext pluginContext, Domain domain, MultivaluedMap<String, String> formParams) {

    UpdateLqNotificationsConfigAction action = new UpdateLqNotificationsConfigAction(domain, formParams);

    LqNotificationsConfig config = getConfig(pluginContext.getCouchServer(), domain);
    if (config == null) {
      config = new LqNotificationsConfig();
    }

    config.apply(action);
    getConfigStore(pluginContext.getCouchServer()).update(config);

    pluginContext.setLastMessage("Google Talk configuration updated.");
  }

  @Override
  public void test(PluginContext context, Domain domain) throws Exception {
  }

  @Override
  public byte[] getIcon() throws IOException {
    InputStream stream = getClass().getResourceAsStream("/com/cosmicpush/plugins/notifier/icon.png");
    return IoUtils.toBytes(stream);
  }

  @Override
  public String getAdminUi(PluginContext context, Domain domain) throws IOException {

    LqNotificationsConfig config = getConfig(context.getCouchServer(), domain);

    InputStream stream = getClass().getResourceAsStream("/com/cosmicpush/plugins/notifier/admin.html");
    String content = IoUtils.toString(stream);

    content = content.replace("${domain-name}",   nullToString(domain.getDomainKey()));
    content = content.replace("${push-server-base}",  nullToString(context.getBaseURI()));

    content = content.replace("${config-user-name}",  nullToString(config == null ? null : config.getUserName()));

    if (content.contains("${")) {
      String msg = String.format("The Google Talk admin UI still contains un-parsed elements.");
      throw new IllegalStateException(msg);
    }

    return content;
  }
}
