package com.cosmicpush.app.system;

import java.util.TimeZone;
import junit.framework.TestCase;

public class CpJobsTest extends TestCase {

  public void testTimezones() {
    TimeZone timeZone = TimeZone.getTimeZone(CpJobs.PDT);
    assertNotNull(timeZone);
    assertEquals(timeZone.getDisplayName(), "Pacific Standard Time");

  }
}