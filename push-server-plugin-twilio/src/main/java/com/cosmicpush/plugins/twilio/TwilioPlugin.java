/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.twilio;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.Plugin;
import com.cosmicpush.common.plugins.PluginContext;
import com.cosmicpush.common.requests.PushRequest;
import com.cosmicpush.common.system.CpCouchServer;
import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.common.PushType;
import com.cosmicpush.pub.push.TwilioSmsPush;
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
    public TwilioConfig getConfig(CpCouchServer couchServer, Domain apiClient) {
        String docId = TwilioConfigStore.toDocumentId(apiClient);
        return getConfigStore(couchServer).getByDocumentId(docId);
    }

    @Override
    public PushType getPushType() {
        return TwilioSmsPush.PUSH_TYPE;
    }

    @Override
    public TwilioDelegate newDelegate(PluginContext context, Account account, Domain domain, PushRequest pushRequest, Push push) {
        TwilioConfig config = getConfig(context.getCouchServer(), domain);
        return new TwilioDelegate(context, account, domain, pushRequest, (TwilioSmsPush)push, config);
    }

    @Override
    public void deleteConfig(PluginContext context, Account account, Domain domain) {

        TwilioConfig config = getConfig(context.getCouchServer(), domain);

        if (config != null) {
            getConfigStore(context.getCouchServer()).delete(config);
            context.setLastMessage("Twilio SMS configuration deleted.");
        } else {
            context.setLastMessage("Twilio SMS configuration doesn't exist.");
        }

        context.getAccountStore().update(account);
    }

    @Override
    public void updateConfig(PluginContext context, Account account, Domain domain, MultivaluedMap<String, String> formParams) {
        UpdateTwilioConfigAction action = new UpdateTwilioConfigAction(domain, formParams);

        TwilioConfig twilioConfig = getConfig(context.getCouchServer(), domain);
        if (twilioConfig == null) {
            twilioConfig = new TwilioConfig();
        }

        twilioConfig.apply(action);
        getConfigStore(context.getCouchServer()).update(twilioConfig);

        context.setLastMessage("Twilio configuration updated.");
        context.getAccountStore().update(account);
    }

    @Override
    public void test(PluginContext context, Account account, Domain domain) throws Exception {

        TwilioConfig config = getConfig(context.getCouchServer(), domain);

        if (config == null) {
            String msg = "The Twilio config has not been specified.";
            context.setLastMessage(msg);
            context.getAccountStore().update(account);
            return;
        }

        String ACCOUNT_SID = config.getAccountSid();
        String AUTH_TOKEN = config.getAuthToken();

        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        // Build a filter for the MessageList
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Body", config.getMessage()));
        params.add(new BasicNameValuePair("To", config.getRecipient()));
        params.add(new BasicNameValuePair("From", config.getFromPhoneNumber()));
        MessageFactory messageFactory = client.getAccount().getMessageFactory();
        messageFactory.create(params);
    }

    @Override
    public byte[] getIcon() throws IOException {
        InputStream stream = getClass().getResourceAsStream("/com/cosmicpush/plugins/gtalk/icon.png");
        return IoUtils.toBytes(stream);
    }

    @Override
    public String getAdminUi(PluginContext context, Account account, Domain domain) throws IOException {

        TwilioConfig config = getConfig(context.getCouchServer(), domain);

        InputStream stream = getClass().getResourceAsStream("/com/cosmicpush/plugins/twilio/admin.html");
        String content = IoUtils.toString(stream);

        content = content.replace("${domain-name}",   nullToString(domain.getDomainKey()));
        content = content.replace("${push-server-base}",  nullToString(context.getBaseURI()));

        content = content.replace("${config-account-sid}",  nullToString(config == null ? null : config.getAccountSid()));
        content = content.replace("${config-auth-token}",  nullToString(config == null ? null : config.getAuthToken()));
        content = content.replace("${config-from-number}",  nullToString(config == null ? null : config.getFromPhoneNumber()));
        content = content.replace("${config-recipient}",  nullToString(config == null ? null : config.getRecipient()));
        content = content.replace("${config-message}",  nullToString(config == null ? null : config.getMessage()));

        if (content.contains("${")) {
            String msg = String.format("The Twilio admin UI still contains un-parsed elements.");
            throw new IllegalStateException(msg);
        }

        return content;
    }
}
