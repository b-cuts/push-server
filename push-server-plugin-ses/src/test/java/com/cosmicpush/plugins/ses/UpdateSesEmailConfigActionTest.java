/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.plugins.ses;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.test.TestFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class UpdateSesEmailConfigActionTest {

  private TestFactory testFactory;

  @BeforeClass
  public void beforeClass() throws Exception {
    testFactory = TestFactory.get();
  }

  public void testUpdate() throws Exception {

    Account account = testFactory.createAccount();
    Domain domain = testFactory.createDomain(account);

    UpdateSesEmailConfigAction updateAction = new UpdateSesEmailConfigAction(domain,
      "some-access-key-id", "some-secret-key", "email.us-west-2.amazonaws.com",
      "to@example.com", "from@example.com", "override@example.com");

    SesEmailConfig config = new SesEmailConfig();
    config.apply(updateAction);

    assertEquals(config.getDomainId(), domain.getDomainId());

    assertEquals(config.getAccessKeyId(), "some-access-key-id");
    assertEquals(config.getSecretKey(),   "some-secret-key");

    assertEquals(config.getTestToAddress(),     "to@example.com");
    assertEquals(config.getTestFromAddress(),   "from@example.com");
    assertEquals(config.getRecipientOverride(), "override@example.com");
  }
}
