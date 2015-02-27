/*
 * Copyright 2012 Jacob D Parr
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

package com.cosmicpush.jackson;

import com.cosmicpush.pub.common.PushType;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import java.io.IOException;

/**
 * User: harlan
 * Date: 7/21/12
 * Time: 10:36 PM
 */
public final class PushTypeDeserializer extends StdScalarDeserializer<PushType> {

  public PushTypeDeserializer() {
    super(PushType.class);
  }

  @Override
  public PushType deserialize(JsonParser jp, DeserializationContext context) throws IOException, JsonProcessingException {
    String code = jp.getValueAsString();
    return (code == null) ? null : PushType.find(code);
  }
}

