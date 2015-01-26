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

import com.cosmicpush.jackson.CpObjectMapper;
import com.cosmicpush.pub.common.*;
import com.cosmicpush.pub.internal.RequestErrors;
import org.crazyyak.dev.jackson.YakJacksonTranslator;
import org.crazyyak.lib.jaxrs.jackson.SimpleRestClient;

public class LiveCosmicPushGateway implements CosmicPushGateway {

  private final SimpleRestClient client;

  public LiveCosmicPushGateway(String userName, String password) {
    CpObjectMapper objectMapper = new CpObjectMapper();
    YakJacksonTranslator translator = new YakJacksonTranslator(objectMapper);
    client = new SimpleRestClient(translator, "http://www.cosmicpush.com/api/v2", userName, password);
  }

  public LiveCosmicPushGateway(String url, String userName, String password) {
    CpObjectMapper objectMapper = new CpObjectMapper();
    YakJacksonTranslator translator = new YakJacksonTranslator(objectMapper);
    client = new SimpleRestClient(translator, url, userName, password);
  }

  public LiveCosmicPushGateway(SimpleRestClient client) {
    this.client = client;
  }

  public SimpleRestClient getClient() {
    return client;
  }

  @Override
  public PushResponse push(Push push) {
    push.validate(new RequestErrors()).assertNoErrors();
    return getClient().post(PushResponse.class, "/pushes", push);
  }
}
