package com.cosmicpush.engine.grizzly;

import com.cosmicpush.app.system.CpApplication;
import org.glassfish.jersey.server.ResourceConfig;

public class CpResourceConfig extends ResourceConfig {

  public CpResourceConfig(CpApplication application) {

    application.getClasses().forEach(this::register);

    application.getSingletons().forEach(this::register);

    addProperties(application.getProperties());
  }
}
