/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.common.config;

public enum Pop3AuthType {
  standard(110, "Standard"),
  ssl(995, "SSL");

  private final String defaultPort;
  private final String label;

  private Pop3AuthType(int defaultPort, String label) {
    this.defaultPort = String.valueOf(defaultPort);
    this.label = String.format("%s (%s)", label, defaultPort);
  }

  public String getLabel() {
    return label;
  }

  public boolean isStandard() {
    return this == standard;
  }

  public boolean isSsl() {
    return this == ssl;
  }

  public String getDefaultPort() {
    return defaultPort;
  }
}
