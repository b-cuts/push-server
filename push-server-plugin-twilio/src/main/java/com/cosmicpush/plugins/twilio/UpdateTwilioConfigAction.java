package com.cosmicpush.plugins.twilio;

import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.pub.internal.RequestErrors;
import com.cosmicpush.pub.internal.ValidatableAction;
import com.cosmicpush.pub.internal.ValidationUtils;

import javax.ws.rs.core.MultivaluedMap;

public class UpdateTwilioConfigAction implements ValidatableAction {

    private final Domain domain;
    private final String accountSid;
    private final String authToken;
    private final String fromPhoneNumber;
    private final String recipient;

    public UpdateTwilioConfigAction(Domain domain, MultivaluedMap<String, String> formParams) {

        this.domain = domain;

        this.accountSid = formParams.getFirst("accountSid");
        this.authToken = formParams.getFirst("authToken");

        this.fromPhoneNumber = formParams.getFirst("fromPhoneNumber");
        this.recipient = formParams.getFirst("recipient");
    }

    public UpdateTwilioConfigAction(Domain domain, String accountSid, String authToken, String fromPhoneNumber, String recipient) {
        this.domain = domain;
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.fromPhoneNumber = fromPhoneNumber;
        this.recipient = recipient;
    }


    @Override
    public RequestErrors validate(RequestErrors errors) {
        ValidationUtils.requireValue(errors, accountSid, "The Twilio account SID must be specified.");
        ValidationUtils.requireValue(errors, authToken, "The Twilio Authentication Token must be specified.");

        ValidationUtils.requireValue(errors, fromPhoneNumber, "The Twilio Originating Phone Number must be specified.");
        ValidationUtils.requireValue(errors, recipient, "The Twilio SMS Recipient must be specified.");
        return errors;
    }


    public Domain getDomain() {
        return domain;
    }

    public String getAccountSid() {
        return accountSid;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getFromPhoneNumber() {
        return fromPhoneNumber;
    }

    public String getRecipient() {
        return recipient;
    }
}
