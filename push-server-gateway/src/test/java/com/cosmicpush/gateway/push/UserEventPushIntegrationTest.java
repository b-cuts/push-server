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

package com.cosmicpush.gateway.push;

import com.cosmicpush.gateway.*;
import org.testng.annotations.Test;

@Test(groups = {"integration"})
public class UserEventPushIntegrationTest {

  private String url = "https://www.localhost/api";
  private CosmicPushGateway gateway = new LiveCosmicPushGateway(url, "TestMonster", "ImJustTesting");

  public void testPush() throws Exception {
  }

  public void rapidPush() throws Exception {
    int max = 100 * 1000 / 5;
    long start = System.currentTimeMillis();
    for (int i = 0; i < max; i++) {
      testPush();
    }
    long end = System.currentTimeMillis() - start;
    System.out.printf("Completed in %s seconds", end/1000);
    Thread.sleep(1000);
  }
}
