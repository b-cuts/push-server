package com.cosmicpush.app.view;

import com.cosmicpush.common.system.Session;

import java.util.HashMap;
import java.util.Map;

public class Thymeleaf {

  private final String view;
  private final Map<String, Object>  variables = new HashMap<>();

//  public Thymeleaf(UriInfo uriInfo) {
//    this("/"+uriInfo.getPath(), Collections.emptyMap());
//  }
//
//  public Thymeleaf(String view) {
//    this(view, Collections.emptyMap());
//  }

//  public Thymeleaf(String view, Map<String, ?>  variables) {
//    this.view = view;
//    this.variables.putAll(variables);
//  }

  public Thymeleaf(Session session, String view, Object model) {
    this.view = view;
    this.variables.put("it", model);
    if (session != null) {
      this.variables.put("emailAddress", session.getEmailAddress());
    }
  }

  public String getView() {
    return view;
  }

  public Map<String, ?> getVariables() {
    return variables;
  }
}
