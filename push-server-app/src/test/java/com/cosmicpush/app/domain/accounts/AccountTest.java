/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.domain.accounts;

import com.cosmicpush.TestFactory;
import com.cosmicpush.common.accounts.*;
import com.cosmicpush.common.accounts.actions.CreateAccountAction;
import com.cosmicpush.common.actions.CreateClientAction;
import com.cosmicpush.common.clients.ApiClient;
import org.testng.annotations.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Test
public class AccountTest {

  private AccountStore accountStore;

  @BeforeClass
  public void beforeClass() throws Exception {
    accountStore = TestFactory.get().getAccountStore();
  }

  public void testCreate() {

    CreateAccountAction createAccount = new CreateAccountAction(
      false, TestFactory.westCoastTimeZone,
      "test", "Test Parr <test@jacobparr.com>",
      "Unit", "Test",
      "testing123", "testing123"
    );

    Account account = new Account(createAccount);

    CreateClientAction createClient = new CreateClientAction("mickey.mouse", "some.password");
    ApiClient apiClient = account.add(createClient);

    assertNotNull(apiClient);
    assertEquals(apiClient.getClientName(), "mickey.mouse");
    assertEquals(apiClient.getClientPassword(), "some.password");

    accountStore.create(account);
  }
}
