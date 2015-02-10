package com.cosmicpush.plugins.twilio;

import com.cosmicpush.common.clients.Domain;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

@Test
public class TwilioConfigTest {

    @Test
    public void constructorTest() {

        String configId = "123";
        String revision = "456";
        String domainId = "789";
        String accountSid = "0987654321";
        String authToken = "2341234dkfasdfdasdfasd";
        String fromPhoneNumber = "+15552221111";
        String recipient = "+12225551111";
        String message = "test message";

        TwilioConfig twilioConfig = new TwilioConfig(configId, revision, domainId, accountSid, authToken, fromPhoneNumber, recipient, message);

        assertEquals(twilioConfig.getConfigId(), configId);
        assertEquals(twilioConfig.getRevision(), revision);
        assertEquals(twilioConfig.getDomainId(), domainId);
        assertEquals(twilioConfig.getAccountSid(), accountSid);
        assertEquals(twilioConfig.getAuthToken(), authToken);
        assertEquals(twilioConfig.getFromPhoneNumber(), fromPhoneNumber);
        assertEquals(twilioConfig.getRecipient(), recipient);
        assertEquals(twilioConfig.getMessage(), message);
    }

    @Test
    public void configApplyTest() {
        String revision = "456";
        String domainId = "789";
        String accountSid = "0987654321";
        String authToken = "2341234dkfasdfdasdfasd";
        String fromPhoneNumber = "+15552221111";
        String recipient = "+12225551111";
        String message = "test message";

        Domain domain = new Domain(domainId, revision, "89989998", "clientKey", "clientPass");
        UpdateTwilioConfigAction updateTwilioConfigAction = new UpdateTwilioConfigAction(domain, accountSid, authToken, fromPhoneNumber, recipient, message);
        TwilioConfig configClone = new TwilioConfig().apply(updateTwilioConfigAction);

        assertNull(configClone.getRevision());
        assertEquals(configClone.getDomainId(), domainId);
        assertEquals(configClone.getAccountSid(), accountSid);
        assertEquals(configClone.getAuthToken(), authToken);
        assertEquals(configClone.getFromPhoneNumber(), fromPhoneNumber);
        assertEquals(configClone.getRecipient(), recipient);
        assertEquals(configClone.getMessage(), message);
    }

}
