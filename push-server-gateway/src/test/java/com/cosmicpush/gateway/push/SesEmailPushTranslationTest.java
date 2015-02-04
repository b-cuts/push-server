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
import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.push.*;
import org.crazyyak.dev.common.*;
import org.crazyyak.dev.common.json.JsonTranslator;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class SesEmailPushTranslationTest {

  private LiveCosmicPushGateway gateway = new LiveCosmicPushGateway("some-name", "some-password");
  private JsonTranslator translator = gateway.getClient().getTranslator();

  public void translateEmailPush() throws Exception {
    Push oldPush = SesEmailPush.newPush("mickey.mouse@disney.com", "donald.duck@disney.com", "This is the subject", "<html><body><h1>Hello World</h1>So, how's it going?</body></html>", "http://callback.com/api.sent", null, "test:true", "type:email");
    String json = translator.toJson(oldPush);
    Assert.assertEquals(json, "{\n" +
        "  \"pushType\" : \"ses-email\",\n" +
        "  \"toAddress\" : \"mickey.mouse@disney.com\",\n" +
        "  \"fromAddress\" : \"donald.duck@disney.com\",\n" +
        "  \"emailSubject\" : \"This is the subject\",\n" +
        "  \"htmlContent\" : \"<h1>Hello World</h1>So, how's it going?\",\n" +
        "  \"callbackUrl\" : \"http://callback.com/api.sent\",\n" +
        "  \"traits\" : {\n" +
        "    \"test\" : \"true\",\n" +
        "    \"type\" : \"email\"\n" +
        "  }\n" +
        "}");

    Push newPush = translator.fromJson(SesEmailPush.class, json);
    ComparisonResults results = EqualsUtils.compare(newPush, oldPush);
    results.assertValidationComplete();
  }
}
