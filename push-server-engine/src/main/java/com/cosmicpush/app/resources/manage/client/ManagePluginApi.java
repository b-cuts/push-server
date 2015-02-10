/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

package com.cosmicpush.app.resources.manage.client;

import com.cosmicpush.app.jaxrs.security.MngtAuthentication;
import com.cosmicpush.app.system.CpApplication;
import com.cosmicpush.common.clients.Domain;
import com.cosmicpush.common.plugins.Plugin;
import com.cosmicpush.common.system.ExecutionContext;
import com.cosmicpush.common.system.PluginManager;
import com.cosmicpush.pub.common.PushType;
import org.crazyyak.dev.common.exceptions.ExceptionUtils;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.net.URI;

@MngtAuthentication
public class ManagePluginApi {

  private final PushType pushType;
  private final Domain domain;
  private final ExecutionContext context = CpApplication.getExecutionContext();

  public ManagePluginApi(Domain domain, PushType pushType) {
    this.pushType = ExceptionUtils.assertNotNull(pushType, "pushType");
    this.domain = ExceptionUtils.assertNotNull(domain, "domain");
  }

  public Response redirect() throws Exception {
    String path = String.format("manage/domain/%s", domain.getDomainKey());
    return Response.seeOther(new URI(path)).build();
  }

  @POST
  public Response updateConfig(MultivaluedMap<String, String> formParams) throws Exception {
    Plugin plugin = PluginManager.getPlugin(pushType);
    plugin.updateConfig(context, domain, formParams);
    return redirect();
  }

  @POST
  @Path("/delete")
  public Response deleteConfig() throws Exception {
    Plugin plugin = PluginManager.getPlugin(pushType);
    plugin.deleteConfig(context, domain);
    return redirect();
  }

  @POST
  @Path("/test")
  public Response testConfig() throws Exception {
    Plugin plugin = PluginManager.getPlugin(pushType);
    plugin.test(context, domain);
    return redirect();
  }
}
