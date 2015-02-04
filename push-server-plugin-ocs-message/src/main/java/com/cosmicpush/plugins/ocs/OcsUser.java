package com.cosmicpush.plugins.ocs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.microsoft.security.ntlm.NtlmAuthenticator;
import org.microsoft.security.ntlm.NtlmSession;

public class OcsUser {

  private static final Log log = LogFactory.getLog(OcsUser.class);

  private static final String DEFAULT_SIP_EPID = "d3665ff3bd3e";
  private final NtlmSession ntlmSession;
  private final String ntlmHostname;
  private final String ntlmDomain;
  private final String username;

  public OcsUser(String username, String password, String ntlmHostname, String ntlmDomain) {
//    this.sipInstance = calculateSipUuid(DEFAULT_SIP_EPID);

    this.username = username;
    this.ntlmHostname = ntlmHostname;
    this.ntlmDomain = ntlmDomain;

    NtlmAuthenticator ntlmAuthentication = new NtlmAuthenticator(
            NtlmAuthenticator.NtlmVersion.ntlmv2,
            NtlmAuthenticator.ConnectionType.connectionless,
            ntlmHostname, ntlmDomain, username, password);

    ntlmSession = ntlmAuthentication.createSession();
    log.info("NTLM session connected");
  }

  public NtlmSession getNtlmSession() {
    return ntlmSession;
  }

  public String getNtlmHostname() {
    return ntlmHostname;
  }

  public String getNtlmDomain() {
    return ntlmDomain;
  }

  public String getUsername() {
    return username;
  }
}
