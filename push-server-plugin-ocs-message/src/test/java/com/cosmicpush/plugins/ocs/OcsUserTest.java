package com.cosmicpush.plugins.ocs;


import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class OcsUserTest {

  @Test
  public void authenticateOcsUserTest() {
    String username = "robertk";
    String password = "-t8erTaught1";
    String ntlmHostname = "localhost";
    String ntlmDomain = "stcg.net";

    OcsUser ocsUser = new OcsUser(username, password, ntlmHostname, ntlmDomain);
    assertNotNull(ocsUser);
    assertEquals(username, ocsUser.getUsername());
    assertEquals(ntlmHostname, ocsUser.getNtlmHostname());
    assertEquals(ntlmDomain, ocsUser.getNtlmDomain());
  }
}
