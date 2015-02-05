package com.cosmicpush.app.view;

import org.thymeleaf.templateresolver.TemplateResolver;

public class ClassPathTemplateResolver extends TemplateResolver {
  public ClassPathTemplateResolver() {
    super();
    super.setResourceResolver(new ClassPathResourceResolver());
  }
}
