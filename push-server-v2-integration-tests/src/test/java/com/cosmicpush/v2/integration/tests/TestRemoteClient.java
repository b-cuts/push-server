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

package com.cosmicpush.v2.integration.tests;

import com.cosmicpush.pub.internal.CpRemoteClient;

public class TestRemoteClient implements CpRemoteClient {

  private final String userName;
  private final String deviceId;
  private final String sessionId;
  private final String ipAddress;

  public TestRemoteClient(String userName, String deviceId, String sessionId, String ipAddress) {
    this.userName = userName;
    this.deviceId = deviceId;
    this.sessionId = sessionId;
    this.ipAddress = ipAddress;
  }

  @Override
  public String getUserName() {
    return userName;
  }

  @Override
  public String getIpAddress() {
    return ipAddress;
  }

  @Override
  public String getSessionId() {
    return sessionId;
  }

  public String getDeviceId() {
    return deviceId;
  }
}
