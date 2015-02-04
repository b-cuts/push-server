package com.cosmicpush.app.view;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import org.crazyyak.dev.common.IoUtils;

@Provider
public class LocalResourceMessageBodyWriter implements MessageBodyWriter<LocalResource> {

  @Context UriInfo uriInfo;

  public LocalResourceMessageBodyWriter() {
  }

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return LocalResource.class.equals(type);
  }

  @Override
  public long getSize(LocalResource localResource, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(LocalResource localResource, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
    String view = localResource.getView();
    String resource = "/push-server-app/view" + (view.startsWith("/") ? "" : "/") + view;
    InputStream is = getClass().getResourceAsStream(resource);

    if (is == null) {
      throw new NotFoundException("View: " + view);
    }

    byte[] bytes = IoUtils.toBytes(is);
    entityStream.write(bytes);
  }
}
