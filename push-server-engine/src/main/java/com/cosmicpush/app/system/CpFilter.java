/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.system;

import java.io.*;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.*;
import org.apache.commons.logging.*;

public class CpFilter implements ContainerResponseFilter {

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
}