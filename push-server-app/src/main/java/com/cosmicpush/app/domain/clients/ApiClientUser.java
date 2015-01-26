/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.domain.clients;

import com.cosmicpush.common.accounts.Account;
import com.cosmicpush.common.clients.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class ApiClientUser implements UserDetails {

  private final String accountId;
  private final String clientName;
  private final String clientPassword;

  public ApiClientUser(Account account, ApiClient apiClient) {
    this.accountId = account.getAccountId();
    this.clientName = apiClient.getClientName();
    this.clientPassword = apiClient.getClientPassword();
  }

  public String getAccountId() {
    return accountId;
  }

  public String getClientName() {
    return clientName;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getPassword() {
    return clientPassword;
  }

  @Override
  public String getUsername() {
    return clientName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
