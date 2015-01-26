/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.system;

import javax.servlet.*;
import org.crazyyak.dev.jerseyspring.YakJerseyWebAppInitializer;
import org.crazyyak.dev.servlet.Log4jServletContextListener;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.WebApplicationContext;

// Order required to ensure this initializer is run
// before Jersey 2.x Spring WebApplicationInitializer
@Order(0)
public class CpWebAppInitializer extends YakJerseyWebAppInitializer {

  public CpWebAppInitializer() {
  }

  @Override
  public Class<? extends ResourceConfig> getApplicationClass(ServletContext servletContext, WebApplicationContext appContext) {
    return CpApplication.class;
  }

  @Override
  public String getEnvironmentPropertyName(ServletContext servletContext, WebApplicationContext appContext) {
    return "cosmic.push.env";
  }

  @Override
  public String getProfilesPropertyName(ServletContext servletContext, WebApplicationContext appContext) {
    return "cosmic.push.profiles";
  }

  @Override
  protected String getSpringConfigLocation(ServletContext servletContext) {
    return null;
  }

  @Override
  protected Class<?>[] getSpringConfigClasses(ServletContext servletContext) {
    return new Class<?>[]{CpSpringConfig.class};
  }

  @Override
  protected void addListeners(ServletContext servletContext, WebApplicationContext appContext) {
    super.addListeners(servletContext, appContext);

    servletContext.setInitParameter(Log4jServletContextListener.LOG4J_LOG_LEVEL, "WARN");
    servletContext.addListener(Log4jServletContextListener.class);
  }

  @Override
  protected void addFilters(ServletContext servletContext, WebApplicationContext appContext) {
    // Spring security filter chain
    addSpringSecurityFilter(servletContext, appContext);

    addJerseyFilter(servletContext, appContext);
  }
}