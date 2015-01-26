/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.system;

import com.cosmicpush.common.accounts.queries.AccountQuery;
import com.cosmicpush.common.accounts.*;
import com.cosmicpush.common.system.CpCouchServer;
import org.crazyyak.dev.common.BeanUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;

@Component
public class DefaultSecurityProvider extends AbstractUserDetailsAuthenticationProvider {

  private final AccountStore accountStore;

  @Autowired
  public DefaultSecurityProvider(CpCouchServer couchServer) {
    this.accountStore = new AccountStore(couchServer);
  }

  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    AccountUser accountUser = (AccountUser)userDetails;

    if (BeanUtils.objectsNotEqual(userDetails.getUsername(), authentication.getPrincipal())) {
      throw new UsernameNotFoundException(Account.INVALID_USER_NAME_OR_PASSWORD);
    }

    if (BeanUtils.objectsNotEqual(userDetails.getPassword(), authentication.getCredentials())) {
      if (BeanUtils.objectsNotEqual(accountUser.getTempPassword(), authentication.getCredentials())) {
        throw new BadCredentialsException(Account.INVALID_USER_NAME_OR_PASSWORD);
      }
    }
  }

  @Override
  protected UserDetails retrieveUser(String userName, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    Account account = accountStore.get(AccountQuery.byUserName(userName));

    if (account == null || BeanUtils.objectsNotEqual(account.getUserName(), userName)) {
      throw new UsernameNotFoundException(Account.INVALID_USER_NAME_OR_PASSWORD);
    }

    return account.toUser();
  }
}
