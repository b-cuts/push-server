/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.ses;

import com.cosmicpush.common.actions.CreateClientAction;
import com.cosmicpush.common.clients.ApiClient;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class UpdateSesEmailConfigActionTest {

  public void testUpdate() throws Exception {

    CreateClientAction clientAction = new CreateClientAction("mickey.mouse", "some.password");
    ApiClient apiClient = new ApiClient().create(clientAction);

    UpdateSesEmailConfigAction action = new UpdateSesEmailConfigAction(apiClient,
      "some-access-key-id", "some-secret-key",
      "to@example.com", "from@example.com", "override@example.com");

    SesEmailConfig config = new SesEmailConfig();
    config.apply(action);

    assertEquals(config.getApiClientId(), apiClient.getApiClientId());

    assertEquals(config.getAccessKeyId(), "some-access-key-id");
    assertEquals(config.getSecretKey(),   "some-secret-key");

    assertEquals(config.getTestToAddress(),     "to@example.com");
    assertEquals(config.getTestFromAddress(),   "from@example.com");
    assertEquals(config.getRecipientOverride(), "override@example.com");
  }
}
