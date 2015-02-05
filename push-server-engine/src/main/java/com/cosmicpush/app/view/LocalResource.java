package com.cosmicpush.app.view;

import javax.ws.rs.core.UriInfo;

public class LocalResource {

  private final String view;

  public LocalResource(String view) {
    this.view = view;
  }

  public LocalResource(UriInfo uriInfo) {
    this("/"+uriInfo.getPath());
  }

  public String getView() {
    return view;
  }
}
