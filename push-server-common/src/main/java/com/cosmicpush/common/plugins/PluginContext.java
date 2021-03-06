package com.cosmicpush.common.plugins;

import com.cosmicpush.common.accounts.AccountStore;
import com.cosmicpush.common.accounts.DomainStore;
import com.cosmicpush.common.requests.PushRequestStore;
import com.cosmicpush.common.system.AppContext;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.jackson.CpObjectMapper;

import java.net.URI;

public interface PluginContext {

  PushRequestStore getPushRequestStore();
  DomainStore getDomainStore();

  CpObjectMapper getObjectMapper();

  CpCouchServer getCouchServer();

  PushProcessor getPushProcessor();

  URI getBaseURI();

  AppContext getAppContext();

  void setLastMessage(String message);
}
