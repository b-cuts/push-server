package com.cosmicpush.pub.internal;

import org.crazyyak.dev.common.id.uuid.TimeUuid;

public class CpIdGenerator {

  public static String newId() {
    return TimeUuid.randomUUID().toString();
  }
}
