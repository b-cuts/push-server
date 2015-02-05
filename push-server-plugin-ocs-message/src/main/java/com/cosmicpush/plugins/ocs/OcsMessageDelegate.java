package com.cosmicpush.plugins.ocs;

import com.cosmicpush.common.AbstractDelegate;
import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.common.RequestStatus;
import com.cosmicpush.pub.push.OcsPush;
import org.crazyyak.dev.common.StringUtils;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;

public class OcsMessageDelegate extends AbstractDelegate {

  private final Account account;
  private final ApiClient apiClient;

  private final OcsPush push;
  private final OcsMessageConfig config;

  public OcsMessageDelegate(PluginContext context, Account account, ApiClient apiClient, ApiRequest apiRequest, OcsPush push, OcsMessageConfig config) {
    super(context.getObjectMapper(), apiRequest, context.getApiRequestStore());
    this.config = ExceptionUtils.assertNotNull(config, "config");
    this.push = ExceptionUtils.assertNotNull(push, "push");
    this.account = ExceptionUtils.assertNotNull(account, "account");
    this.apiClient = ExceptionUtils.assertNotNull(apiClient, "apiClient");
  }

  @Override
  public synchronized RequestStatus processRequest() throws Exception {
    String reasonNotPermitted = account.getReasonNotPermitted(push);
    if (StringUtils.isNotBlank(reasonNotPermitted)) {
      return apiRequest.denyRequest(reasonNotPermitted);
    }

    return apiRequest.failed("Not implemented.");
  }
}