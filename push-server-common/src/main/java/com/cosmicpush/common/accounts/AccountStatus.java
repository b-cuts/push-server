/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts;

import com.cosmicpush.common.accounts.actions.UpdateAccountStatusAction;
import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;

public class AccountStatus implements Serializable {

  private boolean enabled;
  private boolean credentialsNonExpired;
  private boolean accountNonLocked;
  private boolean accountNonExpired;

  @JsonCreator
  public AccountStatus(@JsonProperty("enabled") boolean enabled,
                       @JsonProperty("credentialsNonExpired") boolean credentialsNonExpired,
                       @JsonProperty("accountNonLocked") boolean accountNonLocked,
                       @JsonProperty("accountNonExpired") boolean accountNonExpired) {

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

  public void apply(UpdateAccountStatusAction operation) {
    this.enabled = operation.isEnabled();
    this.credentialsNonExpired = operation.isCredentialsNonExpired();
    this.accountNonLocked = operation.isAccountNonLocked();
    this.accountNonExpired = operation.isAccountNonExpired();
  }
}
