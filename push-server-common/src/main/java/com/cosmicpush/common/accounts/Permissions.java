/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import java.util.*;

public class Permissions implements Serializable {

  private Set<String> roleTypes = new HashSet<>();

  public Permissions() {
  }

  public Permissions(Set<String> roleTypes) {
    if (roleTypes != null) {
      this.roleTypes.addAll(roleTypes);
    }
  }

  public Set<String> getRoleTypes() {
    return Collections.unmodifiableSet(roleTypes);
  }

  public void setRoleTypes(Set<String> roleTypes) {
    this.roleTypes.clear();
    this.roleTypes.addAll(roleTypes);
  }

  public boolean contains(EcsPermission permission) {
    return roleTypes.contains(permission.name());
  }

  public boolean addRoleType(String roleType) {
    return roleTypes.add(roleType);
  }

  public boolean removeRoleType(String roleType) {
    return roleTypes.remove(roleType);
  }

  public boolean canManageVendor() {
    String permission = EcsPermission.MANAGE.name();
    return roleTypes.contains(permission);
  }

  public boolean canManageAnyVendor() {
    String permission = EcsPermission.MANAGE_ANY.name();
    return roleTypes.contains(permission);
  }

  public boolean canPlaceTestOrder() {
    String permission = EcsPermission.TEST_PURCHASE.name();
    return roleTypes.contains(permission);
  }

  @JsonIgnore
  public boolean isAdmin() {
    return contains(EcsPermission.ADMIN);
  }

  @JsonIgnore
  public boolean isNotAdmin() {
    return contains(EcsPermission.ADMIN) == false;
  }

  @JsonIgnore
  public boolean isTestPurchaser(){
    return contains(EcsPermission.TEST_PURCHASE);
  }

  @JsonIgnore
  public boolean isManager() {
    return contains(EcsPermission.MANAGE) || contains(EcsPermission.MANAGE_ANY);
  }
  @JsonIgnore
  public boolean isNotManager() {
    return isManager() == false;
  }

  @JsonIgnore
  public boolean isManagerOfAny() {
    return contains(EcsPermission.MANAGE_ANY);
  }

  @JsonIgnore
  public boolean isBetaUser() {
    return contains(EcsPermission.BETA_FEATURES);
  }
}
