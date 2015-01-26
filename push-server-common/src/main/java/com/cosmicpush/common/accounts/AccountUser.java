/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts;

import java.util.*;
import org.crazyyak.dev.common.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AccountUser implements UserDetails {

  private final String userName;
  private final String accountId;

  private final String password;
  private final String tempPassword;

  private final Permissions permissions;
  private final AccountStatus accountStatus;

  public AccountUser(Account account) {
    this.accountId = account.getAccountId();
    this.userName = account.getUserName();

    this.password = account.getPassword();
    this.tempPassword = account.getTempPassword();

    this.permissions = account.getPermissions();
    this.accountStatus = account.getAccountStatus();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    for (String roleType : permissions.getRoleTypes()) {
      if (StringUtils.isNotBlank(roleType)) {
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(roleType);
        authorities.add(grantedAuthority);
      }
    }
    return Collections.unmodifiableCollection(authorities);
  }

  @Override
  public String getPassword() {
    return password;
  }

  public String getAccountId() {
    return accountId;
  }

  @Override
  public String getUsername() {
    return userName;
  }
  public String getUserName() {
    return userName;
  }

  public String getTempPassword() {
    return tempPassword;
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountStatus.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountStatus.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return accountStatus.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return accountStatus.isEnabled();
  }

  public boolean equals(Object object) {
    if (object instanceof AccountUser) {
      AccountUser that = (AccountUser)object;
      return this.getUserName().equals(that.getUserName());
    }
    return false;
  }
}
