/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.domain.accounts;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.accounts.AccountStore;
import com.cosmicpush.common.accounts.actions.CreateAccountAction;
import com.cosmicpush.test.TestFactory;
import org.crazyyak.dev.domain.account.AccountStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Test
public class AccountTest {

  private TestFactory testFactory;
  private AccountStore accountStore;

  @BeforeClass
  public void beforeClass() throws Exception {
    testFactory = TestFactory.get();
    accountStore = testFactory.getAccountStore();
  }

  public void testCreate() {

    CreateAccountAction createAccountAction = new CreateAccountAction(
      TestFactory.westCoastTimeZone,
      "test", "Test Parr <test@jacobparr.com>",
      "Unit", "Test",
      "testing123", "testing123"
    );

    Account account = new Account(createAccountAction);
    assertNotNull(account);

    AccountStatus status = account.getAccountStatus();
    assertEquals(status.isEnabled(), true);
    assertEquals(status.isAccountNonExpired(), true);
    assertEquals(status.isAccountNonLocked(), true);
    assertEquals(status.isAccountNonLocked(), true);

    accountStore.create(account);
  }
}
