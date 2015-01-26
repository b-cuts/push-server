/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.system;

import com.cosmicpush.app.domain.clients.*;
import com.cosmicpush.common.accounts.*;
import com.cosmicpush.common.clients.*;
import com.cosmicpush.common.system.CpCouchServer;
import org.crazyyak.dev.common.BeanUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;

@Component
public class ApiSecurityProvider extends AbstractUserDetailsAuthenticationProvider {

  private final AccountStore accountStore;

  @Autowired
  public ApiSecurityProvider(CpCouchServer couchServer) {
    this.accountStore = new AccountStore(couchServer);
  }

  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    if (BeanUtils.objectsNotEqual(userDetails.getUsername(), authentication.getPrincipal())) {
      throw new UsernameNotFoundException(Account.INVALID_USER_NAME_OR_PASSWORD);

    } else if (BeanUtils.objectsNotEqual(userDetails.getPassword(), authentication.getCredentials())) {
      throw new BadCredentialsException(Account.INVALID_USER_NAME_OR_PASSWORD);
    }
  }

  @Override
  protected UserDetails retrieveUser(String clientName, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    Account account = accountStore.getByClientName(clientName);
    if (account == null) {
      throw new UsernameNotFoundException(Account.INVALID_USER_NAME_OR_PASSWORD);
    }

    ApiClient apiClient = account.getApiClientByName(clientName);
    if (apiClient == null) {
      throw new UsernameNotFoundException(Account.INVALID_USER_NAME_OR_PASSWORD);
    }

    return new ApiClientUser(account, apiClient);
  }
}
