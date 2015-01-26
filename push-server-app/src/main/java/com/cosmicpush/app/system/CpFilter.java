/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.system;

import java.io.*;
import java.util.zip.GZIPOutputStream;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.apache.commons.logging.*;
import org.crazyyak.dev.common.IoUtils;

public class CpFilter implements ContainerResponseFilter, WriterInterceptor {

  private static final Log log = LogFactory.getLog(CpFilter.class);

  public CpFilter() {
    log.info("Created");
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
    responseContext.getHeaders().add("X-UA-Compatible", "IE=Edge");
    responseContext.getHeaders().add("p3p", "CP=\"Cosmic Push does not have a P3P policy. Learn why here: https://www.CosmicPush.com/static/p3p.html\"");
  }

  @Override
  public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
    MediaType mediaType = context.getMediaType();
    if (MediaType.TEXT_HTML_TYPE.equals(mediaType) == false) {
      context.proceed();
      return;
    }
    OutputStream outputStream = context.getOutputStream();
    context.setOutputStream(new WrappedOutputStream(outputStream));
    context.proceed();
  }

  private static class WrappedOutputStream extends OutputStream {
    private final OutputStream wrappedStream;
    private final ByteArrayOutputStream internalStream = new ByteArrayOutputStream();
    private WrappedOutputStream(OutputStream wrappedStream) {
      this.wrappedStream = wrappedStream;
    }
    @Override public void write(int b) throws IOException {
      internalStream.write(b);
    }
    @Override public void write(byte[] b) throws IOException {
      internalStream.write(b);
    }
    @Override public void write(byte[] b, int off, int len) throws IOException {
      internalStream.write(b, off, len);
    }
    @Override
    public void close() throws IOException {
      String content = new String(internalStream.toByteArray());
      try {
        if (content.contains("<!DOCTYPE html>")) {
          content = content.replace("<!DOCTYPE html>", "");
          content = "<!DOCTYPE html>" + content;
        }
        wrappedStream.write(content.getBytes());
      } finally {
        wrappedStream.close();
      }
    }
  }
}