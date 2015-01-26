/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.gtalk;

import com.cosmicpush.common.actions.*;
import com.cosmicpush.common.clients.ApiClient;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class UpdateGoogleTalkConfigActionTest {

  public void testUpdate() throws Exception {

    CreateClientAction clientAction = new CreateClientAction("mickey.mouse", "some.password");
    ApiClient apiClient = new ApiClient().create(clientAction);

    UpdateGoogleTalkConfigAction action = new UpdateGoogleTalkConfigAction(apiClient,
        "mickey.mouse", "IamMickey",
        "test@example.com", "override@example.com");

    GoogleTalkConfig config = new GoogleTalkConfig();
    config.apply(action);

    assertEquals(config.getApiClientId(), apiClient.getApiClientId());

    assertEquals(config.getUserName(), "mickey.mouse");
    assertEquals(config.getPassword(), "IamMickey");

    assertEquals(config.getTestAddress(), "test@example.com");
    assertEquals(config.getRecipientOverride(), "override@example.com");
  }
}
