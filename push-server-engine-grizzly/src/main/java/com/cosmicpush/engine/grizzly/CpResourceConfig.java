package com.cosmicpush.engine.grizzly;

import com.cosmicpush.app.system.CpApplication;
import org.glassfish.jersey.server.ResourceConfig;

public class CpResourceConfig extends ResourceConfig {

  public CpResourceConfig(CpApplication application) {

    for (Class type : application.getClasses()) {
      register(type);
    }

    for (Object object : application.getSingletons()) {
      register(object);
    }

    addProperties(application.getProperties());
  }
}
