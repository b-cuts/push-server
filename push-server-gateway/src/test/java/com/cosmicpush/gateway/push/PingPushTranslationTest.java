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
import com.cosmicpush.pub.common.PingPush;
import com.cosmicpush.pub.common.Push;
import org.crazyyak.dev.common.ComparisonResults;
import org.crazyyak.dev.common.EqualsUtils;
import org.crazyyak.dev.common.json.JsonTranslator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.InetAddress;

@Test
public class PingPushTranslationTest {

  private LiveCosmicPushGateway gateway = new LiveCosmicPushGateway("some-name", "some-password");
  private JsonTranslator translator = gateway.getClient().getTranslator();

  public void translatePingPush() throws Exception {
    Push oldPush = PingPush.newPush();
    String json = translator.toJson(oldPush);

    InetAddress remoteAddress = InetAddress.getLocalHost();
    String expected = String.format(EXPECTED_JSON, remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress());
    Assert.assertEquals(json, expected);

    Push newPush = translator.fromJson(PingPush.class, json);
    ComparisonResults results = EqualsUtils.compare(newPush, oldPush);
    results.assertValidationComplete();
  }

  private static final String EXPECTED_JSON = "{\n" +
    "  \"pushType\" : \"ping\",\n" +
    "  \"remoteHost\" : \"%s\",\n" +
    "  \"remoteAddress\" : \"%s\",\n" +
    "  \"traits\" : { }\n" +
    "}";
}
