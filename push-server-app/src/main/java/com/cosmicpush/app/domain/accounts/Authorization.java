package com.cosmicpush.app.domain.accounts;

import java.nio.charset.StandardCharsets;
import javax.xml.bind.DatatypeConverter;

public class Authorization {

  private final String key;
  private final String password;

  public Authorization(String key, String password) {
    this.key = key;
    this.password = password;
  }

  public String getKey() {
    return key;
  }

  public String getPassword() {
    return password;
  }

  public static Authorization fromBasicAuthHeader(String authHeader) {

    if (authHeader == null) {
      throw new IllegalArgumentException("The basic-auth header string must be specified.");
    } else if (authHeader.startsWith("Basic ") == false) {
      throw new IllegalArgumentException("The basic-auth header string must start with \"Basic \".");
    } else {
      authHeader = authHeader.substring(6);
    }

    byte[] bytes = DatatypeConverter.parseBase64Binary(authHeader);
    String basicAuth = new String(bytes, StandardCharsets.UTF_8);

    int pos = basicAuth.indexOf(":");

    if (pos < 0) {
      return new Authorization(basicAuth, null);
    } else {
      return new Authorization(
        basicAuth.substring(0, pos),
        basicAuth.substring(pos+1));
    }
  }
}
