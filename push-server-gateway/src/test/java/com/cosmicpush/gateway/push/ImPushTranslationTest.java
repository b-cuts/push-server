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

import com.cosmicpush.gateway.LiveCosmicPushGateway;
import com.cosmicpush.pub.common.*;
import com.cosmicpush.pub.push.GoogleTalkPush;
import org.crazyyak.dev.common.*;
import org.crazyyak.dev.common.json.JsonTranslator;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class ImPushTranslationTest {

  private LiveCosmicPushGateway gateway = new LiveCosmicPushGateway("some-name", "some-password");
  private JsonTranslator translator = gateway.getClient().getTranslator();

  public void translateImPush() throws Exception {
    Push oldPush = new GoogleTalkPush("mickey.mouse@disney.com", "Just calling to say hello", "http://callback.com/api.sent");
    String json = translator.toJson(oldPush);
    Assert.assertEquals(json, "{\n" +
        "  \"pushType\" : \"google-talk\",\n" +
        "  \"imType\" : \"googleTalk\",\n" +
        "  \"recipient\" : \"mickey.mouse@disney.com\",\n" +
        "  \"message\" : \"Just calling to say hello\",\n" +
        "  \"callbackUrl\" : \"http://callback.com/api.sent\"\n" +
        "}");

    Push newPush = translator.fromJson(GoogleTalkPush.class, json);
    ComparisonResults results = EqualsUtils.compare(newPush, oldPush);
    results.assertValidationComplete();
  }
}
