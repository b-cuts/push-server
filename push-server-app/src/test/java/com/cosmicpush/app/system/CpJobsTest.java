package com.cosmicpush.app.system;

import java.util.TimeZone;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Test
public class CpJobsTest {

  public void testTimezones() {
    TimeZone timeZone = TimeZone.getTimeZone(CpJobs.PDT);
    assertNotNull(timeZone);
    assertEquals(timeZone.getDisplayName(), "Pacific Standard Time");

  }
}