package com.cosmicpush.common.plugins;

import com.cosmicpush.common.accounts.AccountStore;
import com.cosmicpush.common.requests.ApiRequestStore;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.jackson.CpObjectMapper;

import java.net.InetAddress;
import java.net.URI;

public interface PluginContext {

  AccountStore getAccountStore();
  ApiRequestStore getApiRequestStore();

  CpObjectMapper getObjectMapper();

  CpCouchServer getCouchServer();

  PushProcessor getPushProcessor();

  InetAddress getRemoteAddress();

  URI getBaseURI();
}
