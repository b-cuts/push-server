package com.cosmicpush.app.resources;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class WelcomeModel {

  private final Authentication authentication;

  public WelcomeModel(Authentication authentication) {
    this.authentication = authentication;
  }

  public Authentication getAuthentication() {
    return authentication;
  }

  public boolean isAuthenticated() {
    return authentication != null && authentication instanceof UsernamePasswordAuthenticationToken;
  }
}
