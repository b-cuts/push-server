/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.smtp;

import com.cosmicpush.common.actions.*;
import com.cosmicpush.common.clients.ApiClient;
import com.cosmicpush.common.config.SmtpAuthType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class UpdateSmtpEmailConfigActionTest {

  public void testUpdate() throws Exception {

    CreateClientAction clientAction = new CreateClientAction("mickey.mouse", "some.password");
    ApiClient apiClient = new ApiClient().create(clientAction);

    UpdateSmtpEmailConfigAction action = new UpdateSmtpEmailConfigAction(
      apiClient,
      "mickey.mouse", "IamMickey",
      SmtpAuthType.ssl, "google.com", "99",
      "to@example.com", "from@example.com", "override@example.com");


    SmtpEmailConfig config = new SmtpEmailConfig();
    config.apply(action);

    assertEquals(config.getApiClientId(), apiClient.getApiClientId());

    assertEquals(config.getUserName(), "mickey.mouse");
    assertEquals(config.getPassword(), "IamMickey");

    assertEquals(config.getPortNumber(),  "99");
    assertEquals(config.getAuthType(),    SmtpAuthType.ssl);
    assertEquals(config.getServerName(),  "google.com");

    assertEquals(config.getTestToAddress(),     "to@example.com");
    assertEquals(config.getTestFromAddress(),   "from@example.com");
    assertEquals(config.getRecipientOverride(), "override@example.com");
  }
}
