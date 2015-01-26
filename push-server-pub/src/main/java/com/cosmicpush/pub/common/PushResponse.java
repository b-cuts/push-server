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

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class PushResponse implements Serializable {

  private final String accountId;
  private final String apiClientId;
  private final String apiRequestId;

  private final LocalDateTime createdAt;
  private final RequestStatus requestStatus;

  private final List<String> notes = new ArrayList<String>();

  @JsonCreator
  public PushResponse(@JsonProperty("accountId") String accountId,
                      @JsonProperty("apiClientId") String apiClientId,
                      @JsonProperty("apiRequestId") String apiRequestId,
                      @JsonProperty("createdAt") LocalDateTime createdAt,
                      @JsonProperty("requestStatus") RequestStatus requestStatus,
                      @JsonProperty("notes") Collection<String> notes) {

    this.accountId = accountId;
    this.apiClientId = apiClientId;
    this.apiRequestId = apiRequestId;

    this.createdAt = createdAt;
    this.requestStatus = requestStatus;

    this.notes.addAll(notes);
  }

  public String getAccountId() {
    return accountId;
  }

  public String getApiClientId() {
    return apiClientId;
  }

  public String getApiRequestId() {
    return apiRequestId;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public RequestStatus getRequestStatus() {
    return requestStatus;
  }

  public List<String> getNotes() {
    return notes;
  }
}
