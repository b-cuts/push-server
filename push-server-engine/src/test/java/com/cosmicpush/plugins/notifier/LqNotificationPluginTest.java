package com.cosmicpush.plugins.notifier;

import org.testng.annotations.Test;

import java.net.URL;

import static org.testng.Assert.assertNotNull;

@Test
public class LqNotificationPluginTest {

  public void testResources() throws Exception {
    URL url = getClass().getResource("/com/cosmicpush/plugins/notifier/icon.png");
    assertNotNull(url);

    url = getClass().getResource("/com/cosmicpush/plugins/notifier/admin.html");
    assertNotNull(url);

    url = getClass().getResource("/META-INF/services/com.cosmicpush.common.plugins.Plugin");
    assertNotNull(url);
  }
}