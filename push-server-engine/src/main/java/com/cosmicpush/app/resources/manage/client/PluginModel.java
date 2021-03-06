package com.cosmicpush.app.resources.manage.client;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.*;
import com.cosmicpush.pub.common.PushType;
import java.io.IOException;

public class PluginModel implements Comparable<PluginModel>{

  private final String htmlContent;
  private final PushType pushType;

  public PluginModel(PluginContext context, Plugin plugin, Account account, Domain domain) throws IOException {
    this.pushType = plugin.getPushType();
    this.htmlContent = plugin.getAdminUi(context, domain);
  }

  public PushType getPushType() {
    return pushType;
  }

  public String getHtmlContent() {
    return htmlContent;
  }

  @Override
  public int compareTo(PluginModel that) {
    return this.pushType.compareTo(that.pushType);
  }
}
