/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.app.resources.manage.account;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.Plugin;
import com.cosmicpush.common.plugins.PluginConfig;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.system.PluginManager;
import com.cosmicpush.pub.common.PushType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ManageAccountModel {

  private final Account account;
  private final List<DomainModel> domains = new ArrayList<>();

  public ManageAccountModel(PluginContext pluginContext, Account account, List<Domain> domains) throws IOException {
    this.account = account;

    for (Domain domain : domains) {
      DomainModel domainModel = new DomainModel(domain.getDomainKey());
      this.domains.add(domainModel);

      for (Plugin plugin : PluginManager.getPlugins()) {
        PluginConfig config = plugin.getConfig(pluginContext.getCouchServer(), domain);
        if (config != null) {
          domainModel.pushTypes.add(plugin.getPushType());
        }
      }
    }
  }

  public Account getAccount() {
    return account;
  }

  public List<DomainModel> getDomains() {
    return domains;
  }

  public static class DomainModel {
    private final String domainKey;
    private final Set<PushType> pushTypes = new TreeSet<>();
    public DomainModel(String domainKey) {
      this.domainKey = domainKey;
    }
    public String getDomainKey() {
      return domainKey;
    }
    public Set<PushType> getPushTypes() {
      return pushTypes;
    }
  }
}
