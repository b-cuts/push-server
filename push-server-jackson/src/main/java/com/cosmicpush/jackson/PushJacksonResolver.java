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

package com.cosmicpush.jackson;

import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.common.PushType;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class PushJacksonResolver implements TypeIdResolver {

  @Override
  public void init(JavaType baseType) {
  }

  @Override
  public String idFromValue(Object value) {
    Push push = (Push)value;
    return push.getPushType().getCode();
  }

  @Override
  public String idFromValueAndType(Object value, Class<?> suggestedType) {
    Push push = (Push)value;
    return push.getPushType().getCode();
  }

  @Override
  public String idFromBaseType() {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  public JavaType typeFromId(String id) {
    return typeFromId(null, id);
  }

  @Override
  public JavaType typeFromId(DatabindContext context, String id) {
    PushType pushType = PushType.find(id);
    return TypeFactory.defaultInstance().uncheckedSimpleType(pushType.getType());
  }

  @Override
  public JsonTypeInfo.Id getMechanism() {
    return JsonTypeInfo.Id.CUSTOM;
  }
}
