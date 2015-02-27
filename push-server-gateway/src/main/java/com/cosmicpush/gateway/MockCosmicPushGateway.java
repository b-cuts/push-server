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
package com.cosmicpush.gateway;

import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.common.PushResponse;
import com.cosmicpush.pub.common.RequestStatus;
import org.crazyyak.dev.common.DateUtils;
import org.crazyyak.dev.common.id.uuid.TimeUuid;

import java.util.Collections;

public class MockCosmicPushGateway implements CosmicPushGateway {

  public MockCosmicPushGateway() {
  }

  @Override
  public long ping() {
    return 0;
  }

  @Override
  public PushResponse send(Push push) {
    return new PushResponse(
        "mock:"+TimeUuid.randomUUID().toString(),
        "mock:"+TimeUuid.randomUUID().toString(),
        DateUtils.currentDateTime(),
        RequestStatus.pending,
        Collections.<String>emptyList()
    );
  }

  @Override
  public PushResponse push(Push push) {
    return send(push);
  }
}
