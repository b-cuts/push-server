/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.plugins.*;
import com.cosmicpush.common.system.PluginManager;
import java.io.IOException;
import java.util.*;

public class ManageApiClientModel {

  private final ApiClient apiClient;
  private final String message;

  private final Set<PluginModel> plugins = new TreeSet<>();

  public ManageApiClientModel(PluginContext context, Account account, ApiClient apiClient, String message) throws IOException {
    this.message = message;
    this.apiClient = apiClient;

    for (Plugin plugin : PluginManager.getPlugins()) {
      plugins.add(new PluginModel(context, plugin, account, apiClient));
    }
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public String getMessage() {
    return message;
  }

  public Set<PluginModel> getPlugins() {
    return plugins;
  }

}
