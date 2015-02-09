package com.cosmicpush.app.resources.manage.client.twilio;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.pub.push.TwilioPush;

public class ApiClientTwilioModel {
    private final Account account;
    private final ApiClient apiClient;

    private final ApiRequest request;
    private final TwilioPush twilioPush;

    public ApiClientTwilioModel(Account account, ApiClient apiClient, ApiRequest request, TwilioPush twilioPush) {
        this.account = account;
        this.apiClient = apiClient;
        this.request = request;
        this.twilioPush = twilioPush;
    }

    public Account getAccount() {
        return account;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public ApiRequest getRequest() {
        return request;
    }

    public TwilioPush getTwilioPush() {
        return twilioPush;
    }
}
