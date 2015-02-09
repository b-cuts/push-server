package com.cosmicpush.common.plugins;

import com.cosmicpush.common.AbstractDelegate;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.common.PushType;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

public interface Plugin {

  PushType getPushType();
  PluginConfig getConfig(CpCouchServer couchServer, Domain domain);

  byte[] getIcon() throws IOException;
  String getAdminUi(PluginContext context, Account account, Domain domain) throws IOException;

  AbstractDelegate newDelegate(PluginContext context, Account account, Domain domain, PushRequest pushRequest, Push push);
  void test(PluginContext context, Account account, Domain domain) throws Exception;
  void updateConfig(PluginContext context, Account account, Domain domain, MultivaluedMap<String, String> formParams);
  void deleteConfig(PluginContext context, Account account, Domain domain);
}
