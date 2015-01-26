package com.cosmicpush.plugins.gtalk;

import java.net.URL;
import org.testng.Assert;
import org.testng.annotations.*;

@Test
public class GoogleTalkPluginTest {

  private GoogleTalkPlugin plugin;

  @BeforeClass
  public void beforeClass() throws Exception {
    URL url = getClass().getResource("/com/cosmicpush/plugins/gtalk/icon.png");
    Assert.assertNotNull(url);
  }
}