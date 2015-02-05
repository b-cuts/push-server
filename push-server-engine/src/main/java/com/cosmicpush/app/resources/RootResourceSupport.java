package com.cosmicpush.app.resources;

import com.cosmicpush.app.view.LocalResource;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.crazyyak.dev.common.net.InetMediaType;

public abstract class RootResourceSupport {

  public RootResourceSupport() {
  }

  public abstract UriInfo getUriInfo();
  
  @GET
  @Produces(InetMediaType.IMAGE_PNG_VALUE)
  @Path("{resource: ([^\\s]+(\\.(?i)(png|PNG))$) }")
  public LocalResource renderPNGs() throws Exception {
    return new LocalResource(getUriInfo());
  }

  @GET
  @Produces(InetMediaType.IMAGE_GIF_VALUE)
  @Path("{resource: ([^\\s]+(\\.(?i)(gif|GIF))$) }")
  public LocalResource renderGIFs() throws Exception {
    return new LocalResource(getUriInfo());
  }

  @GET
  @Produces(InetMediaType.TEXT_PLAIN_VALUE)
  @Path("{resource: ([^\\s]+(\\.(?i)(txt|TXT|text|TEXT))$) }")
  public LocalResource renderText() throws Exception {
    return new LocalResource(getUriInfo());
  }

  @GET
  @Produces(InetMediaType.TEXT_HTML_VALUE)
  @Path("{resource: ([^\\s]+(\\.(?i)(html|HTML))$) }")
  public LocalResource renderHtml() throws Exception {
    return new LocalResource(getUriInfo());
  }

  @GET
  @Produces(InetMediaType.TEXT_CSS_VALUE)
  @Path("{resource: ([^\\s]+(\\.(?i)(css|CSS))$) }")
  public LocalResource renderCSS() throws Exception {
    return new LocalResource(getUriInfo());
  }

  @GET
  @Produces(InetMediaType.APPLICATION_JAVASCRIPT_VALUE)
  @Path("{resource: ([^\\s]+(\\.(?i)(js|JS))$) }")
  public LocalResource renderJavaScript() throws Exception {
    return new LocalResource(getUriInfo());
  }

  @GET
  @Produces(InetMediaType.IMAGE_ICON_VALUE)
  @Path("{resource: ([^\\s]+(\\.(?i)(ico|ICO))$) }")
  public LocalResource renderICOs() throws Exception {
    return new LocalResource(getUriInfo());
  }

  @GET
  @Produces(InetMediaType.APPLICATION_PDF_VALUE)
  @Path("{resource: ([^\\s]+(\\.(?i)(pdf|PDF))$) }")
  public LocalResource renderPDFs() throws Exception {
    return new LocalResource(getUriInfo());
  }

  @GET @Path("/trafficbasedsspsitemap.xml")
  public Response trafficbasedsspsitemap_xml() { return Response.status(404).build(); }

  @GET @Path("/apple-touch-icon-precomposed.png")
  public Response apple_touch_icon_precomposed_png() { return Response.status(404).build(); }

  @GET @Path("/apple-touch-icon.png")
  public Response apple_touch_icon_png() { return Response.status(404).build(); }

  @GET @Path("/manager/status")
  public Response managerStatus() throws Exception { return Response.status(404).build(); }

  @GET @Path("{resource: ([^\\s]+(\\.(?i)(php|PHP))$) }")
  public Response renderTXTs() throws Exception { return Response.status(404).build(); }
}
