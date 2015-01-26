package com.cosmicpush.common.plugins;

import com.cosmicpush.common.AbstractDelegate;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.pub.common.*;
import java.io.IOException;
import javax.ws.rs.core.MultivaluedMap;

public interface Plugin {

  PushType getPushType();
  PluginConfig getConfig(CpCouchServer couchServer, ApiClient apiClient);

  byte[] getIcon() throws IOException;
  String getAdminUi(PluginContext context, Account account, ApiClient apiClient) throws IOException;

  AbstractDelegate newDelegate(PluginContext context, Account account, ApiClient apiClient, ApiRequest apiRequest, Push push);
  void test(PluginContext context, Account account, ApiClient apiClient) throws Exception;
  void updateConfig(PluginContext context, Account account, ApiClient apiClient, MultivaluedMap<String, String> formParams);
  void deleteConfig(PluginContext context, Account account, ApiClient apiClient);
}
