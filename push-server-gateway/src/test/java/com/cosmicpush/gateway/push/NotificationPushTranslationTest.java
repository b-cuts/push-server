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

import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.push.LqNotificationPush;
import org.crazyyak.dev.common.*;
import org.crazyyak.dev.common.json.JsonTranslator;
import org.crazyyak.dev.jackson.YakJacksonTranslator;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(enabled=false)
public class NotificationPushTranslationTest {

  private JsonTranslator translator = new YakJacksonTranslator();

  public void translateNotificationPush() throws Exception {
    Push oldPush = LqNotificationPush.newPush("Hey, you need to check this out.", "http://callback.com/api.sent", null, "test:true", "type:warning");
    String json = translator.toJson(oldPush);
    Assert.assertEquals(json, "{\n" +
        "  \"pushType\" : \"notification\",\n" +
        "  \"message\" : \"Hey, you need to check this out.\",\n" +
        "  \"callbackUrl\" : \"http://callback.com/api.sent\",\n" +
        "  \"traits\" : {\n" +
        "    \"test\" : \"true\",\n" +
        "    \"type\" : \"warning\"\n" +
        "  }\n" +
        "}");

    Push newPush = translator.fromJson(LqNotificationPush.class, json);
    ComparisonResults results = EqualsUtils.compare(newPush, oldPush);
    results.assertValidationComplete();
  }
}
