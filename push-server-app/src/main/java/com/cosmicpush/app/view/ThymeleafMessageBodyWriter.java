package com.cosmicpush.app.view;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import org.crazyyak.dev.common.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Provider
public class ThymeleafMessageBodyWriter implements MessageBodyWriter<Thymeleaf> {

  @Context UriInfo uriInfo;
  private final TemplateEngine engine;

  public ThymeleafMessageBodyWriter() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setTemplateMode("HTML5");
    templateResolver.setSuffix("");
    templateResolver.setPrefix("push-server-app/view");
    templateResolver.setCacheable(false);

    engine = new TemplateEngine();
    engine.setTemplateResolver(templateResolver);
    engine.addDialect(new Java8TimeDialect());
  }

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return Thymeleaf.class.equals(type);
  }

  @Override
  public long getSize(Thymeleaf thymeleaf, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(Thymeleaf thymeleaf, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
    String resource = thymeleaf.getView();

    org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
    context.setVariables(thymeleaf.getVariables());

    String baseUri = StringUtils.substring(uriInfo.getBaseUri(), 0, -1);
    context.setVariable("contextRoot", baseUri);

    StringWriter writer = new StringWriter();
    engine.process(resource, context, writer);

    String content = writer.toString();
    entityStream.write(content.getBytes());
  }
}
