package com.cosmicpush.jackson;

import com.cosmicpush.pub.common.Push;
import com.cosmicpush.pub.common.PushType;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
* Created by jacobp on 8/4/2014.
*/
public class CpJacksonModule extends SimpleModule {
  public CpJacksonModule() {
  }

  @Override
  public void setupModule(SetupContext context) {
    super.setupModule(context);

    setMixInAnnotation(Push.class, PushMixin.class);

    addSerializer(PushType.class, new PushTypeSerializer());
    addDeserializer(PushType.class, new PushTypeDeserializer());
  }
}
