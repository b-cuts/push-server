package com.cosmicpush.app.view;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;

import org.crazyyak.dev.common.IoUtils;
import org.crazyyak.dev.common.ReflectUtils;
import org.crazyyak.dev.common.StringUtils;
import org.thymeleaf.*;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.resourceresolver.ClassLoaderResourceResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

@Provider
public class ThymeleafMessageBodyWriter implements MessageBodyWriter<Thymeleaf> {

  UriInfo uriInfo;
  private final TemplateEngine engine;

  public ThymeleafMessageBodyWriter(@Context UriInfo uriInfo) {
    this.uriInfo = uriInfo;

    ClassPathTemplateResolver templateResolver = new ClassPathTemplateResolver();
    templateResolver.setTemplateMode("HTML5");
    templateResolver.setSuffix(ThymeleafViewFactory.TAIL);
    templateResolver.setPrefix(ThymeleafViewFactory.ROOT);
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

  public String getBaseUri() {
    return uriInfo.getBaseUri().toASCIIString();
  }

  @Override
  public void writeTo(Thymeleaf thymeleaf, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
    writeTo(thymeleaf, entityStream);
  }

  /** Provided mainly for testing, writes the thymeleaf to the specified writer. */
  public void writeTo(Thymeleaf thymeleaf, Writer writer) throws IOException, WebApplicationException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    writeTo(thymeleaf, out);
    String text = new String(out.toByteArray(), Charset.forName("UTF-8"));
    writer.write(text);
  }

  /** Writes the thymeleaf to the specified writer. */
  public void writeTo(Thymeleaf thymeleaf, OutputStream entityStream) throws IOException, WebApplicationException {
    String view = thymeleaf.getView();

    org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
    context.setVariables(thymeleaf.getVariables());

    String baseUri = StringUtils.substring(getBaseUri(), 0, -1);
    context.setVariable("contextRoot", baseUri);

    StringWriter writer = new StringWriter();
    engine.process(view, context, writer);

    String content = writer.toString();
    entityStream.write(content.getBytes());
  }
}
