package com.cosmicpush.common.plugins;

import com.cosmicpush.common.AbstractDelegate;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.common.system.PluginManager;
import com.cosmicpush.pub.common.*;

public class PushProcessor {

  private final PluginContext context;

  public PushProcessor(PluginContext context) {
    this.context = context;
  }

  public PushResponse execute(Account account, ApiClient apiClient, Push push) {
    ApiRequest apiRequest = new ApiRequest(apiClient, push, context.getRemoteAddress());
    context.getApiRequestStore().create(apiRequest);

    Plugin plugin = PluginManager.getPlugin(push.getPushType());
    AbstractDelegate delegate = plugin.newDelegate(context, account, apiClient, apiRequest, push);

    delegate.start();

    return new PushResponse(
        account.getAccountId(),
        apiClient.getApiClientId(),
        apiRequest.getApiRequestId(),
        apiRequest.getCreatedAt(),
        apiRequest.getRequestStatus(),
        apiRequest.getNotes()
    );
  }
}
