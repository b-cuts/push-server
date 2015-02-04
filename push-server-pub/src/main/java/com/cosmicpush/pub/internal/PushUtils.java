package com.cosmicpush.pub.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class PushUtils {

  private PushUtils() {
  }

  public static InetAddress getLocalHost() {
    try {
      return InetAddress.getLocalHost();
    } catch (UnknownHostException e) {
      throw new RuntimeException("Exception getting InetAddress for localhost.", e);
    }
  }
}
