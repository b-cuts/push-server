/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.gtalk;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.test.TestFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class UpdateGoogleTalkConfigActionTest {

  private TestFactory testFactory;

  @BeforeClass
  public void beforeClass() throws Exception {
    testFactory = TestFactory.get();
  }

  public void testUpdate() throws Exception {

    Account account = testFactory.createAccount();
    Domain domain = testFactory.createDomain(account);

    UpdateGoogleTalkConfigAction updateAction = new UpdateGoogleTalkConfigAction(domain,
        "mickey.mouse", "IamMickey",
        "test@example.com", "override@example.com");

    GoogleTalkConfig config = new GoogleTalkConfig();
    config.apply(updateAction);

    assertEquals(config.getDomainId(), domain.getDomainId());

    assertEquals(config.getUserName(), "mickey.mouse");
    assertEquals(config.getPassword(), "IamMickey");

    assertEquals(config.getTestAddress(), "test@example.com");
    assertEquals(config.getRecipientOverride(), "override@example.com");
  }
}
