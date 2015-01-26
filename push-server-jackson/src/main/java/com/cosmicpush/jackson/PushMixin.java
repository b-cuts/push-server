package com.cosmicpush.jackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

@JsonTypeInfo(use=JsonTypeInfo.Id.CUSTOM, property="pushType")
@JsonTypeIdResolver(PushJacksonResolver.class)
public abstract class PushMixin {

}
