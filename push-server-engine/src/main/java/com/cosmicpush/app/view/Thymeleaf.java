package com.cosmicpush.app.view;

import java.util.*;
import javax.ws.rs.core.UriInfo;

public class Thymeleaf {

  private final String view;
  private final Map<String, Object>  variables = new HashMap<>();

  public Thymeleaf(UriInfo uriInfo) {
    this("/"+uriInfo.getPath(), Collections.emptyMap());
  }

  public Thymeleaf(String view) {
    this(view, Collections.emptyMap());
  }

  public Thymeleaf(String view, Map<String, ?>  variables) {
    this.view = view;
    this.variables.putAll(variables);
  }

  public Thymeleaf(String view, Object model) {
    this.view = view;
    this.variables.put("it", model);
  }

  public String getView() {
    return view;
  }

  public Map<String, ?> getVariables() {
    return variables;
  }
}
