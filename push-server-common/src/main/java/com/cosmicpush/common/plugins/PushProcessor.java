package com.cosmicpush.common.plugins;

import com.cosmicpush.common.AbstractDelegate;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.common.system.PluginManager;
import com.cosmicpush.pub.common.*;

public class PushProcessor {

  private final PluginContext context;

  public PushProcessor(PluginContext context) {
    this.context = context;
  }

  public PushResponse execute(int apiVersion, Account account, Domain domain, Push push) {

    // TODO - validate the remoteHost and remoteAddress specifeid in the push as really coming from them.

    ApiRequest apiRequest = new ApiRequest(apiVersion, domain, push);
    context.getApiRequestStore().create(apiRequest);

    Plugin plugin = PluginManager.getPlugin(push.getPushType());
    AbstractDelegate delegate = plugin.newDelegate(context, account, domain, apiRequest, push);

    delegate.start();

    return new PushResponse(
        account.getAccountId(),
        domain.getDomainId(),
        apiRequest.getApiRequestId(),
        apiRequest.getCreatedAt(),
        apiRequest.getRequestStatus(),
        apiRequest.getNotes()
    );
  }
}
