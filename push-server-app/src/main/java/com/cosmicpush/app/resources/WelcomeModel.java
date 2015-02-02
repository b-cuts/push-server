package com.cosmicpush.app.resources;

public class WelcomeModel {

  private final boolean authenticated;

  public WelcomeModel(boolean authenticated) {
    this.authenticated = authenticated;
  }

  public boolean isAuthenticated() {
    return authenticated;
  }
}
