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
import com.cosmicpush.pub.common.UserAgent;
import com.cosmicpush.pub.internal.CpRemoteClient;
import com.cosmicpush.pub.push.UserEventPush;
import com.cosmicpush.test.TestFactory;
import org.crazyyak.dev.common.BeanUtils;
import org.crazyyak.dev.common.ComparisonResults;
import org.crazyyak.dev.common.EqualsUtils;
import org.crazyyak.dev.common.json.JsonTranslator;
import org.crazyyak.dev.jackson.YakJacksonTranslator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Map;

@Test
public class UserEventPushTranslationTest {

  private TestFactory testFactory;
  private JsonTranslator translator = new YakJacksonTranslator();

  @BeforeClass
  public void beforeClass() throws Exception {
    testFactory = TestFactory.get();
  }

  public void translateUserEventPush() throws Exception {

    Map<String,String> map = BeanUtils.toMap("user:mickeym");
    UserAgent userAgent = testFactory.createUserAgent();

    CpRemoteClient remoteClient = new CpRemoteClient() {
      @Override public String getUserName() { return "mickey"; }
      @Override public String getIpAddress() { return "192.168.1.36"; }
      @Override public String getSessionId() { return "some-sessionId"; }
      @Override public String getDeviceId() { return "some-deviceId"; }
    };

    UserEventPush oldPush = UserEventPush.newPush(
        remoteClient,
        LocalDateTime.parse("2014-05-06T09:34"),
        "You logged in.",
        userAgent, "http://callback.com/api.sent", map);

    String json = translator.toJson(oldPush);

    InetAddress remoteAddress = InetAddress.getLocalHost();
    String expected = String.format(EXPECTED_JSON, remoteAddress.getCanonicalHostName(), remoteAddress.getHostAddress());
    Assert.assertEquals(json, expected);

    Push newPush = translator.fromJson(UserEventPush.class, json);
    ComparisonResults results = EqualsUtils.compare(newPush, oldPush);
    results.assertValidationComplete();
  }

  private static final String EXPECTED_JSON = "{\n" +
    "  \"sendStory\" : false,\n" +
    "  \"deviceId\" : \"some-deviceId\",\n" +
    "  \"sessionId\" : \"some-sessionId\",\n" +
    "  \"userName\" : \"mickey\",\n" +
    "  \"ipAddress\" : \"192.168.1.36\",\n" +
    "  \"createdAt\" : \"2014-05-06T09:34\",\n" +
    "  \"message\" : \"You logged in.\",\n" +
    "  \"userAgent\" : {\n" +
    "    \"agentType\" : \"agent-type\",\n" +
    "    \"agentName\" : \"agent-name\",\n" +
    "    \"agentVersion\" : \"agent-version\",\n" +
    "    \"agentLanguage\" : \"agent-language\",\n" +
    "    \"agentLanguageTag\" : \"agent-lang-tag\",\n" +
    "    \"osType\" : \"os-type\",\n" +
    "    \"osName\" : \"os-name\",\n" +
    "    \"osProducer\" : \"os=produceer\",\n" +
    "    \"osProducerUrl\" : \"osproducer-url\",\n" +
    "    \"osVersionName\" : \"os-version-name\",\n" +
    "    \"osVersionNumber\" : \"os-version-number\",\n" +
    "    \"linuxDistribution\" : \"linux-distro\"\n" +
    "  },\n" +
    "  \"callbackUrl\" : \"http://callback.com/api.sent\",\n" +
    "  \"remoteHost\" : \"%s\",\n" +
    "  \"remoteAddress\" : \"%s\",\n" +
    "  \"traits\" : {\n" +
    "    \"user\" : \"mickeym\"\n" +
    "  }\n" +
    "}";
}
