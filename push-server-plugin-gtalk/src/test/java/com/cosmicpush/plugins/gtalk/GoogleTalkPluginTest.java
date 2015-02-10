package com.cosmicpush.plugins.gtalk;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URL;

@Test
public class GoogleTalkPluginTest {

    private GoogleTalkPlugin plugin;

    @Test
    public void iconTest() throws Exception {
        URL url = getClass().getResource("/com/cosmicpush/plugins/gtalk/icon.png");
        Assert.assertNotNull(url);
    }

}