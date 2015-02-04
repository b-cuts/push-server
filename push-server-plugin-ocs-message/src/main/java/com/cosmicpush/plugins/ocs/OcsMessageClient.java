package com.cosmicpush.plugins.ocs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.microsoft.security.ntlm.NtlmAuthenticator;
import org.microsoft.security.ntlm.NtlmSession;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class OcsMessageClient {

  private static final Log log = LogFactory.getLog(OcsMessageClient.class);

  private static String PROPERTIES_FILE_NAME = "ntlm-example.properties";

  private final NtlmSession ntlmSession;
  private final String ntlmHostname;
  private final String ntlmDomain;
  private final String username;

  private static String sipHostname;
  private static String sipDomain;
  private static String sipServer;

  private static String TRANSPORT_STRING;

  // NTLM
  private String realm;
  private String targetname;

  public OcsMessageClient(String username, String password, String ntlmHostname, String ntlmDomain) throws IOException {
    this.username = username;
    this.ntlmHostname = ntlmHostname;
    this.ntlmDomain = ntlmDomain;

    NtlmAuthenticator ntlmAuthentication = new NtlmAuthenticator(
            NtlmAuthenticator.NtlmVersion.ntlmv2,
            NtlmAuthenticator.ConnectionType.connectionless,
            ntlmHostname, ntlmDomain, username, password);

    ntlmSession = ntlmAuthentication.createSession();
    log.info("NTLM session connected");

    loadProperties();
  }

  public void sendOcsMessage() {
    // Connect to Office Communicator Server and send message
  }

  public void connect() {

  }

  private void loadProperties() throws IOException {
    Properties properties = new Properties();
    File propertiesFile = new File(PROPERTIES_FILE_NAME);

    FileReader fileReader = new FileReader(propertiesFile);
    properties.load(fileReader);
    fileReader.close();

    sipHostname = properties.getProperty("sip.local.hostname");
    sipDomain = properties.getProperty("sip.domain");
    sipServer = properties.getProperty("sip.server");

    TRANSPORT_STRING = properties.getProperty("sip.transport");
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

  public static String getSipHostname() {
    return sipHostname;
  }

  public static String getSipDomain() {
    return sipDomain;
  }

  public static String getSipServer() {
    return sipServer;
  }

  public String getRealm() {
    return realm;
  }

  public String getTargetname() {
    return targetname;
  }
}
