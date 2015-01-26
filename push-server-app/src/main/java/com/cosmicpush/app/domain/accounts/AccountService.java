/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.domain.accounts;

import com.cosmicpush.common.accounts.queries.AccountQuery;
import com.cosmicpush.common.accounts.Account;

public interface AccountService {
  Account execute(AccountQuery query);
  Account execute(AccountRequest request);
}
