/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.twilio;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.plugins.Plugin;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.ApiRequest;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.push.TwilioPush;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.factory.MessageFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.crazyyak.dev.common.IoUtils;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.crazyyak.dev.common.StringUtils.nullToString;

public class TwilioPlugin implements Plugin {

    private TwilioConfigStore _configStore;

    public TwilioPlugin() {
    }

    public TwilioConfigStore getConfigStore(CpCouchServer couchServer) {
        if (_configStore == null) {
            _configStore = new TwilioConfigStore(couchServer);
        }
        return _configStore;
    }

    @Override
    public TwilioConfig getConfig(CpCouchServer couchServer, ApiClient apiClient) {
        String docId = TwilioConfigStore.toDocumentId(apiClient);
        return getConfigStore(couchServer).getByDocumentId(docId);
    }

    @Override
    public PushType getPushType() {
        return TwilioPush.PUSH_TYPE;
    }

    @Override
    public TwilioDelegate newDelegate(PluginContext context, Account account, ApiClient apiClient, ApiRequest apiRequest, Push push) {
        TwilioConfig config = getConfig(context.getCouchServer(), apiClient);
        return new TwilioDelegate(context, account, apiClient, apiRequest, (TwilioPush)push, config);
    }

    @Override
    public void deleteConfig(PluginContext context, Account account, ApiClient apiClient) {

        TwilioConfig config = getConfig(context.getCouchServer(), apiClient);

        if (config != null) {
            getConfigStore(context.getCouchServer()).delete(config);
            apiClient.setLastMessage("Google Talk email configuration deleted.");
        } else {
            apiClient.setLastMessage("Google Talk email configuration doesn't exist.");
        }

        context.getAccountStore().update(account);
    }

    @Override
    public void updateConfig(PluginContext context, Account account, ApiClient apiClient, MultivaluedMap<String, String> formParams) {
        // do nothing...
    }

    @Override
    public void test(PluginContext context, Account account, ApiClient apiClient) throws Exception {
        String ACCOUNT_SID = "AC1584175437f3ba4d8697150c90334d9b";
        String AUTH_TOKEN = "0185f31b1ab051af661acd2d9a7d384f";

        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        // Build a filter for the MessageList
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Body", "Hello? Is anybody out there?"));
        params.add(new BasicNameValuePair("To", "+15592886686"));
        params.add(new BasicNameValuePair("From", "+15596440005"));
        MessageFactory messageFactory = client.getAccount().getMessageFactory();
        messageFactory.create(params);
    }

    @Override
    public byte[] getIcon() throws IOException {
        InputStream stream = getClass().getResourceAsStream("/com/cosmicpush/plugins/gtalk/icon.png");
        return IoUtils.toBytes(stream);
    }

    @Override
    public String getAdminUi(PluginContext context, Account account, ApiClient apiClient) throws IOException {

        TwilioConfig config = getConfig(context.getCouchServer(), apiClient);

        InputStream stream = getClass().getResourceAsStream("/com/cosmicpush/plugins/gtalk/admin.html");
        String content = IoUtils.toString(stream);

        content = content.replace("${api-client-name}",   nullToString(apiClient.getClientName()));
        content = content.replace("${push-server-base}",  nullToString(context.getBaseURI()));

        content = content.replace("${config-user-name}",  nullToString(config == null ? null : config.getUserName()));
        content = content.replace("${config-password}",   nullToString(config == null ? null : config.getPassword()));

        content = content.replace("${config-test-address}",       nullToString(config == null ? null : config.getTestAddress()));
        content = content.replace("${config-recipient-override}", nullToString(config == null ? null : config.getRecipientOverride()));

        if (content.contains("${")) {
            String msg = String.format("The Twilio admin UI still contains un-parsed elements.");
            throw new IllegalStateException(msg);
        }

        return content;
    }
}
