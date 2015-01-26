/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts.actions;

import com.cosmicpush.pub.internal.RequestErrors;
import java.util.*;
import org.crazyyak.dev.common.exceptions.ApiException;

public class UpdatePermissionsAction extends AccountAction {

  private final Set<String> roleTypes = new HashSet<String>();

  public UpdatePermissionsAction(Collection<String> roleTypes) {
    if (roleTypes != null) {
      this.roleTypes.addAll(roleTypes);
    }
  }

  public UpdatePermissionsAction(String roleTypeString) {
    if (roleTypeString == null) {
      roleTypeString = "";
    }
    for (String roleType : roleTypeString.split(",")) {
      roleType = roleType.trim();
      this.roleTypes.add(roleType);
    }
  }

  public Set<String> getRoleTypes() {
    return roleTypes;
  }

  @Override
  public RequestErrors validate(RequestErrors errors) throws ApiException {
    return errors;
  }
}
