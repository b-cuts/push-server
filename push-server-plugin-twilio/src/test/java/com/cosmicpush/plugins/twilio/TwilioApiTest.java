package com.cosmicpush.plugins.twilio;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class TwilioApiTest {

  @Test(enabled=false)
  public void sendSmsTest() throws TwilioRestException {
    String ACCOUNT_SID = "AC1584175437f3ba4d8697150c90334d9b";
    String AUTH_TOKEN = "0185f31b1ab051af661acd2d9a7d384f";

    TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

    // Build a filter for the MessageList
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("Body", "Hello? Is anybody out there?"));
    params.add(new BasicNameValuePair("To", "+15592886686"));
    params.add(new BasicNameValuePair("From", "+15596440005"));
    MessageFactory messageFactory = client.getAccount().getMessageFactory();
    Message message = messageFactory.create(params);
  }
}
