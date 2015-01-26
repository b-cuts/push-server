package com.cosmicpush.common.plugins;

import com.cosmicpush.common.accounts.AccountStore;
import com.cosmicpush.common.requests.ApiRequestStore;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.jackson.CpObjectMapper;
import java.net.*;

public interface PluginContext {

  AccountStore getAccountStore();
  InetAddress getRemoteAddress();
  ApiRequestStore getApiRequestStore();

  CpObjectMapper getObjectMapper();

  CpCouchServer getCouchServer();

  String getServerRoot();

  PushProcessor getPushProcessor();

}
