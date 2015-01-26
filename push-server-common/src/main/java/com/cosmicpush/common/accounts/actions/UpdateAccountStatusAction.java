/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts.actions;

import com.cosmicpush.pub.internal.RequestErrors;
import org.crazyyak.dev.common.exceptions.ApiException;

public class UpdateAccountStatusAction extends AccountAction {

  private final boolean enabled;
  private final boolean credentialsNonExpired;
  private final boolean accountNonLocked;
  private final boolean accountNonExpired;

  public UpdateAccountStatusAction(boolean enabled, boolean credentialsNonExpired, boolean accountNonLocked, boolean accountNonExpired) {
    this.enabled = enabled;
    this.credentialsNonExpired = credentialsNonExpired;
    this.accountNonLocked = accountNonLocked;
    this.accountNonExpired = accountNonExpired;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) throws ApiException {
    return errors;
  }
}
