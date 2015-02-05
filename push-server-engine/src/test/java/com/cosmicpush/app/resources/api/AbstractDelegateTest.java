package com.cosmicpush.app.resources.api;

import com.cosmicpush.common.AbstractDelegate;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class AbstractDelegateTest {

  public void testGetUserName() throws Exception {

    assertEquals(AbstractDelegate.getUserName(null), null);
    assertEquals(AbstractDelegate.getUserName(""), null);

    assertEquals(AbstractDelegate.getUserName("http://www.google.com"), null);
    assertEquals(AbstractDelegate.getUserName("https://www.google.com"), null);

    assertEquals(AbstractDelegate.getUserName("http://donald.duck@www.google.com"), "donald.duck");
    assertEquals(AbstractDelegate.getUserName("https://donald.duck@www.google.com"), "donald.duck");

    assertEquals(AbstractDelegate.getUserName("http://donald.duck:some-password@www.google.com"), "donald.duck");
    assertEquals(AbstractDelegate.getUserName("https://donald.duck:some-password@www.google.com"), "donald.duck");
  }

  public void testGetPassword() throws Exception {

    assertEquals(AbstractDelegate.getPassword(null), null);
    assertEquals(AbstractDelegate.getPassword(""), null);

    assertEquals(AbstractDelegate.getPassword("http://www.google.com"), null);
    assertEquals(AbstractDelegate.getPassword("https://www.google.com"), null);

    assertEquals(AbstractDelegate.getPassword("http://donald.duck@www.google.com"), null);
    assertEquals(AbstractDelegate.getPassword("https://donald.duck@www.google.com"), null);

    assertEquals(AbstractDelegate.getPassword("http://donald.duck:some-password@www.google.com"), "some-password");
    assertEquals(AbstractDelegate.getPassword("https://donald.duck:some-password@www.google.com"), "some-password");
  }

  public void testStripAuthentication() throws Exception {

    assertEquals(AbstractDelegate.stripAuthentication(null), null);
    assertEquals(AbstractDelegate.stripAuthentication(""), "");

    assertEquals(AbstractDelegate.stripAuthentication("http://www.google.com"), "http://www.google.com");
    assertEquals(AbstractDelegate.stripAuthentication("https://www.google.com"), "https://www.google.com");

    assertEquals(AbstractDelegate.stripAuthentication("http://donald.duck@www.google.com"), "http://www.google.com");
    assertEquals(AbstractDelegate.stripAuthentication("https://donald.duck@www.google.com"), "https://www.google.com");

    assertEquals(AbstractDelegate.stripAuthentication("http://donald.duck:some-password@www.google.com"), "http://www.google.com");
    assertEquals(AbstractDelegate.stripAuthentication("https://donald.duck:some-password@www.google.com"), "https://www.google.com");
  }
}