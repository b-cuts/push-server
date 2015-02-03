package com.cosmicpush.app.view;

import org.crazyyak.dev.common.exceptions.ExceptionUtils;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;

import java.io.InputStream;

public class ClassPathResourceResolver implements IResourceResolver {

  public ClassPathResourceResolver() {
  }

  @Override
  public String getName() {
    return "CLASSPATH";
  }

  @Override
  public InputStream getResourceAsStream(final TemplateProcessingParameters templateProcessingParameters, final String resourceName) {
    ExceptionUtils.assertNotNull(resourceName, "resourceName");
    InputStream is = getClass().getResourceAsStream(resourceName);
    if (is != null) return is;

    String msg = String.format("The resource \"%s\" was not found.", resourceName);
    throw new IllegalArgumentException(msg);
  }
}
