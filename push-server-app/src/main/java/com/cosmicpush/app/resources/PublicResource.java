/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Viewable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;

public class PublicResource {

  public PublicResource() {
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Viewable getWelcome() {
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication auth = context.getAuthentication();
    return new Viewable("/public/welcome.jsp", new WelcomeModel(auth));
  }
}
