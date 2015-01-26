/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.accounts;

public enum EcsPermission {

  ADMIN("Allows for the administration of the ECS System."),
  TEST_PURCHASE("Indicates that the user can complete a \"test\" purchase."),

  MANAGE("Indicates that the user can manage one or more restaurant's in the system."),
  MANAGE_ANY("Indicates that the user can manage any vendor in the system."),

  BETA_FEATURES("Indicates that the user has access to beta/test features.");

  private final String description;

  private EcsPermission(String description) {
    this.description = description;
  }

  public String getCode() {
    return name();
  }

  public String getDescription() {
    return description;
  }
}
