/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cosmicpush.pub.common;

import java.io.Serializable;

public enum RequestStatus implements Serializable {

  pending("Pending"),
  processed("Processed"),
  failed("Failed"),
  warning("Warning"),
  denied("Denied");

  private final String label;

  RequestStatus(String label) {
    this.label = label;
  }

  public String getName() {
    return name();
  }

  public String getLabel() {
    return label;
  }

  public boolean isPending() {
    return this == pending;
  }

  public boolean isProcessed() {
    return this == processed;
  }

  public boolean isFailed() {
    return this == failed;
  }

  public boolean isWarning() {
    return this == warning;
  }

  public boolean isDenied() {
    return this == denied;
  }

  public String getColor() {
    if (isFailed()) return "red";
    if (isWarning()) return "yellow";
    if (isDenied()) return "orange";
    return "";
  }
}
