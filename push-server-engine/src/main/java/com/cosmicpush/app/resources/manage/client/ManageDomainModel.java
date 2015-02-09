/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.*;
import com.cosmicpush.common.system.PluginManager;
import java.io.IOException;
import java.util.*;

public class ManageDomainModel {

  private final Domain domain;
  private final String message;

  private final Set<PluginModel> plugins = new TreeSet<>();

  public ManageDomainModel(PluginContext context, Account account, Domain domain, String message) throws IOException {
    this.message = message;
    this.domain = domain;

    for (Plugin plugin : PluginManager.getPlugins()) {
      plugins.add(new PluginModel(context, plugin, account, domain));
    }
  }

  public Domain getDomain() {
    return domain;
  }

  public String getMessage() {
    return message;
  }

  public Set<PluginModel> getPlugins() {
    return plugins;
  }

}
