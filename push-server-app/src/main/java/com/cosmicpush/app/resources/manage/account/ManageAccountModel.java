/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package com.cosmicpush.app.resources.manage.account;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.plugins.Plugin;
import com.cosmicpush.common.system.PluginManager;
import com.cosmicpush.pub.common.PushType;
import java.io.IOException;
import java.util.*;

public class ManageAccountModel {

  private final Account account;
  private final Set<PushType> pushTypes = new TreeSet<>();

  public ManageAccountModel(Account account) throws IOException {
    this.account = account;

    for (Plugin plugin : PluginManager.getPlugins()) {
      pushTypes.add(plugin.getPushType());
    }
  }

  public Account getAccount() {
    return account;
  }

  public Set<PushType> getPushType() {
    return pushTypes;
  }
}
